package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.DetailContactBinding

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: DetailContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize your views and set up the activity here
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val email = intent.getStringExtra("email")
        val note = intent.getStringExtra("note")



        binding.textNameDetail.text = name
        binding.textPhoneDetail.text = phoneNumber
        binding.textEmailDetail.text = email
        binding.textNoteDetail.text = note

    }
}