package com.example.session5

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.session5.databinding.FragmentCameraBinding
import com.example.session5.utils.PermissionHelper
import com.example.session5.vision.BoundingBox
import com.example.session5.vision.Constants.LABELS_PATH
import com.example.session5.vision.Constants.MODEL_PATH
import com.example.session5.vision.Detector
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment(), Detector.DetectorListener {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var detector: Detector

    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var currentRecording: Recording? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private var isPause = false
    private var isFrontCamera = false
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var flashMode = ImageCapture.FLASH_MODE_AUTO

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result  ->
        if (result.all { it.value }) startCamera() // nếu tất cả quyền đã được cấp, bắt đầu camera
        else Toast.makeText(requireContext(), "Bạn cần cấp đầy đủ quyền", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        cameraExecutor = Executors.newSingleThreadExecutor()
        return binding.root
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDetector()
        checkPermissions()
        setupListeners()
    }

    private fun initDetector() {
        detector = Detector(requireContext(), MODEL_PATH, LABELS_PATH, this)
        detector.setup()
    }

    private fun checkPermissions() {
        if (PermissionHelper.hasAllPermissions(requireContext())) startCamera()
        else PermissionHelper.requestMissingPermissions(requireContext(), permissionLauncher)
    }

    private fun setupListeners() {
        with(binding) {
            switchCameraButton.setOnClickListener { switchCamera() }
            flashButton.setOnClickListener { switchFlashMode() }
            captureButton.setOnClickListener {
                animateFlashEffect()
                takePhoto()
            }
            pauseResumeButton.setOnClickListener {
                if (isPause) resumeRecording() else pauseRecording()
            }
            videoCaptureButton.setOnClickListener {
                if (currentRecording == null) videoRecording() else stopRecording()
            }
        }
        loadLatestCapturedMedia()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun bindCameraUseCases() {
        val rotation = binding.viewFinder.display?.rotation ?: Surface.ROTATION_0

        val preview = Preview.Builder()
            .setTargetRotation(rotation) //đặt hướng xoay của preview
            .build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider) // đặt surface provider cho preview
            }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_ZERO_SHUTTER_LAG) // chế độ chụp nhanh
            .setFlashMode(flashMode)// chế độ đèn flash
            .setTargetRotation(rotation) // đặt hướng xoay của ảnh chụp
            .build()

        val cameraSelector = CameraSelector.Builder() // chọn camera trước hoặc sau
            .requireLensFacing(lensFacing)
            .build()

        imageAnalyzer = createImageAnalysis() // tạo ImageAnalysis để phân tích ảnh

        val orientationListener = object : OrientationEventListener(requireContext()) { // lắng nghe sự thay đổi hướng của thiết bị
            override fun onOrientationChanged(orientation: Int) {
                val rotationDegrees = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotationDegrees // cập nhật hướng xoay của ảnh chụp
            }
        }
        orientationListener.enable()

        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalyzer) // liên kết các use case với lifecycle của Fragment
        } catch (e: Exception) {
            Log.e("CameraFragment", "Use case binding failed", e)
            Toast.makeText(requireContext(), "Use case binding failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun takePhoto() {
//        imageCapture?.takePicture(
//            ContextCompat.getMainExecutor(requireContext()),
//            object : ImageCapture.OnImageCapturedCallback() {
//                override fun onCaptureSuccess(image: ImageProxy) {
//                    binding.imagePreview.setImageBitmap(image.toBitmap())
//                    image.close()
//                }
//
//                override fun onError(exception: ImageCaptureException) {
//                    Log.e("CameraFragment", "Photo capture failed", exception)
//                }
//            })
        val contentValues = ContentValues().apply {
            val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: return
                    Glide.with(this@CameraFragment)
                        .load(savedUri)
                        .centerCrop()
                        .into(binding.imagePreview)
                    binding.imagePreview.setOnClickListener {
                        openMediaInExternalApp(savedUri, false)
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed", exception)
                }
            }
        )
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    private fun videoRecording() {
        bindUseCasesForVideoOnly()
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, name)
            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }

        val outputOptions = MediaStoreOutputOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).setContentValues(contentValues).build()

        currentRecording = videoCapture?.output
            ?.prepareRecording(requireContext(), outputOptions)
            ?.withAudioEnabled()
            ?.start(ContextCompat.getMainExecutor(requireContext())) { event -> handleVideoEvent(event) }
    }

    private fun handleVideoEvent(event: VideoRecordEvent) {
        with(binding) {
            when (event) {
                is VideoRecordEvent.Start -> {
                    videoCaptureButton.setImageResource(R.drawable.icon_record_active)
                    pauseResumeButton.visibility = View.VISIBLE
                    captureButton.visibility = View.GONE
                }
                is VideoRecordEvent.Pause -> {
                    pauseResumeButton.setImageResource(R.drawable.resume)
                    pauseRecording()
                }
                is VideoRecordEvent.Resume -> {
                    pauseResumeButton.setImageResource(R.drawable.pause)
                    resumeRecording()
                }
                is VideoRecordEvent.Finalize -> {
                    videoCaptureButton.setImageResource(R.drawable.icon_record_video)
                    pauseResumeButton.visibility = View.GONE
                    captureButton.visibility = View.VISIBLE
                    bindCameraUseCases()
                }
            }
        }
    }

    private fun bindUseCasesForVideoOnly() {
        binding.overlay.clear()

        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
        val rotation = binding.viewFinder.display?.rotation ?: Surface.ROTATION_0

        val preview = Preview.Builder()
            .setTargetRotation(rotation)
            .build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

        val recorder = Recorder.Builder()
            .setExecutor(cameraExecutor)
            .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
            .build()

        videoCapture = VideoCapture.withOutput(recorder)

        cameraProvider.unbindAll()
        camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
    }

    private fun resumeRecording() {
        isPause = false
        currentRecording?.resume()
    }

    private fun pauseRecording() {
        isPause = true
        currentRecording?.pause()
    }

    private fun stopRecording() {
        currentRecording?.stop()
        currentRecording = null
    }

    private fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK)
            CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
        bindCameraUseCases()
    }

    private fun switchFlashMode() {
        flashMode = when (flashMode) {
            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
            else -> ImageCapture.FLASH_MODE_OFF
        }
        val icon = when (flashMode) {
            ImageCapture.FLASH_MODE_OFF -> R.drawable.icon_flash_off
            ImageCapture.FLASH_MODE_ON -> R.drawable.icon_flash_on
            else -> R.drawable.icon_flash_auto
        }
        binding.flashButton.setImageResource(icon)
        bindCameraUseCases()
    }

    private fun createImageAnalysis(): ImageAnalysis {
        return ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display?.rotation ?: Surface.ROTATION_0)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()
            .apply {
                setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer{
                    analyzeImage(it)
                })
            }
    }

    private fun analyzeImage(imageProxy: ImageProxy) {
        val bitmapBuffer = createBitmap(imageProxy.width, imageProxy.height)
        try {
            bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer)
            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                if (isFrontCamera) postScale(-1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat())
            }
            val rotated = Bitmap.createBitmap(bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, matrix, true)
            detector.detect(rotated)
        } catch (e: Exception) {
            Log.e("Analyzer", "Exception during analysis", e)
        } finally {
            imageProxy.close()
        }
    }

    private fun loadLatestCapturedMedia() {
        val imageCursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED),
            "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?",
            arrayOf("Pictures/CameraX-Image%"),
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )
        val videoCursor = requireContext().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.Video.Media._ID, MediaStore.Video.Media.DATE_ADDED),
            "${MediaStore.Video.Media.RELATIVE_PATH} LIKE ?",
            arrayOf("Movies/CameraX-Video%"),
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )

        var latestUri: Uri? = null
        var isVideo = false
        var latestDate: Long = 0

        imageCursor?.use {
            if (it.moveToFirst()) {
                val date = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))
                if (date > latestDate) {
                    latestDate = date
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                    latestUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                    isVideo = false
                }
            }
        }
        videoCursor?.use {
            if (it.moveToFirst()) {
                val date = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))
                if (date > latestDate) {
                    latestDate = date
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                    latestUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id.toString())
                    isVideo = true
                }
            }
        }

        latestUri?.let { uri ->
            Glide.with(this)
                .load(uri)
                .centerCrop()
                .into(binding.imagePreview)
            binding.imagePreview.setOnClickListener {
                openMediaInExternalApp(uri, isVideo)
            }
        }
    }
    private fun openMediaInExternalApp(uri: Uri, isVideo: Boolean) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, if (isVideo) "video/*" else "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    }

    fun animateFlashEffect() {
        val flashView = binding.flashOverlay
        val fadeIn = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_in).apply { duration = 100 }
        val fadeOut = AnimationUtils.loadAnimation(requireContext(), android.R.anim.fade_out).apply { duration = 100 }
        flashView.visibility = View.VISIBLE
        flashView.startAnimation(fadeIn)
        fadeIn.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                flashView.startAnimation(fadeOut)
            }
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
        fadeOut.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                flashView.visibility = View.GONE
            }
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        detector.clear()
    }

    override fun onEmptyDetect() {
        binding.overlay.invalidate()
    }

    @SuppressLint("SetTextI18n")
    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        requireActivity().runOnUiThread {
            binding.overlay.setResults(boundingBoxes)
            binding.overlay.invalidate()
            binding.inferenceTime.text = "Inference time: ${inferenceTime}ms"
        }
    }
}