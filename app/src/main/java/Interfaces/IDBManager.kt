package Interfaces

import Entities.Contact

interface IDBManager {
    fun add(contact: Contact)
    fun update(contact: Contact)
    fun remove(id: String)
    fun getAll(): List<Contact>
    fun getById(id: String): Contact?
    fun getByFullName(fullName: String): Contact?
}