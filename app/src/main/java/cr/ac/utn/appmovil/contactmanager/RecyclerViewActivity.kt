package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.interfaces.*
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util

class RecyclerViewActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var customAdapter: RecyclerCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val recycler =  findViewById<RecyclerView>(R.id.rcvContactList)
        customAdapter = RecyclerCustomAdapter(ContactModel.getContacts(), this)
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = customAdapter
        customAdapter.notifyDataSetChanged()
    }

    override fun onItemClicked(contact: Contact) {
        util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, contact.FullName)
        //Toast.makeText(this,"contact name ${contact.FullName} \n Phone:${contact.Phone.toString()}",Toast.LENGTH_LONG)
          //  .show()
        //Log.i("CONTACT", contact.FullName)
    }
}