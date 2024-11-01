package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import Util.util
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
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
    private lateinit var menuitemDelete: MenuItem

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

        txtId = findViewById<EditText>(R.id.txtContact_Id)
        txtName = findViewById<EditText>(R.id.txtContact_Name)
        txtLastName = findViewById<EditText>(R.id.txtContact_LastName)
        txtPhone = findViewById<EditText>(R.id.txtContact_Phone)
        txtEmail = findViewById<EditText>(R.id.txtContact_Email)
        txtAddress = findViewById<EditText>(R.id.txtContact_Address)
        limitsOfText()

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (contactInfo != null && contactInfo != "") loadContact(contactInfo.toString())
    }

    // Resticciones para los txt
    private fun limitsOfText() {
        txtId.filters = arrayOf(InputFilter.LengthFilter(8))
        txtName.filters = arrayOf(InputFilter.LengthFilter(20))
        txtLastName.filters = arrayOf(InputFilter.LengthFilter(25))
        txtPhone.filters = arrayOf(InputFilter.LengthFilter(8))
        txtAddress.filters = arrayOf(InputFilter.LengthFilter(50))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.mnu_delete)
        if (isEditionMode)
            menuitemDelete.isVisible = true
        else
            menuitemDelete.isVisible =false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveContact()
                return true
            }

            R.id.mnu_delete -> {
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

            R.id.mnu_cancel -> {
                cleanScreen()
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveContact(){
        try {
            val contact = Contact()
            contact.id = txtId.text.toString()
            contact.name = txtName.text.toString()
            contact.lastName = txtLastName.text.toString()
            contact.phone = txtPhone.text.toString()?.toInt()!!
            contact.email = txtEmail.text.toString()
            contact.address = txtAddress.text.toString()
            if (dataValidation(contact)){
                if (!isEditionMode)
                    contactModel.addContact(contact)
                else
                    contactModel.updateContact(contact)
                cleanScreen()
                Toast.makeText(this,R.string .msgSave, Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, R.string.msgMissingData, Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean{
        return contact.id.isNotEmpty() && contact.name.isNotEmpty() &&
                contact.lastName.isNotEmpty() && contact.address.isNotEmpty() &&
                contact.email.isNotEmpty() &&
                (contact.phone != null && contact.phone > 0)
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
        contactModel.removeContact(txtId.text.toString())
        Toast.makeText(this, R.string.msgDelete, Toast.LENGTH_LONG).show()
        cleanScreen()
        util.openActivity(this, ContactListActivity::class.java)
    }

    private fun deleteContact(){

    }

    private fun cleanScreen(){
        txtId.setText("")
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
        txtId.isEnabled = true
        isEditionMode = false
        invalidateOptionsMenu()
    }

    private fun loadContact(contactInfo: String){
        try {
            val contact = contactModel.getContactByFullName(contactInfo)
            txtId.setText(contact.id)
            txtName.setText(contact.name)
            txtLastName.setText(contact.lastName)
            txtPhone.setText(contact.phone.toString())
            txtEmail.setText(contact.email)
            txtAddress.setText(contact.address)
            isEditionMode = true
            txtId.isEnabled = false
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }


    }


}