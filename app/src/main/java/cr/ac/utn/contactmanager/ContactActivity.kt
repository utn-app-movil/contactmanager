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

        // Inicializamos los EditText para su uso
        txtId = findViewById(R.id.txtContact_Id)
        txtName = findViewById(R.id.txtContact_Name)
        txtLastName = findViewById(R.id.txtContact_LastName)
        txtPhone = findViewById(R.id.txtContact_Phone)
        txtEmail = findViewById(R.id.txtContact_Email)
        txtAddress = findViewById(R.id.txtContact_Address)

        // Comprobación para ver si estamos en modo edición o creación
        val contactInfo = intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)
        if (!contactInfo.isNullOrEmpty()) {
            isEditionMode = true
            loadContact(contactInfo.toString()) // Solo llama a loadContact en modo edición
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_menu, menu)

        menuitemDelete = menu!!.findItem(R.id.mnu_delete)
        menuitemDelete.isVisible = isEditionMode // Solo muestra la opción eliminar en modo edición
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveContact()
                true
            }

            R.id.mnu_delete -> {
                showDeleteDialog() // Diálogo de confirmación antes de eliminar
                true
            }

            R.id.mnu_cancel -> {
                cleanScreen()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // Método para guardar contacto
    private fun saveContact() {
        try {
            val contact = Contact()
            contact.id = txtId.text.toString()
            contact.name = txtName.text.toString()
            contact.lastName = txtLastName.text.toString()
            contact.phone = txtPhone.text.toString().toIntOrNull() ?: -1 // Validación de número
            contact.email = txtEmail.text.toString()
            contact.address = txtAddress.text.toString()

            // Validaciones antes de guardar el contacto
            if (dataValidation(contact)) {
                // Verificación de duplicados antes de crear el contacto
                if (!isEditionMode && contactModel.getContactByFullName(contact.fullName) != null) {
                    showWarning("El contacto ya existe")
                    return
                }

                // Guardado o actualización de contacto
                if (!isEditionMode) {
                    contactModel.addContact(contact)
                } else {
                    contactModel.updateContact(contact)
                }
                cleanScreen()
                showWarning("Contacto guardado correctamente")
            } else {
                showWarning("Todos los campos son requeridos")
            }

        } catch (e: Exception) {
            showWarning("Error: ${e.message}")
        }
    }

    // Validación de datos ingresados
    private fun dataValidation(contact: Contact): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+" // Expresión regular para email
        val costaRicaPhonePattern = "^[2687]\\d{7}$" // Números Nacionales (8 dígitos, empieza con 2, 6, 7, o 8)
        val internationalPhonePattern = "^\\+\\d{1,3}\\d{4,14}$" // Número internacional con prefijo y de 4 a 14 dígitos

        return when {
            contact.id.isEmpty() -> {
                showWarning("El campo ID es requerido")
                false
            }
            contact.name.isEmpty() -> {
                showWarning("El campo Nombre es requerido")
                false
            }
            contact.lastName.isEmpty() -> {
                showWarning("El campo Apellido es requerido")
                false
            }
            !(contact.phone.toString().matches(costaRicaPhonePattern.toRegex()) ||
                    contact.phone.toString().matches(internationalPhonePattern.toRegex())) -> {
                showWarning("Número de teléfono inválido. Debe tener 8 dígitos para CR o incluir prefijo internacional (+)")
                false
            }
            !contact.email.matches(emailPattern.toRegex()) -> {
                showWarning("Formato de correo inválido")
                false
            }
            contact.address.isEmpty() -> {
                showWarning("El campo Dirección es requerido")
                false
            }
            else -> true
        }
    }

    // Método que muestra un diálogo de confirmación antes de eliminar
    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage("Desea eliminar el contacto?")
            .setCancelable(false)
            .setPositiveButton("Si") { _, _ -> deleteContact() }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .setTitle("Confirmación")
            .show()
    }

    // Método para eliminar un contacto
    private fun deleteContact() {
        showWarning("Contacto eliminado")
    }

    // Método para Limpiar la pantalla y restablece los campos
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

    // Método que carga los datos del contacto en modo edición
    private fun loadContact(contactInfo: String) {
        try {
            val contact = contactModel.getContactByFullName(contactInfo)
            if (contact != null) { // Verifica si el contacto fue encontrado
                txtId.setText(contact.id)
                txtName.setText(contact.name)
                txtLastName.setText(contact.lastName)
                txtPhone.setText(contact.phone.toString())
                txtEmail.setText(contact.email)
                txtAddress.setText(contact.address)
                isEditionMode = true
                txtId.isEnabled = false
            } else {
                showWarning("El contacto no fue encontrado.")
            }
        } catch (e: Exception) {
            showWarning("Error: ${e.message}")
        }
    }

    // Método auxiliar para mostrar mensajes de advertencia
    private fun showWarning(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
