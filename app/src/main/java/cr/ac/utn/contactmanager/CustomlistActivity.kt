package cr.ac.utn.contactmanager

import Adapter.ContaAdapter
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


class CustomlistActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        lateinit var ContactModel: ContactModel
        lateinit var Listcontact : List<Contact>

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_customlist)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val contactModel = ContactModel(this)
        val Contactlistview =  findViewById<ListView>(R.id.Contactlistwiew)
        Listcontact = contactModel.getContacts()


        val adapter = ContaAdapter(this, R.layout.activity_contact_list,Listcontact)
        Contactlistview.adapter = adapter
        Contactlistview.onItemClickListener =
            AdapterView.OnItemClickListener{parent,view,position,id -> val Cname = Listcontact[position].fullName
                util.openActivity(this,ContactActivity::class.java, EXTRA_MESSAGE_CONTACT_ID,Cname)
        }
    }
}