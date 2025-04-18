package com.example.myapplication

import ContactAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.VelocityTrackerCompat.recycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.MainActivityBinding
import com.example.myapplication.models.Contact
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var recyclerViewContact: RecyclerView
    private lateinit var binding: MainActivityBinding
    private val sampleContacts = mutableListOf(
        Contact("Nguyễn Văn A", "0901234567", R.drawable.avt1),
        Contact("Trần Thị B", "0939876543", R.drawable.avt2),
        Contact("Lê Văn C", "0981122334", R.drawable.avt3),
        Contact("Phạm Thị D", "0912345678", R.drawable.avt2),
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        contactAdapter = ContactAdapter(context = this, sampleContacts,
            onItemClick = { contact ->
                showDetail(contact)
            },
            onItemLongClick = { contact ->
                showSnackbar(contact)
            }
        )

        recyclerViewContact = findViewById(R.id.recyclerViewContacts)
        recyclerViewContact.layoutManager = LinearLayoutManager(this)
        recyclerViewContact.adapter = contactAdapter
        binding.buttonAddContact.setOnClickListener {
            val intent = Intent(this, AddContactActivity::class.java)
            startActivityForResult(intent, 1)
        }

    }

    fun showDetail(contact: Contact) {
        // Handle the click event here
        // For example, you can start a new activity to show contact details
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("name", contact.name)
            putExtra("phoneNumber", contact.phoneNumber)
            putExtra("email", contact.email)
            putExtra("note", contact.note)
        }
        startActivity(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            data?.let {
                val newContact = Contact(
                    name = it.getStringExtra("name") ?: "",
                    phoneNumber = it.getStringExtra("phoneNumber") ?: "",
                    email = it.getStringExtra("email") ?: "",
                    note = it.getStringExtra("note") ?: "",
                    avatar = R.drawable.avt2 // Default avatar
                )
                sampleContacts.add(newContact)
                contactAdapter.notifyItemInserted(sampleContacts.size - 1)
            }
        }
    }
    fun showSnackbar(contact: Contact) {
        Snackbar.make(
            binding.root,
            "Bạn có muốn xóa ${contact.name} không?",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            // Handle the undo action here
            Toast.makeText(this, "Undo clicked", Toast.LENGTH_SHORT).show()
        }.show()
    }

}
