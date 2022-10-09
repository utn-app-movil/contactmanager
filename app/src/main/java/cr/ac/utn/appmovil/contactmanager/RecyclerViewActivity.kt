package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import cr.ac.utn.appmovil.util.util
import cr.ac.utn.appmovil.interfaces.OnItemClickListener

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

    override fun onItemClicked  (contact: Contact) {
        util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, contact.FullName)
    }
}