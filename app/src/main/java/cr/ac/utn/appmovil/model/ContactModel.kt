package cr.ac.utn.appmovil.model
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact

class ContactModel {
    companion object  {
        private var contactList = mutableListOf<Contact>()

        fun addContact(contact: Contact){
            contactList.add(contact)
        }

        fun removeContact(id: String){
            try {
                val result = contactList.filter { it.id == id }
                if (!result.any())
                    throw Exception(Resources.getSystem().getString(R.string.msgInvalidContact))

                contactList.remove(result[0])
            }catch (e: Exception){
                throw e
            }
        }

        fun getContacts()= contactList.toList()

        fun getContact(id: String): Contact{
            try {
                val result = contactList.filter { (it.fullName) == id }
                if (!result.any())
                    throw Exception(Resources.getSystem().getString(R.string.msgContactNoFound))

                return result[0]
            }catch (e: Exception){
                throw e
            }
        }

        fun getContactNames(): List<String> {
            val names = mutableListOf<String>()
            contactList.forEach{i-> names.add(i.fullName)}
            return names.toList()
        }
    }
}