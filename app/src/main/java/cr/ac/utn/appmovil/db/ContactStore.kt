package cr.ac.utn.appmovil.db

import android.content.Context
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact

class ContactStore: IDBHelper {
    lateinit var dbHelper: ContactDBHelper

    constructor(context: Context){
        dbHelper = ContactDBHelper(context)
    }

    override fun add(contact: Contact){
        dbHelper.insertContact(contact)
    }

    override fun update(contact: Contact){
        dbHelper.updateContact(contact)
    }

    override  fun remove(id: String){
        try {
            dbHelper.deleteContact(id)
        }catch (e: Exception){
            throw e
        }
    }

    override fun getAll()= dbHelper.readAllContact()

    override fun getById(id: String): Contact {
        try {
            return dbHelper.readContact(id)
        }catch (e: Exception){
            throw e
        }
    }

    override fun getNames(): List<String> {
        val names = mutableListOf<String>()
        val contacts = dbHelper.readAllContact()
        contacts.forEach{ i-> names.add(i.FullName)}
        return names.toList()
    }
}