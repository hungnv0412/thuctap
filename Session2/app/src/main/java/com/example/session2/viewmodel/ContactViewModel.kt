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
            Contact(1,"John Doe", "123456789", R.drawable.avt2, "john.doe@example.com", "Friend"),
            Contact(2,"Jane Smith", "987654321", R.drawable.avt2, "jane.smith@example.com", "Colleague"),
            Contact(3,"Alice Brown", "555666777", R.drawable.avt2, "alice.brown@example.com", "Family"),
            Contact(4,"Bob Johnson", "444333222", R.drawable.avt2, "bob.johnson@example.com", "Neighbor"),
            Contact(5,"nvh","12345",R.drawable.avt2,"@gmail.com","note here"),
        )
    )
    val contacts: LiveData<MutableList<Contact>> = _contacts
    private var nextId: Int = _contacts.value?.maxOfOrNull { it.id }?.plus(1) ?: 1
    fun addContact(contact: Contact) {
        val newContact = contact.copy(id = nextId)
        _contacts.value?.add(newContact)
        _contacts.value = _contacts.value
    }

    fun getContacts(): List<Contact> {
        return _contacts.value ?: emptyList()
    }
    fun getContactById(id: Int): Contact? {
        return _contacts.value?.find { it.id == id }
    }
}
