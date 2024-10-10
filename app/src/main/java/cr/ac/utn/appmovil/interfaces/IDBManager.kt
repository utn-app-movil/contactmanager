package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Contact

interface IDBManager {
    fun add (contact: Contact)
    fun update (contact: Contact)
    fun remove (id: String)
    //fun remove (contact: Contact)
    fun getAll(): List<Contact>
    fun getByid(id: String): Contact?
    fun getByFullName(fullName: String): Contact?
}