package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util

class ContactListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val contactModel = ContactModel()
        val lstContactList = findViewById<ListView>(R.id.lstContactList)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactModel.getContactNames())
        lstContactList.adapter = adapter

        lstContactList.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val itemValue = lstContactList.getItemAtPosition(position) as String
                //Toast.makeText(applicationContext, "Position: $position\nItem Value: $itemValue", Toast.LENGTH_LONG).show()
                util.openActivity(applicationContext, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, itemValue)
            }
        }
    }
}