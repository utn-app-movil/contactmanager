package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
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

        txtId = findViewById(R.id.txtContact_Id)
        txtName = findViewById(R.id.txtContact_Name)
        txtLastName = findViewById(R.id.txtContact_LastName)
        txtPhone = findViewById(R.id.txtContact_Phone)
        txtEmail = findViewById(R.id.txtContact_Email)
        txtAddress = findViewById(R.id.txtContact_Address)

        txtId.filters = arrayOf(InputFilter.LengthFilter(10))
        txtName.filters = arrayOf(InputFilter.LengthFilter(50))
        txtLastName.filters = arrayOf(InputFilter.LengthFilter(50))
        txtPhone.filters = arrayOf(InputFilter.LengthFilter(10))
        txtEmail.filters = arrayOf(InputFilter.LengthFilter(100))
        txtAddress.filters = arrayOf(InputFilter.LengthFilter(100))

        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (contactInfo != null && contactInfo != "") loadContact(contactInfo.toString())
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
            val contact = Contact(
                id = txtId.text.toString(),
                name = txtName.text.toString(),
                lastName = txtLastName.text.toString(),
                phone = txtPhone.text.toString().toIntOrNull() ?: -1,
                email = txtEmail.text.toString(),
                address = txtAddress.text.toString()
            )

            if (dataValidation(contact)) {
                if (!isEditionMode && contactModel.isDuplicateContact(contact)) {
                    Toast.makeText(this, "El contacto ya existe con el nombre: ${contact.fullName}.", Toast.LENGTH_LONG).show()
                    return
                }

                showConfirmationDialog("¿Desea guardar los cambios?") {
                    if (!isEditionMode) {
                        contactModel.addContact(contact)
                    } else {
                        contactModel.updateContact(contact)
                    }
                    cleanScreen()
                    Toast.makeText(this, "La información se ha guardado correctamente.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error al guardar el contacto: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun dataValidation(contact: Contact): Boolean {
        if (contact.id.isEmpty()) {
            Toast.makeText(this, "El campo ID no puede estar vacío.", Toast.LENGTH_LONG).show()
            return false
        }
        if (contact.name.isEmpty()) {
            Toast.makeText(this, "El campo Nombre es obligatorio.", Toast.LENGTH_LONG).show()
            return false
        }
        if (contact.lastName.isEmpty()) {
            Toast.makeText(this, "El campo Apellido es obligatorio.", Toast.LENGTH_LONG).show()
            return false
        }
        if (contact.phone <= 0) {
            Toast.makeText(this, "El número de teléfono debe ser un valor positivo.", Toast.LENGTH_LONG).show()
            return false
        }
        if (contact.phone.toString().length != 10) {
            Toast.makeText(this, "El número de teléfono debe tener 10 dígitos.", Toast.LENGTH_LONG).show()
            return false
        }
        if (!isValidEmail(contact.email)) {
            Toast.makeText(this, "El correo electrónico tiene un formato inválido.", Toast.LENGTH_LONG).show()
            return false
        }
        if (contact.address.isEmpty()) {
            Toast.makeText(this, "El campo Dirección es obligatorio.", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showConfirmationDialog(message: String, action: () -> Unit) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ -> action() }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmación")
        alert.show()
    }

    private fun deleteContact() {
        contactModel.removeContact(txtId.text.toString())
        cleanScreen()
        Toast.makeText(this, "El contacto ha sido eliminado.", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "Error al cargar el contacto: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
