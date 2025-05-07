package com.example.session3.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.session3.R
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.viewModels
import com.example.session3.databinding.SettingsFragmentBinding

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    // Add settings-related UI and logic here
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
        val username = UserPreferences.getUsername()
        binding.textUserName.setText("hello $username")
        binding.btnSaveSettings.setOnClickListener{
            val nameInput = binding.textInput.text.toString()
            if (nameInput.isNotEmpty()) {
                UserPreferences.setUsername(nameInput)
                binding.textUserName.setText("hello $nameInput")
                Log.d("SettingsFragment", "Username saved: $nameInput")
            } else {
                Log.d("SettingsFragment", "Username input is empty")
            }

        }
}
}
