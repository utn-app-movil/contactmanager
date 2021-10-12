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
                var result = contactList.filter { it.Id.equals(id) }
                if (!result.any())
                    throw Exception(Resources.getSystem().getString(R.string.msgInvalidContact).toString())

                contactList.remove(result[0])
            }catch (e: Exception){
                throw e
            }
        }

        fun getContacts()= contactList.toList()

        fun getContact(id: String): Contact{
            try {
                var result = contactList.filter { (it.FullName).equals(id) }
                if (!result.any())
                    throw Exception(Resources.getSystem().getString(R.string.msgContactNoFound).toString())

                return result[0]
            }catch (e: Exception){
                throw e
            }
        }

        fun getContactNames(): List<String> {
            val names = mutableListOf<String>()
            contactList.forEach{i-> names.add(i.FullName)}
            return names.toList()
        }
    }
}