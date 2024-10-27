package Model

import Data.MemoryManager
import Entities.Contact
import Interfaces.IDBManager
import android.content.Context
import cr.ac.utn.contactmanager.R

class ContactModel(private val context: Context) {
    private var dbManager: IDBManager = MemoryManager

    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact {
        return dbManager.getById(id) ?: throw Exception(context.getString(R.string.msgContactNotFound))
    }

    fun getContactNames(): List<String> {
        return dbManager.getAll().map { it.fullName }
    }

    fun removeContact(id: String) {
        val result = dbManager.getById(id)
        if (result == null) {
            throw Exception(context.getString(R.string.msgContactNotFound))
        }
        dbManager.remove(id)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun getContactByFullName(fullName: String): Contact? {
        // Devuelve null si no encuentra el contacto en lugar de lanzar una excepción
        return dbManager.getByFullName(fullName)
    }
}
