package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
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


        txtId = findViewById(R.id.txtContact_Id)
        txtName = findViewById(R.id.txtContact_Name)
        txtLastName = findViewById(R.id.txtContact_LastName)
        txtPhone = findViewById(R.id.txtContact_Phone)
        txtEmail = findViewById(R.id.txtContact_Email)
        txtAddress = findViewById(R.id.txtContact_Address)


        txtId.filters = arrayOf(LengthFilter(10))
        txtName.filters = arrayOf(LengthFilter(50))
        txtLastName.filters = arrayOf(LengthFilter(50))
        txtPhone.filters = arrayOf(LengthFilter(10))
        txtEmail.filters = arrayOf(LengthFilter(100))
        txtAddress.filters = arrayOf(LengthFilter(200))

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (contactInfo != null && contactInfo.isNotEmpty()) loadContact(contactInfo)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.mnu_delete)
        menuitemDelete.isVisible = isEditionMode
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveContact()
                true
            }
            R.id.mnu_delete -> {
                confirmDelete()
                true
            }
            R.id.mnu_cancel -> {
                cleanScreen()
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
                phone = txtPhone.text.toString().toIntOrNull() ?: 0,
                email = txtEmail.text.toString(),
                address = txtAddress.text.toString()
            )

            if (dataValidation(contact)) {
                if (isEditionMode) {
                    confirmUpdate(contact)
                } else {
                    contactModel.addContact(contact)
                    showToast("Contacto guardado")
                }
                cleanScreen()
            }
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
        }
    }

    private fun dataValidation(contact: Contact): Boolean {
        return when {
            contact.id.isEmpty() -> {
                showToast("ID es requerido")
                false
            }
            contact.name.isEmpty() -> {
                showToast("Nombre es requerido")
                false
            }
            contact.lastName.isEmpty() -> {
                showToast("Apellido es requerido")
                false
            }
            !isPhoneValid(contact.phone.toString()) -> {
                showToast("Teléfono debe tener entre 8 y 10 dígitos")
                false
            }
            !isEmailValid(contact.email) -> {
                showToast("Formato de correo no válido")
                false
            }
            contactModel.contactIdExists(contact.id) && !isEditionMode -> {
                showToast("El ID ya está en uso")
                false
            }
            contactModel.contactPhoneExists(contact.phone) && !isEditionMode -> {
                showToast("El número de teléfono ya está en uso")
                false
            }
            contactModel.contactExists(contact) && !isEditionMode -> {
                showToast("El nombre completo ya está en uso")
                false
            }
            else -> true
        }
    }


    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.length in 8..10
    }

    private fun confirmUpdate(contact: Contact) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("¿Desea actualizar el contacto?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                contactModel.updateContact(contact)
                showToast("Contacto actualizado")
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmación de actualización")
        alert.show()
    }

    private fun confirmDelete() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("¿Desea eliminar el contacto?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                deleteContact()
                showToast("Contacto eliminado")
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmación de eliminación")
        alert.show()
    }

    private fun deleteContact() {
        contactModel.removeContact(txtId.text.toString())
        cleanScreen()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun cleanScreen() {
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

    private fun loadContact(contactInfo: String) {
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
        } catch (e: Exception) {
            showToast("Error: ${e.message}")
        }
    }
}
