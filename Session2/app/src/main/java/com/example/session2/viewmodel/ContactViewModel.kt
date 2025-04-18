import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.session2.model.Contact
import com.example.session2.R
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor() : ViewModel() {
    private val _contacts = MutableLiveData<MutableList<Contact>>(
        mutableListOf(
            Contact("John Doe", "123456789", R.drawable.avt2, "john.doe@example.com", "Friend"),
            Contact("Jane Smith", "987654321", R.drawable.avt2, "jane.smith@example.com", "Colleague"),
            Contact("Alice Brown", "555666777", R.drawable.avt2, "alice.brown@example.com", "Family"),
            Contact("Bob Johnson", "444333222", R.drawable.avt2, "bob.johnson@example.com", "Neighbor"),
            Contact("nvh","12345",R.drawable.avt2,"@gmail.com","note here"),
        )
    )
    val contacts: LiveData<MutableList<Contact>> = _contacts
    fun addContact(contact: Contact) {
        _contacts.value?.add(contact)
        _contacts.value = _contacts.value
    }

    fun getContacts(): List<Contact> {
        return _contacts.value ?: emptyList()
    }
}
