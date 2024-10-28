package cr.ac.utn.contactmanager

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
import cr.ac.utn.R

class ContactListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val contactModel = ContactModel(this)
        val lstContact = findViewById<ListView>(R.id.lstContactList)
        val adapter = ArrayAdapter(this,
                    android.R.layout.simple_list_item_1,
                    contactModel.getContactNames())

        lstContact.adapter = adapter

        lstContact.onItemClickListener = AdapterView.OnItemClickListener{
           parent, view, position, id ->
            val itemValue = lstContact.getItemAtPosition(position) as String
            util.openActivity(this, ContactActivity::class.java
                , EXTRA_MESSAGE_CONTACT_ID, itemValue)
        }
    }
}