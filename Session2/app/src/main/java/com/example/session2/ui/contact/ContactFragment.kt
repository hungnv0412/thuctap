package com.example.session2.ui.contact

import com.example.session2.adapter.ContactAdapter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.session2.R
import com.example.session2.databinding.ContactFragmentBinding
import com.example.session2.data.contact.Contact
import com.example.session2.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var binding: ContactFragmentBinding
    private val viewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ContactFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Khởi tạo adapter
        contactAdapter = ContactAdapter(
            emptyList(),
            onItemClick = {
                contact ->
                val action = ContactFragmentDirections
                    .actionContactFragmentToDetailContactFragment(contact.id)
                findNavController().navigate(action)
            },
            onItemLongClick = { contact ->
                showDiaglogDelete(contact)
            }
        )
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // Hiển thị ProgressBar
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerViewContacts.visibility = View.GONE
            } else {
                // Ẩn ProgressBar
                binding.progressBar.visibility = View.GONE
                binding.recyclerViewContacts.visibility = View.VISIBLE
            }
        }
        recyclerView.adapter = contactAdapter
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contactAdapter.updateContacts(contacts)
        }
        binding.buttonAddGroup.setOnClickListener {
            findNavController().navigate(R.id.action_contactFragment_to_addContactFragment)
        }
        binding.editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val query = p0.toString()
                if (query.isNotEmpty()){
                    viewModel.searchContacts(query)
                }
                else{
                    viewModel.refreshContacts()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
    private fun showDiaglogDelete(contact: Contact) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Xoá liên hệ")
            .setMessage("Bạn có chắc chắn muốn xoá ${contact.name} không?")
            .setPositiveButton("Có") { _, _ ->
                viewModel.deleteContact(contact.id)
                Toast.makeText(requireContext(), "Đã xoá ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Không", null)
            .create()
        dialog.show()
    }

    override fun onPause() {
        super.onPause()
        binding.editTextSearch.setText("")
    }
}
