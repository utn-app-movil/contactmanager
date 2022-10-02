package cr.ac.utn.appmovil.model
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.MemoryManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactModel {
    private var dbManager: IDBManager = MemoryManager

    fun addContact(contact: Contact){
        dbManager.add(contact)
    }

    fun removeContact(id: String){
        val result = dbManager.getByid(id)
        if (result == null)
            throw Exception(Resources.getSystem().getString(R.string.msgInvalidContact))

        dbManager.remove(result)
    }

    fun getContacts()= dbManager.getAll()

    fun getContact(id: String): Contact{
        var result = dbManager.getByid(id)
        if (result == null){
            val system  = Resources.getSystem()
            throw Exception(system.getString(R.string.msgContactNoFound).toString())
        }
        return result
    }

    fun getContactNames(): List<String> {
        val names = mutableListOf<String>()
        dbManager.getAll().forEach{ i-> names.add(i.FullName)}
        return names.toList()
    }
}