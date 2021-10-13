package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.model.ContactModel

class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var customAdapter: RecyclerCustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val recycler =  findViewById<RecyclerView>(R.id.rcvContactList)
        customAdapter = RecyclerCustomAdapter(ContactModel.getContacts())
        val layoutManager = LinearLayoutManager(applicationContext)
        recycler.layoutManager = layoutManager
        recycler.adapter = customAdapter
        customAdapter.notifyDataSetChanged()
    }
}