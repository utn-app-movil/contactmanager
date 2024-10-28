package Data

import Entities.Contact
import Interfaces.IDBManager

object MemoryManager : IDBManager {
    private var contactList = mutableListOf<Contact>()

    override fun add(contact: Contact) {
        contactList.add(contact)
    }

    override fun update(contact: Contact) {
        remove(contact.id)
        contactList.add(contact)
    }

    override fun remove(id: String) {
        contactList.removeIf { it.id.trim() == id.trim() }
    }

    override fun getAll(): List<Contact> = contactList.toList()

    override fun getById(id: String): Contact? {
        try {
            var result = contactList.filter { (it.id) == id }
            return if (!result.any()) null else result[0]
        } catch (e: Exception) {
            throw e
        }
    }

    override fun getByFullName(fullName: String): Contact? {
        try {
            var result = contactList.filter { (it.fullName) == fullName }
            return if (!result.any()) null else result[0]


        } catch (e: Exception) {
            throw e
        }
    }

}