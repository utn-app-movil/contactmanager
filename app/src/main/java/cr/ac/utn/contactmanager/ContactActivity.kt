package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import android.app.AlertDialog
import android.content.DialogInterface
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
    private lateinit var menuitemDelete: MenuItem
    private  var contactInfo: String? =""

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

        contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (contactInfo != null && contactInfo != "") loadContact(contactInfo.toString())
    }

    private fun DisplayDialog(mesage: String = "", title: String = "", showNegativeButton: Boolean = true, showPositiveButton: Boolean = true, positiveText: String = "", negativeText: String = "", onPositiveClick: (() -> Unit)? = null){

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(this)
        dialogBuilder.setMessage(mesage)
            .setCancelable(false)
        if (showPositiveButton) {
            dialogBuilder.setPositiveButton(positiveText) {
                    dialog, id -> onPositiveClick?.invoke()
                dialog.dismiss()
            }
        }

        if (showNegativeButton){
            dialogBuilder.setNegativeButton(negativeText) {
                    dialog, id -> dialog.dismiss()
            }
        }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
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
                DisplayDialog(
                    getString(R.string.msgsure),
                    getString(R.string.msgsure), showNegativeButton = true, showPositiveButton = false,getString(R.string.msgready), getString(R.string.msgNo) , onPositiveClick = {deleteContact(contactInfo.toString())})

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
            if (dataValidation(contact)) {
                if (!isEditionMode) {
                    contactModel.addContact(contact)
                    cleanScreen()
                } else {
                    DisplayDialog(
                        getString(R.string.msgupdate),
                        getString(R.string.msgupdate),
                        showNegativeButton = true,
                        showPositiveButton = true,
                        getString(R.string.msgready),
                        getString(R.string.msgNo),
                        onPositiveClick = {
                            contactModel.updateContact(contact)
                            cleanScreen()
                        })
                    }
                Toast.makeText(this, R.string.msgsaveContact, Toast.LENGTH_SHORT).show()
                }else
                Toast.makeText(this, R.string.msgMissingData, Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean{
        val phoneRegu = Regex("^\\d{1,8}$")
        val emailRegu = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        val contactId = contactModel.getContact(contact.id)
        if (!isEditionMode){
            if ( contactId?.id == contact.id ) {
                DisplayDialog(
                    getString(R.string.msgContactexist),
                    getString(R.string.msgContactexist), showNegativeButton = true, showPositiveButton = false,
                    "", getString(R.string.msgready) ,null)
                return false
            }
        }


        if (!phoneRegu.matches(contact.phone.toString())) {
            DisplayDialog(
                getString(R.string.msgnotnumber),
                getString(R.string.msgnotnumber), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgready) ,null)
            return false
        }

        if (!emailRegu.matches(contact.email)) {
            DisplayDialog(
                getString(R.string.msgnotemail),
                getString(R.string.msgnotemail), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgready) ,null)
            return false
        }




        if (contact.id.length > 9 || contact.name.length > 30 || contact.lastName.length  > 40 || contact.address.length > 50)
        {
            DisplayDialog(
                getString(R.string.msgmuchtext),
                getString(R.string.msgmuchtext), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgready) ,null)
            return false
        }

        if (contact.id.isNotEmpty() && contact.name.isEmpty() && contact.lastName.isEmpty() && contact.address.isEmpty() && contact.email.isEmpty() && !(contact.phone != null && contact.phone > 0))
            {
                DisplayDialog(
                    getString(R.string.msgMissingData),
                    getString(R.string.msgMissingData), showNegativeButton = true, showPositiveButton = false,
                    "", getString(R.string.msgready) ,null)
                return false
            }
            return true
    }

    private fun deleteContact(contactInfo: String){
        val clearscree = contactModel.getContactByFullName(contactInfo)
        contactModel.removeContact(clearscree.id)
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