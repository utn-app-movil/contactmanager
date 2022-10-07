package cr.ac.utn.appmovil.data

import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import java.util.function.Predicate

object MemoryManager: IDBManager {
    private var contactList = mutableListOf<Contact>()

    override fun add (contact: Contact){
        contactList.add(contact)
    }
    override fun update (contact: Contact){
        remove(contact.Id)
        contactList.add(contact)

    }
    override fun remove (id: String){
        var filterContact = Predicate {contactId: String -> contactId.trim().equals(id.trim())}
        contactList.filter { filterContact.test(it.Id) }.forEach{ contactList.remove(it) }
    }

    fun remove (contact: Contact){
        contactList.remove(contact)
    }

    override fun getAll(): List<Contact> = contactList.toList()

    override fun getByid(id: String): Contact? {
        try {
            var result = contactList.filter { (it.Id).equals(id) }
            return if(!result.any()) null else result[0]
        }catch (e: Exception){
            throw e
        }
    }
}