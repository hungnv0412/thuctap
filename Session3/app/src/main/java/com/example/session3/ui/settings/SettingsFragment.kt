package com.example.session3.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.session3.databinding.SettingsFragmentBinding
import com.example.session3.sharedPreferences.UserPreferences

@AndroidEntryPoint
class SettingsFragment : Fragment() {
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
    override fun onResume() {
        super.onResume()
        val savedname = UserPreferences.getUsername()
        Log.d("TAG", "onResume: $savedname")
        binding.textInput.setText(savedname)
    }

    override fun onPause() {
        super.onPause()
        UserPreferences.setUsername(binding.textInput.text.toString())
    }
}
