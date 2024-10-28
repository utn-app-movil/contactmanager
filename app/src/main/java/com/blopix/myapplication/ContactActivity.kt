package com.blopix.myapplication

import Entities.Contact
import Model.ContactModel
import Util.util
import android.os.Bundle
import android.text.InputFilter
import android.text.TextUtils
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
        setContentView(R.layout.activity_contact)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialización del modelo y campos
        contactModel = ContactModel(this)
        txtId = findViewById(R.id.txtContact_id)
        txtName = findViewById(R.id.txtContact_name)
        txtLastName = findViewById(R.id.txtContact_lastName)
        txtPhone = findViewById(R.id.txtContact_phone)
        txtEmail = findViewById(R.id.txtContact_email)
        txtAddress = findViewById(R.id.txtContact_address)

        setEditTextLimits()

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (!contactInfo.isNullOrEmpty()) loadContact(contactInfo)
    }

    // Establece límites de caracteres
    private fun setEditTextLimits() {
        txtId.filters = arrayOf(InputFilter.LengthFilter(10))
        txtName.filters = arrayOf(InputFilter.LengthFilter(30))
        txtLastName.filters = arrayOf(InputFilter.LengthFilter(30))
        txtPhone.filters = arrayOf(InputFilter.LengthFilter(10))
        txtAddress.filters = arrayOf(InputFilter.LengthFilter(50))
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
                true
            }

            R.id.menu_delete -> {
                showConfirmationDialog("delete")
                true
            }

            R.id.menu_cancel -> {
                cleanContact()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact() {
        try {
            val contact = Contact(
                id = txtId.text.toString(),
                name = txtName.text.toString(),
                lastName = txtLastName.text.toString(),
                phone = txtPhone.text.toString().toIntOrNull() ?: -1,
                email = txtEmail.text.toString(),
                address = txtAddress.text.toString()
            )

            if (dataValidation(contact)) {
                if (isEditionMode) {
                    showConfirmationDialog("update")
                } else {
                    if (contactModel.isDuplicate(contact)) {
                        Toast.makeText(this, R.string.msgDuplicate, Toast.LENGTH_LONG).show()
                    } else {
                        contactModel.addContact(contact)
                        Toast.makeText(this, R.string.msgSave, Toast.LENGTH_LONG).show()
                        util.openActivity(this, ContactListActivity::class.java)
                    }
                }
            } else {
                Toast.makeText(this, R.string.msgMissingData, Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean {
        val emailValid =
            !TextUtils.isEmpty(contact.email) && Patterns.EMAIL_ADDRESS.matcher(contact.email)
                .matches()
        return contact.id.isNotEmpty() && contact.name.isNotEmpty() &&
                contact.lastName.isNotEmpty() && emailValid &&
                contact.address.isNotEmpty() && contact.phone > 0
    }

    private fun showConfirmationDialog(action: String) {
        val message =
            if (action == "delete") "¿Seguro que deseas eliminar el contacto?" else "¿Seguro que deseas actualizar el contacto?"

        AlertDialog.Builder(this).apply {
            setTitle("Confirmación")
            setMessage(message)
            setPositiveButton("Sí") { _, _ ->
                when (action) {
                    "delete" -> performDeleteContact()
                    "update" -> performUpdateContact()
                }
            }
            setNegativeButton("No", null)
        }.show()
    }

    private fun performUpdateContact() {
        val contact = Contact(
            id = txtId.text.toString(),
            name = txtName.text.toString(),
            lastName = txtLastName.text.toString(),
            phone = txtPhone.text.toString().toInt(),
            email = txtEmail.text.toString(),
            address = txtAddress.text.toString()
        )
        contactModel.updateContact(contact)
        Toast.makeText(this, R.string.msgUpdate, Toast.LENGTH_LONG).show()
        util.openActivity(this, ContactListActivity::class.java)
    }

    private fun performDeleteContact() {
        contactModel.remContact(txtId.text.toString())
        Toast.makeText(this, R.string.msgDelete, Toast.LENGTH_LONG).show()
        cleanContact()
        util.openActivity(this, ContactListActivity::class.java)
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
    }
}
