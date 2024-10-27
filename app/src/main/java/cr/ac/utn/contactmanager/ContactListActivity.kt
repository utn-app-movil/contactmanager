package cr.ac.utn.contactmanager

import Entities.Contact
import Interface.OnItemClickListener
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import Util.util
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ContactListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var contactListAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val contactList = findViewById<RecyclerView>(R.id.rvContactList)
        val contactModel = ContactModel(this)
        contactListAdapter = ContactListAdapter(contactModel.getContacts(), this)
        val layoutManager = LinearLayoutManager(applicationContext)
        contactList.layoutManager = layoutManager
        contactList.adapter = contactListAdapter
        //contactList.notifyDataSetChanged()
    }

    override fun onItemClicked (contact: Contact){
        util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACT_ID, contact.id.toString())
    }
}