package com.blopix.myapplication

import Model.ContactModel
import Util.util
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


const val EXTRA_MESSAGE_CONTACT_ID = "com.blopix.myapplication.contactId"

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
        val adapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, contactModel.getContactNames())

        lstContact.adapter = adapter
        lstContact.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val itemValue = lstContact.getItemAtPosition(position) as String
                util.openActivity(
                    this,
                    ContactActivity::class.java,
                    EXTRA_MESSAGE_CONTACT_ID,
                    itemValue
                )
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_contact -> {
                util.openActivity(this, ContactActivity::class.java)
                return true
            }

            //editar esta opcion para que no salga
            R.id.menu_viewContactList -> {
                util.openActivity(this, ContactListActivity::class.java)
                return false
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}