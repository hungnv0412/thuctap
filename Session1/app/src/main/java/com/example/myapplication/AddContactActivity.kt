package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.AddContactActivityBinding

class AddContactActivity: AppCompatActivity() {
    private lateinit var binding: AddContactActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddContactActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonSave.setOnClickListener {
            val name = binding.nameContent.text.toString()
            val phoneNumber = binding.phoneContent.text.toString()
            val email = binding.emailContent.text.toString()
            val note = binding.noteContent.text.toString()
            if (name.isEmpty()) {
                binding.newTextName.error = "Name cannot be empty"
                return@setOnClickListener
            }
            if (phoneNumber.isEmpty()) {
                binding.newTextPhone.error = "Phone number cannot be empty"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                binding.newTextEmail.error = "Email cannot be empty"
                return@setOnClickListener
            }
            val intent = Intent()
            intent.putExtra("name", name)
            intent.putExtra("phoneNumber", phoneNumber)
            intent.putExtra("email", email)
            intent.putExtra("note", note)
            setResult(RESULT_OK, intent)

            finish()
        }
        binding.buttonDiscard.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}