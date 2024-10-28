package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.view.Menu
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

        setInputFilters()

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (!contactInfo.isNullOrEmpty()) {
            loadContact(contactInfo)
        }
    }

    private fun setInputFilters() {
        txtId.filters = arrayOf(InputFilter.LengthFilter(10))
        txtName.filters = arrayOf(InputFilter.LengthFilter(50))
        txtLastName.filters = arrayOf(InputFilter.LengthFilter(50))
        txtPhone.filters = arrayOf(InputFilter.LengthFilter(10))
        txtEmail.filters = arrayOf(InputFilter.LengthFilter(100))
        txtAddress.filters = arrayOf(InputFilter.LengthFilter(100))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.crud_menu, menu)
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
                showConfirmationDialog("¿Desea eliminar el contacto?") { deleteContact() }
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
            val contact = createContactFromInputs()

            if (!validateAllFields(contact)) return

            if (!isEditionMode && contactModel.isDuplicateContact(contact)) {
                showWarning("El contacto ya existe con el nombre: ${contact.fullName}.")
                return
            }

            showConfirmationDialog("¿Desea guardar los cambios?") {
                if (!isEditionMode) {
                    contactModel.addContact(contact)
                } else {
                    contactModel.updateContact(contact)
                }
                cleanScreen()
                showWarning("La información se ha guardado correctamente.")
            }
        } catch (e: Exception) {
            showWarning("Error al guardar el contacto: ${e.message}")
        }
    }

    private fun createContactFromInputs(): Contact {
        return Contact(
            id = txtId.text.toString(),
            name = txtName.text.toString(),
            lastName = txtLastName.text.toString(),
            phone = txtPhone.text.toString().toIntOrNull() ?: -1,
            email = txtEmail.text.toString(),
            address = txtAddress.text.toString()
        )
    }

    private fun validateAllFields(contact: Contact): Boolean {
        return when {
            contact.id.isBlank() -> {
                showWarning("El campo ID no puede estar vacío.")
                false
            }
            contact.name.isBlank() -> {
                showWarning("El campo Nombre es obligatorio.")
                false
            }
            contact.lastName.isBlank() -> {
                showWarning("El campo Apellido es obligatorio.")
                false
            }
            contact.phone <= 0 || contact.phone.toString().length != 10 -> {
                showWarning("El número de teléfono debe tener 10 dígitos y ser positivo.")
                false
            }
            !isValidEmail(contact.email) -> {
                showWarning("El correo electrónico tiene un formato inválido.")
                false
            }
            contact.address.isBlank() -> {
                showWarning("El campo Dirección es obligatorio.")
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showWarning(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showConfirmationDialog(message: String, action: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ -> action() }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun deleteContact() {
        contactModel.removeContact(txtId.text.toString())
        cleanScreen()
        showWarning("El contacto ha sido eliminado.")
    }

    private fun cleanScreen() {
        txtId.text.clear()
        txtName.text.clear()
        txtLastName.text.clear()
        txtPhone.text.clear()
        txtEmail.text.clear()
        txtAddress.text.clear()
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
            showWarning("Error al cargar el contacto: ${e.message}")
        }
    }
}
