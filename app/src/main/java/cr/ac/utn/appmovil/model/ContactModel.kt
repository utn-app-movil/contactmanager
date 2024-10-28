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

        fun updateContact(olId:String, contact: Contact){
            val delContact = getContact(olId)
            contactList.remove(delContact)
            contactList.add(contact)
        }

        fun deleteContact(id: String){
            try {
                val delContact = getContact(id)
                contactList.remove(delContact)
            }catch (e: Exception){
                throw e
            }
        }

        fun getContacts()= contactList.toList()

        fun getContact(id: String): Contact{
            try {
                var result = contactList.filter { (it.FullName).equals(id) }
                if (!result.any()){
                    val system  = Resources.getSystem()
                    throw Exception(system.getString(R.string.msgContactNoFound).toString())
                }
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