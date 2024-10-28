package Model

import Data.MemoryManager
import Entities.Contact
import Interfaces.IDBManager
import android.content.Context
import com.blopix.myapplication.R

class ContactModel(context: Context) {
    private var dbManager: IDBManager = MemoryManager
    private val _context: Context = context

    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact {
        val result = dbManager.getById(id)
        if (result == null) {
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String> {
        return dbManager.getAll().map { it.fullName }
    }

    fun remContact(id: String) {
        val result = dbManager.getById(id)
        if (result == null) {
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        dbManager.remove(id)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun getContactByFullName(fullName: String): Contact {
        val result = dbManager.getByFullName(fullName)
        if (result == null) {
            val message = _context.getString(R.string.msgContactNotFound)
            throw Exception(message)
        }
        return result
    }

    // Método isDuplicate para verificar duplicados por ID o correo electrónico
    fun isDuplicate(contact: Contact): Boolean {
        return dbManager.getAll().any {
            it.id == contact.id || it.email.equals(contact.email, ignoreCase = true)
        }
    }
}
