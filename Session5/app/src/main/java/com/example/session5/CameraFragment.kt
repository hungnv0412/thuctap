package com.example.session5

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.ScaleGestureDetector
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.session5.databinding.FragmentCameraBinding
import com.example.session5.utils.PermissionHelper
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraFragment : Fragment() {
    private lateinit var viewBinding : FragmentCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var lensFacing = CameraSelector.LENS_FACING_BACK
    private var flashMode = ImageCapture.FLASH_MODE_OFF
    private var camera : Camera? = null
    private lateinit var cameraProvider: ProcessCameraProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentCameraBinding.inflate(inflater, container, false)
        cameraExecutor = Executors.newSingleThreadExecutor()
        return viewBinding.root
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (PermissionHelper.allPermissionsGranted(requireContext())){
            startCamera()
        } else {
            PermissionHelper.requestCameraPermission(this)
        }
        viewBinding.switchCameraButton.setOnClickListener {
            switchCamera()
        }
        viewBinding.flashButton.setOnClickListener {
            switchFlashMode()
        }
        viewBinding.captureButton.setOnClickListener {
            takePhoto()
        }
        viewBinding.imagePreview.setOnClickListener {
        }
        loadLatestCapturedImage()
        val scaleGestureDetector = ScaleGestureDetector(requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener(){
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f
                    camera?.cameraControl?.setZoomRatio(currentZoomRatio * detector.scaleFactor)
                    return true
                }
            })
        viewBinding.viewFinder.setOnTouchListener { _,event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }
    }
    fun startCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    fun bindCameraUseCases(){
        val rotation = viewBinding.viewFinder.display.rotation
        val previewView = Preview.Builder()
            .setTargetRotation(rotation)
            .build().also {
                it.surfaceProvider = viewBinding.viewFinder.surfaceProvider
            }
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setFlashMode(flashMode)
            .setTargetRotation(rotation)
            .build()
        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing)
            .build()
        val orientationEventListener = object : OrientationEventListener(requireContext()){
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                val rotationDegrees = when (orientation) {
                    in 45..134 -> Surface.ROTATION_270
                    in 135..224 -> Surface.ROTATION_180
                    in 225..314 -> Surface.ROTATION_90
                    else -> Surface.ROTATION_0
                }
                imageCapture?.targetRotation = rotationDegrees
            }
        }
        orientationEventListener.enable()
        try {
            cameraProvider.unbindAll()
            camera = cameraProvider.bindToLifecycle(
                this, cameraSelector, previewView, imageCapture
            )
        }catch (exc: Exception) {
            Log.e("CameraFragment", "Use case binding failed", exc)
            Toast.makeText(requireContext(), "Use case binding failed: ${exc.message}", Toast.LENGTH_SHORT).show()
    }
    }
    fun takePhoto(){
        val imageCapture = imageCapture?:return
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS",java.util.Locale.US).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            requireContext().contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraFragment", "Photo capture failed: ${exception.message}", exception)
                    Toast.makeText(requireContext(), "Photo capture failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    outputFileResults.savedUri?.let { uri ->
                        animateCapturedImageToPreview(uri)
                        viewBinding.imagePreview.setOnClickListener {
                            openImageInExternalApp(uri)
                        }
                    }
                }
            }
        )
    }
    private fun loadLatestCapturedImage() {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED
        )

        val selection = "${MediaStore.Images.Media.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("Pictures/CameraX-Image%")
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val imageId = it.getLong(idColumn)
                val imageUri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageId.toString()
                )

                // Dùng Glide để load ảnh vào imagePreview
                Glide.with(this)
                    .load(imageUri)
                    .centerCrop()
                    .into(viewBinding.imagePreview)
            }
        }
    }

    private fun switchCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera()
    }
    private fun switchFlashMode() {
        flashMode = if (flashMode == ImageCapture.FLASH_MODE_OFF) {
            ImageCapture.FLASH_MODE_ON
        } else {
            ImageCapture.FLASH_MODE_OFF
        }
        val flashIcon = if (flashMode == ImageCapture.FLASH_MODE_ON) {
            R.drawable.icon_flash_on
        } else {
            R.drawable.icon_flash_off
        }
        viewBinding.flashButton.setImageResource(flashIcon)
        startCamera()
    }
    private fun openImageInExternalApp(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "image/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "No app found to view image", Toast.LENGTH_SHORT).show()
        }
    }
    private fun animateCapturedImageToPreview(imageUri: Uri) {
        val overlay = viewBinding.capturedImageOverlay
        val preview = viewBinding.imagePreview

        // Đặt ảnh và hiển thị overlay
        overlay.setImageURI(imageUri)
        overlay.visibility = View.VISIBLE
        overlay.alpha = 1f
        overlay.scaleX = 1f
        overlay.scaleY = 1f
        overlay.translationX = 0f
        overlay.translationY = 0f

        // Lấy vị trí global
        val overlayLoc = IntArray(2)
        val previewLoc = IntArray(2)
        overlay.getLocationOnScreen(overlayLoc)
        preview.getLocationOnScreen(previewLoc)

        val deltaX = (previewLoc[0] - overlayLoc[0]).toFloat()
        val deltaY = (previewLoc[1] - overlayLoc[1]).toFloat()

        val translateX = ObjectAnimator.ofFloat(overlay, View.TRANSLATION_X, deltaX)
        val translateY = ObjectAnimator.ofFloat(overlay, View.TRANSLATION_Y, deltaY)
        val scaleX = ObjectAnimator.ofFloat(overlay, View.SCALE_X, 0.3f)
        val scaleY = ObjectAnimator.ofFloat(overlay, View.SCALE_Y, 0.3f)
        val fadeOut = ObjectAnimator.ofFloat(overlay, View.ALPHA, 0f)

        val animatorSet = AnimatorSet().apply {
            playTogether(translateX, translateY, scaleX, scaleY, fadeOut)
            duration = 600
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        animatorSet.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                overlay.visibility = View.GONE
                preview.setImageURI(imageUri)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}