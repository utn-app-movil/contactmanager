package cr.ac.utn.contactmanager

import Entity.Contact
import Model.ContactModel
import Utilities.EXTRA_MESSAGE_CONTACTID
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

class ContactActivity : AppCompatActivity() {
    lateinit var spCountry: Spinner
    lateinit var txtId: EditText
    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    var isEdit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        spCountry = findViewById<Spinner>(R.id.spCountry_Contact)
        txtId = findViewById<EditText>(R.id.txtId_Contact)
        txtName = findViewById<EditText>(R.id.txtName_Contact)
        txtLastName = findViewById<EditText>(R.id.txtLastName_Contact)
        txtPhone = findViewById<EditText>(R.id.txtPhone_Contact)
        txtEmail = findViewById<EditText>(R.id.txtEmail_Contact)
        txtAddress = findViewById<EditText>(R.id.txtAddress_Contact)

        loadCountries()

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") isEdit = loadEditContact(contactId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnuSave_Contact -> {
                //if (!isEdit)
                   saveContact()
                //else
                  //  confirmUpdate()
                true
            }
            R.id.mnuDelete_Contact -> {
                confirmDelete()
                true
            }
            R.id.mnuCancel_Contact -> {
                cancel()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete_Contact)?.setVisible(isEdit)
        return true
    }

    fun loadCountries(){
        if (spCountry != null){
            val adapter = ArrayAdapter.createFromResource(this, R.array.Countries, android.R.layout.simple_spinner_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCountry.adapter=adapter
        }
    }

    fun cancel(){
        isEdit = false
        txtId.isEnabled = true
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        loadCountries()
    }

    fun saveContact(){
        try {
            val contactM= ContactModel(this)
            var contact = Contact()
            if (isValidInformation()){
                contact.Id = txtId.text.toString()
                contact.Name = txtName.text.toString()
                contact.LastName = txtLastName.text.toString()
                contact.Phone = txtPhone.text.toString().toInt()
                contact.Email = txtEmail.text.toString()
                contact.Address = txtAddress.text.toString()
                contact.Country = spCountry.selectedItem.toString()

                if (!isEdit) {
                    contactM.addContact(contact)
                    cancel()
                    Toast.makeText(this,getString(R.string.SaveSuccess).toString(),Toast.LENGTH_LONG).show()
                }else
                    confirmUpdate(contact)

            }else{
                Toast.makeText(this, getString(R.string.MissingContactData).toString(), Toast.LENGTH_LONG).show()
            }
        }catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun isValidInformation(): Boolean{
        return txtId.text.toString().trim().length > 0 &&
                txtName.text.toString().trim().length > 0 &&
                txtLastName.text.toString().trim().length > 0 &&
                txtPhone.text.toString().trim().length > 8 && txtPhone.text.toString().trim().toInt() > 0 &&
                txtEmail.text.toString().trim().length > 0 &&
                txtAddress.text.toString().trim().length > 0 &&
                spCountry.selectedItem.toString().trim().length > 0
    }

    fun deleteContact(){
        try {
            val contactM= ContactModel(this)
            if (isEdit && txtId.text.toString().trim().length > 0){
                contactM.removeContact(txtId.text.toString().trim())
                cancel()
                Toast.makeText(this, getString(R.string.DeleteSuccess).toString(), Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, getString(R.string.MissingContactData).toString(), Toast.LENGTH_LONG).show()
            }
        }catch (ex: Exception){
            Toast.makeText(this, ex.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    fun loadEditContact(id: String): Boolean{
        try{
            val contactModel = ContactModel(this)
            val contact = contactModel.getContact(id)
            txtId.setText(contact.Id)
            txtId.isEnabled=false
            txtName.setText(contact.Name.trim())
            txtLastName.setText(contact.LastName.trim())
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email.trim())
            txtAddress.setText(contact.Address.trim())
            val countries = resources.getStringArray(R.array.Countries).toList()
            spCountry.setSelection(countries.indexOf(contact.Country))
            return true
        }catch (e:Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
        return false
    }

    fun confirmDelete(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.ConfirmDelete).toString())
            .setCancelable(false)
            .setPositiveButton(getString(R.string.OK).toString()
                    , DialogInterface.OnClickListener {
                        dialog, id ->
                        deleteContact()
                })
            .setNegativeButton(getString(R.string.Cancel).toString()
                    , DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.TitleDialogQuestion).toString())
        alert.show()
    }

    fun confirmUpdate(contact: Contact){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(getString(R.string.ConfirmUpdate).toString())
            .setCancelable(false)
            .setPositiveButton(getString(R.string.OK).toString()
                , DialogInterface.OnClickListener {
                        dialog, id ->
                    //saveContact()
                    val contactM = ContactModel(this)
                    contactM.updateContact(contact)
                    cancel()
                    Toast.makeText(this, getString(R.string.SaveSuccess).toString(), Toast.LENGTH_LONG).show()
                })
            .setNegativeButton(getString(R.string.Cancel).toString()
                , DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                })
        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.TitleDialogQuestion).toString())
        alert.show()
    }
}