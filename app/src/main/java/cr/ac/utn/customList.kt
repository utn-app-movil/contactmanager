package cr.ac.utn

import Adapter.contactListAdapter
import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import Util.util
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.contactmanager.ContactActivity

class customList : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var contactModel: ContactModel
        lateinit var contactList: List<Contact>
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_custom_list2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        contactModel = ContactModel(this)
        val lstViewContact = findViewById<ListView>(R.id.lst_contact_view)
        contactList = contactModel.getContacts()

        val adapter = contactListAdapter(this,  R.layout.activity_contact_list,contactList)
        lstViewContact.adapter = adapter
        lstViewContact.onItemClickListener =AdapterView.OnItemClickListener{parent, view, position, id ->
            val fname =contactList[position].fullName
            util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACT_ID, fname)
        }



    }
}