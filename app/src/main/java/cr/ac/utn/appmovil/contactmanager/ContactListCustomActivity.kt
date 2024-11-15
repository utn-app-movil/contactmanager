package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util

class ContactListCustomActivity : AppCompatActivity() {
    lateinit var lstContactList : ListView
    lateinit var model: ContactModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list_custom)

        model = ContactModel(this)
        lstContactList = findViewById<ListView>(R.id.lstContactListCustom)
        val contactArray = ArrayList<Contact>(model.getContacts())
        val adapter = ContactAdapter(this, R.layout.list_item_contact, model.getContacts())
        lstContactList.adapter = adapter

        lstContactList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val contacts = model.getContacts()
            val id = contacts[position].Id
            util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, id)
        }
    }
}