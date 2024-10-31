package cr.ac.utn.contactmanager

import Adapters.ContactListAdapter
import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import Util.util
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CustomListActivity : AppCompatActivity() {
    lateinit var contactModel: ContactModel
    lateinit var contactList: List<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        contactModel = ContactModel(this)
        val lstCustomList = findViewById<ListView>(R.id.lstCustomContactList)
        contactList = contactModel.getContacts()
        val adapter = ContactListAdapter(this, R.layout.contact_item_list, contactList)
        lstCustomList.adapter = adapter

        lstCustomList.onItemClickListener = AdapterView.OnItemClickListener{ adapter, view, position, id ->
            val fullName = contactList[position].fullName
            util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACT_ID, fullName)
        }
    }


}