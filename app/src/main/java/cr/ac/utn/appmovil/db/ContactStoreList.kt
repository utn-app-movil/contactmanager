package cr.ac.utn.appmovil.db

import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact

class ContactStoreList: IDBHelper {
    companion object {
        private var contactList = mutableListOf<Contact>()
    }
        override fun add(contact: Contact){
            contactList.add(contact)
        }

        override fun update(contact: Contact){
            val delContact = getById(contact.Id)
            contactList.remove(delContact)
            contactList.add(contact)
        }

        override  fun remove(id: String){
            try {
                val delContact = getById(id)
                contactList.remove(delContact)
            }catch (e: Exception){
                throw e
            }
        }

        override fun getAll()= contactList.toList()

        override fun getById(id: String): Contact {
            try {
                var result = contactList.filter { (it.Id).equals(id) }
                if (!result.any()){
                    val system  = Resources.getSystem()
                    throw Exception(system.getString(R.string.msgContactNoFound).toString())
                }
                return result[0]
            }catch (e: Exception){
                throw e
            }
        }

        override fun getNames(): List<String> {
            val names = mutableListOf<String>()
            contactList.forEach{i-> names.add(i.FullName)}
            return names.toList()
        }
}