package cr.ac.utn.appmovil.contactmanager

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

class ContactActivity : AppCompatActivity() {

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    var isEdit: Boolean = false
    var contactIdEdit: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        txtName = findViewById<EditText>(R.id.txtContactName)
        txtLastName = findViewById<EditText>(R.id.txtContactLastName)
        txtPhone = findViewById<EditText>(R.id.txtContactPhone)
        txtEmail = findViewById<EditText>(R.id.txtContactEmail)
        txtAddress = findViewById<EditText>(R.id.txtContactAddress)

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") isEdit = loadEditContact(contactId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete)?.setVisible(isEdit)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnuSave -> {
                saveContact()
                true
            }
            R.id.mnuDelete -> {
                deleteContact()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveContact(){
        try {
            val contact = Contact()
            contact.Name = txtName.text.toString()
            contact.LastName = txtLastName.text.toString()
            contact.Phone = txtPhone.text.toString()?.toInt()
            contact.Email = txtEmail.text.toString()
            contact.Address = txtAddress.text.toString()

            if (dataValidation(contact)){
                if (!isEdit)
                    ContactModel.addContact(contact)
                else
                    ContactModel.updateContact(contactIdEdit, contact)

                cleanScreen()
                Toast.makeText(this, getString(R.string.msgSave).toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun deleteContact(){
        //if (dataValidation()){

            Toast.makeText(this, getString(R.string.msgDelete).toString(),Toast.LENGTH_LONG).show()
        //}else{
          //  Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
        //}
    }

    fun dataValidation(contact: Contact): Boolean{
        return contact.Name.length > 0 && contact.LastName.length > 0 && contact.Address.length > 0 && contact.Email.length > 0 && contact.Phone > 0
    }

    fun cleanScreen(){
        contactIdEdit = ""
        isEdit=false
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
    }

    fun loadEditContact(id: String): Boolean{
        try{
            val contact = ContactModel.getContact(id)
            contactIdEdit= contact.FullName.trim()
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
            return true
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
        return false
    }

    fun confirmDelete(){
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(getString(R.string.ConfirmDelete).toString())
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Ok), DialogInterface.OnClickListener {
                    dialog, id ->

                ContactModel.deleteContact(contactIdEdit)
                cleanScreen()
                Toast.makeText(this, getString(R.string.msgDelete).toString(), Toast.LENGTH_LONG).show()

            })
            .setNegativeButton(getString(R.string.Cancel), DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.TitleDialogQuestion).toString())
        alert.show()
    }

}