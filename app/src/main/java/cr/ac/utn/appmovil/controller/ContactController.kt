package cr.ac.utn.appmovil.controller
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.model.Contact

class ContactController {
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

        fun getContacts()= contactList
    }
}