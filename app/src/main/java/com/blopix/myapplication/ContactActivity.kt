package com.blopix.myapplication

import Entities.Contact
import Model.ContactModel
import Util.util
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ContactActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var contactModel: ContactModel

    private var isEditionMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        contactModel = ContactModel(this)
        txtId = findViewById<EditText>(R.id.txtContact_id)
        txtName = findViewById<EditText>(R.id.txtContact_name)
        txtLastName = findViewById<EditText>(R.id.txtContact_lastName)
        txtPhone = findViewById<EditText>(R.id.txtContact_phone)
        txtEmail = findViewById<EditText>(R.id.txtContact_email)
        txtAddress = findViewById<EditText>(R.id.txtContact_address)


        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)

        if (contactInfo != null && contactInfo != "") loadContact(contactInfo.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_menu, menu)

        if (isEditionMode) {
            menu?.findItem(R.id.menu_delete)?.isVisible = true
            menu?.findItem(R.id.menu_delete)?.isEnabled = true
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveContact()
                return true
            }

            R.id.menu_delete -> {
                deleteContact()
                return true
            }

            R.id.menu_cancel -> {
                cleanContact()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact() {
        try {
            val contact = Contact()
            contact.id = txtId.text.toString()
            contact.name = txtName.text.toString()
            contact.lastName = txtLastName.text.toString()
            contact.phone = txtPhone.text.toString()?.toInt()!!
            contact.email = txtEmail.text.toString()
            contact.address = txtAddress.text.toString()
            if (dataValidation(contact)) {
                if (isEditionMode == true) {
                    contactModel.updateContact(contact)
                    util.openActivity(this, ContactListActivity::class.java)
                    Toast.makeText(this, R.string.msgUpdate, Toast.LENGTH_LONG).show()
                } else {
                    contactModel.addContact(contact)
                    cleanContact()
                    util.openActivity(this, ContactListActivity::class.java)
                    Toast.makeText(this, R.string.msgSave, Toast.LENGTH_LONG).show()
                }

            } else {
                Toast.makeText(this, R.string.msgMissingData, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean {
        return contact.id.isNotEmpty() && contact.name.isNotEmpty() &&
                contact.lastName.isNotEmpty() && contact.email.isNotEmpty() &&
                contact.address.isNotEmpty() &&
                (contact.phone != null && contact.phone > 0)
    }

    private fun deleteContact() {
        if (isEditionMode) {
            val contactId = txtId.text.toString()
            if (contactId.isNotEmpty()) {
                contactModel.remContact(contactId)
                cleanContact()
                util.openActivity(this, ContactListActivity::class.java)
                Toast.makeText(this, R.string.msgDelete, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.msgMissingId, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, R.string.msgNotInEditMode, Toast.LENGTH_LONG).show()
        }
    }

    private fun cleanContact() {
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
    }

    private fun loadContact(contactInfo: String) {
        try {
            val contact = contactModel.getContactByFullName(contactInfo)
            txtId.setText(contact.id)
            txtName.setText(contact.name)
            txtLastName.setText(contact.lastName)
            txtEmail.setText(contact.email)
            txtPhone.setText(contact.phone.toString())
            txtAddress.setText(contact.address)
            isEditionMode = true
            txtId.isEnabled = false

            invalidateOptionsMenu()
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
