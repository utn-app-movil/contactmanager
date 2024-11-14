package cr.ac.utn.appmovil.contactmanager

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import java.lang.Exception

class ContactActivity : AppCompatActivity() {

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    lateinit var contactModel: ContactModel
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        contactModel= ContactModel(this)
        txtName = findViewById<EditText>(R.id.txtContactName)
        txtLastName = findViewById<EditText>(R.id.txtContactLastName)
        txtPhone = findViewById<EditText>(R.id.txtContactPhone)
        txtEmail = findViewById<EditText>(R.id.txtContactEmail)
        txtAddress = findViewById<EditText>(R.id.txtContactAddress)

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") loadEditContact(contactId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        menuitemDelete = menu!!.findItem(R.id.mnuDelete)
        if (isEditionMode)
            menuitemDelete.isVisible = true
        else
            menuitemDelete.isVisible =false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnuSave -> {
                saveContact()
                true
            }
            R.id.mnuDelete -> {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Desea eliminar el contacto?")
                    .setCancelable(false)
                    .setPositiveButton("Si", DialogInterface.OnClickListener{
                            dialog, id -> deleteContact()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })

                val alert = dialogBuilder.create()
                alert.setTitle("Titulo del dialog")
                alert.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveContact(){
        try {
            val contact = Contact()
            contact.Name = txtName.text.toString()
            contact.LastName = txtLastName.text.toString()
            contact.Phone = txtPhone.text.toString()?.toInt()!!
            contact.Email = txtEmail.text.toString()
            contact.Address = txtAddress.text.toString()

            if (dataValidation(contact)){
                if (!isEditionMode)
                    contactModel.addContact(contact)
                else
                    contactModel.updateContact(contact)
                cleanScreen()
                Toast.makeText(this, getString(R.string.msgSave).toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun dataValidation(contact: Contact): Boolean{
        return contact.Name.isNotEmpty() &&
                contact.LastName.isNotEmpty() && contact.Address.isNotEmpty() &&
                contact.Email.isNotEmpty() &&
                (contact.Phone != null && contact.Phone > 0)
    }

    private fun deleteContact(){

    }

    fun cleanScreen(){
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        isEditionMode = false
        invalidateOptionsMenu()
    }

    fun loadEditContact(id: String){
        try{
            val contact = contactModel.getContactbyName(id)
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
            isEditionMode = true
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }
}