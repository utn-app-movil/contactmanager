package cr.ac.utn.appmovil.model
import android.content.Context
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.db.ContactStore
import cr.ac.utn.appmovil.db.ContactStoreList
import cr.ac.utn.appmovil.db.IDBHelper
import cr.ac.utn.appmovil.identities.Contact

class ContactModel {
        private lateinit var contactDb: IDBHelper

        constructor(context: Context){
            contactDb = ContactStore(context) //ContactStoreList()
        }

        fun addContact(contact: Contact){
            contactDb.add(contact)
        }

        fun updateContact(olId:String, contact: Contact){
            contactDb.remove(olId)
            contactDb.add(contact)
        }

        fun deleteContact(id: String){
            try {
                contactDb.remove(id)
            }catch (e: Exception){
                throw e
            }
        }

        fun getContacts()= contactDb.getAll().toList()

        fun getContact(id: String): Contact{
            try {
                return contactDb.getById(id)
            }catch (e: Exception){
                throw e
            }
        }

        fun getContactNames(): List<String> {
            val names = mutableListOf<String>()
            val contactList = contactDb.getAll()
            contactList.forEach{i-> names.add(i.FullName)}
            return names.toList()
        }
}