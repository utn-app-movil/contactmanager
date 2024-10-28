package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Message
import android.security.keystore.StrongBoxUnavailableException
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.R
import java.util.regex.Pattern

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
    private var contactInfo: String? = ""

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
                    getString(R.string.msg_sureDeleteContact),
                    getString(R.string.msg_sureDeleteContact), showNegativeButton = true, showPositiveButton = true,
                    getString(R.string.msg_yes), getString(R.string.msg_no) , onPositiveClick = { deleteContact(contactInfo.toString()) })
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
                if (!isEditionMode) {
                    contactModel.addContact(contact)
                    cleanScreen()
                }else {
                    DisplayDialog(
                        getString(R.string.msg_updateSure),
                        getString(R.string.msg_updateSure),
                        showNegativeButton = true,
                        showPositiveButton = true,
                        getString(R.string.msg_yes),
                        getString(R.string.msg_no),
                        onPositiveClick = {
                            contactModel.updateContact(contact)
                            cleanScreen()
                        })
                }
                Toast.makeText(this,R.string.msgSave, Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, R.string.msgMissingData, Toast.LENGTH_LONG).show()

        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun DisplayDialog(mesage: String = "",
                              title: String = "",
                              showNegativeButton: Boolean = true,
                              showPositiveButton: Boolean = true,
                              positiveText: String = "",
                              negativeText: String = "",
                              onPositiveClick: (() -> Unit)? = null){

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

    private fun dataValidation(contact: Contact): Boolean{
        val phoneRegex = Regex("^\\d{1,8}$")
        val emailRegex = Regex("^[A-Za-z0-9+_.-]{1,40}@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")


        val contactId = contactModel.getContact(contact.id)
        if (!isEditionMode) {
            if ( contactId?.id == contact.id ) {
                DisplayDialog(
                    getString(R.string.msg_conctactAlreadyExist),
                    getString(R.string.msg_conctactAlreadyExist), showNegativeButton = true, showPositiveButton = false,
                    "", getString(R.string.msgOK) ,null)
                return false
            }
        }


        if (!phoneRegex.matches(contact.phone.toString())) {
            DisplayDialog(
                getString(R.string.msg_phoneRegex),
                getString(R.string.msg_phoneRegex), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgOK) ,null)
            return false
        }

        if (!emailRegex.matches(contact.email)){
            DisplayDialog(
                getString(R.string.msg_email_regex),
                getString(R.string.msg_email_regex), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgOK) ,null)
            return false
        }

        if (contact.id.length > 9 || contact.name.length > 30 ||
            contact.lastName.length > 30 ||
            contact.address.length > 50) {
            DisplayDialog(
                getString(R.string.msg_longText),
                getString(R.string.msg_longText), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgOK) ,null)
            return false
        }

        if (contact.id.isEmpty() && contact.name.isEmpty() &&
            contact.lastName.isEmpty() && contact.address.isEmpty() &&
            contact.email.isEmpty() &&
            !(contact.phone != null && contact.phone > 0)) {
            DisplayDialog(
                getString(R.string.msgMissingData),
                getString(R.string.msgMissingData), showNegativeButton = true, showPositiveButton = false,
                "", getString(R.string.msgOK) ,null)
            return false
        }


        return true
    }

    private fun deleteContact(contactInfo: String){
        val contact = contactModel.getContactByFullName(contactInfo)
        contactModel.removeContact(contact.id)
        cleanScreen()
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