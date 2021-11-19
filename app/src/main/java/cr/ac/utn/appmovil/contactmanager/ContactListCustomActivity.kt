package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util

class ContactListCustomActivity : AppCompatActivity() {
    lateinit var lstContactList : ListView
    lateinit var contactModel: ContactModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list_custom)

        contactModel= ContactModel(this)

        lstContactList = findViewById<ListView>(R.id.lstContactListCustom)
        val contactArray = ArrayList<Contact>(contactModel.getContacts())
        val adapter = ContactAdapter(this, R.layout.list_item_contact, contactModel.getContacts()) // ContactAdapter(this, ArrayList<Contact>(ContactModel.getContacts()))
        lstContactList.adapter = adapter

        lstContactList.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val contacts = contactModel.getContacts()
                val id = contacts[position].Id
                //Toast.makeText(applicationContext, itemValue, Toast.LENGTH_LONG).show()
                util.openActivity(applicationContext, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, id)
            }
        }
    }
}