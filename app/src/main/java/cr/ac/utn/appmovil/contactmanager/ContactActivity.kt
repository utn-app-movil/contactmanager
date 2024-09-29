package cr.ac.utn.appmovil.contactmanager

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
import java.lang.Exception

class ContactActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

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

    private fun saveContact(){
        try {
            val contact = Contact()
            contact.name = txtName.text.toString()
            contact.lastName = txtLastName.text.toString()
            contact.phone = txtPhone.text.toString()?.toInt()!!
            contact.email = txtEmail.text.toString()
            contact.address = txtAddress.text.toString()

            if (dataValidation(contact)){
                ContactModel.addContact(contact)
                cleanScreen()
                Toast.makeText(this, getString(R.string.msgSave),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, getString(R.string.msgInvalidData),Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteContact(){
        //if (dataValidation()){

            Toast.makeText(this, getString(R.string.msgDelete),Toast.LENGTH_LONG).show()
        //}else{
          //  Toast.makeText(this, getString(R.string.msgInvalidData),Toast.LENGTH_LONG).show()
        //}
    }

    private fun dataValidation(contact: Contact): Boolean{
        return contact.name.isNotEmpty() && contact.lastName.isNotEmpty() && contact.address.isNotEmpty() && contact.email.isNotEmpty() && contact.phone > 0
    }

    private fun cleanScreen(){
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
    }

    private fun loadEditContact(id: String){
        try{
            val contact = ContactModel.getContact(id)
            txtName.setText(contact.name)
            txtLastName.setText(contact.lastName)
            txtPhone.setText(contact.phone.toString())
            txtEmail.setText(contact.email)
            txtAddress.setText(contact.address)
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
        //val btnDelete = findViewById<MenuItem>(R.id.mnuDelete)
        //btnDelete.setVisible(false)
    }
}