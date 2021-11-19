package cr.ac.utn.appmovil.db

import cr.ac.utn.appmovil.identities.Contact

interface IDBHelper {
    fun add(contact: Contact)
    fun update(contact: Contact)
    fun remove(id: String)
    fun getById(id: String): Contact
    fun getAll(): List<Contact>
    fun getNames(): List<String>
}