package com.example.session3.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.session3.R
import com.example.session3.viewmodel.SettingsViewmodel
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.session3.databinding.SettingsFragmentBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    // Add settings-related UI and logic here
    private val viewModel : SettingsViewmodel by viewModels()
    private lateinit var binding: SettingsFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.username.observe(viewLifecycleOwner) { username ->
            binding.textUserName.setText(username)
        }
        binding.btnSaveSettings.setOnClickListener {
            val newName = binding.textUserName.text.toString()
            viewModel.saveUserName(newName)
        }
    }

}
