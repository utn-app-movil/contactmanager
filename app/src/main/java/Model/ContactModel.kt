package Model

import Data.MemoryManager
import Entities.Contact
import Interfaces.IDBManager
import android.content.Context
import android.content.res.Resources
import cr.ac.utn.contactmanager.R

class ContactModel {
    private var dbManager: IDBManager= MemoryManager
    private lateinit var _context: Context

    constructor(context: Context){
        _context= context
    }

    fun addContact (contact: Contact){
        dbManager.add(contact)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact{
        var result = dbManager.getById(id)
        if (result == null){
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String>{
        val names = mutableListOf<String>()
        dbManager.getAll().forEach { i-> names.add(i.fullName) }
        return names.toList()
    }

    fun removeContact(id: String){
        val result = dbManager.getById(id)
        if (result == null){
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun updateContact(contact: Contact){
        dbManager.update(contact)
    }

    fun getContactByFullName(fullName: String): Contact {
        var result = dbManager.getByFullName(fullName)
        if (result == null){
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        return result
    }

    // isDuplicate maneja la validacion de email y id, obtenemos los datos de db manager
    //y sacamos el is y email y decimos que si son iguales a los que tiene contact
    fun isDuplicate(contact: Contact): Boolean {
        return dbManager.getAll().any {
            it.id == contact.id || it.email.equals(contact.email, ignoreCase = true)
        }
    }
}