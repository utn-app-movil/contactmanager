package cr.ac.utn.contactmanager

import Entities.Contact
import Model.ContactModel
import Util.EXTRA_MESSAGE_CONTACT_ID
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Patterns
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.content.FileProvider
import kotlinx.coroutines.NonCancellable.cancel
import java.io.File
import Util.util.Companion.convertToByteArray

private const val GALLERY_CONTACT = 101
private const val CAPTUREPHOTO_CONTACT = 100
private const val PROVIDER = "cr.ac.utn.contactmanager.fileprovider"

class ContactActivity : AppCompatActivity() {

    private lateinit var spCountry: Spinner
    private lateinit var txtId: EditText
    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtPhone: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtAddress: EditText
    private lateinit var imgPhoto: ImageView
    private lateinit var btnCamera: Button
    private lateinit var btnGallery: Button
    private lateinit var contactModel: ContactModel
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem
    private lateinit var filePhoto: File

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

        // Inicialización de vistas
        spCountry = findViewById(R.id.spCountry_Contact)
        txtId = findViewById(R.id.txtContact_Id)
        txtName = findViewById(R.id.txtContact_Name)
        txtLastName = findViewById(R.id.txtContact_LastName)
        txtPhone = findViewById(R.id.txtContact_Phone)
        txtEmail = findViewById(R.id.txtContact_Email)
        txtAddress = findViewById(R.id.txtContact_Address)
        imgPhoto = findViewById(R.id.imgPhoto_Contact)
        btnCamera = findViewById(R.id.btnCamera_Contact)
        btnGallery = findViewById(R.id.btnGallery_Contact)

        // Configurar listeners
        btnCamera.setOnClickListener { capturePhoto() }
        btnGallery.setOnClickListener { selectGallery() }

        loadCountries()

        // Cargar información del contacto si existe
        intent.getStringExtra(EXTRA_MESSAGE_CONTACT_ID)?.let { contactInfo ->
            if (contactInfo.isNotEmpty()) loadContact(contactInfo)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.crud_menu, menu)
        menuitemDelete = menu!!.findItem(R.id.mnu_delete).apply {
            isVisible = isEditionMode
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_save -> {
                saveContact()
                true
            }
            R.id.mnu_delete -> {
                // Mostrar diálogo de confirmación antes de eliminar
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnu_delete)?.isVisible = isEditionMode
        return true
    }

    private fun saveContact() {
        try {
            val contact = Contact().apply {
                id = txtId.text.toString()
                name = txtName.text.toString()
                lastName = txtLastName.text.toString()
                phone = txtPhone.text.toString().toInt()
                email = txtEmail.text.toString()
                address = txtAddress.text.toString()
                Country = spCountry.selectedItem.toString()
                Photo = convertToByteArray((imgPhoto.drawable as BitmapDrawable).bitmap)
            }

            // Validar datos antes de proceder
            if (dataValidation(contact)) {
                if (!isEditionMode) {
                    // Solo validar duplicados al agregar un nuevo contacto
                    if (contactModel.isContactDuplicate(contact)) {
                        showToast("El contacto ya existe")
                    } else {
                        // Agregar nuevo contacto
                        contactModel.addContact(contact)
                        cleanScreen()
                        showToast(getString(R.string.msgSave))
                    }
                } else {
                    // Modo edición: mostrar diálogo de confirmación antes de actualizar
                    showConfirmationDialog("¿Desea actualizar el contacto?") {
                        contactModel.updateContact(contact)
                        cleanScreen()
                        showToast(getString(R.string.msgSave))
                    }
                }
            } else {
                showToast(getString(R.string.msgMissingData))
            }
        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun dataValidation(contact: Contact): Boolean {
        return when {
            contact.id.isEmpty() -> {
                showToast("El ID es requerido.")
                false
            }
            contact.name.isEmpty() -> {
                showToast("El nombre es requerido.")
                false
            }
            contact.lastName.isEmpty() -> {
                showToast("El apellido es requerido.")
                false
            }
            contact.phone <= 0 -> {
                showToast("El teléfono es requerido y debe ser un número válido.")
                false
            }
            contact.email.isEmpty() || !isEmailValid(contact.email) -> {
                showToast("El correo electrónico es requerido y debe ser válido.")
                false
            }
            contact.address.isEmpty() -> {
                showToast("La dirección es requerida.")
                false
            }
            spCountry.selectedItem.toString().trim().isEmpty() -> {
                showToast("La ciudad es requerida.")
                false
            }
            else -> true // Todos los datos son válidos
        }
    }

    // Método auxiliar para mostrar mensajes de advertencia
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteContact() {
        try {
            if (isEditionMode && txtId.text.toString().trim().isNotEmpty()) {
                contactModel.removeContact(txtId.text.toString().trim())
                showToast(getString(R.string.DeleteSuccess))
            } else {
                showToast(getString(R.string.MissingContactData))
            }
        } catch (ex: Exception) {
            showToast(ex.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                GALLERY_CONTACT -> {
                    val imageUri = data?.data
                    imgPhoto.setImageURI(imageUri)
                }
                CAPTUREPHOTO_CONTACT -> {
                    val photo = BitmapFactory.decodeFile(filePhoto.absolutePath)
                    imgPhoto.setImageBitmap(photo)
                }
            }
        }
    }

    private fun loadCountries() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Countries,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCountry.adapter = adapter
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

    private fun loadContact(contactInfo: String): Boolean {
        return try {
            val contact = contactModel.getContactByFullName(contactInfo)
            txtId.setText(contact.id)
            txtName.setText(contact.name)
            txtLastName.setText(contact.lastName)
            txtPhone.setText(contact.phone.toString())
            txtEmail.setText(contact.email)
            txtAddress.setText(contact.address)
            isEditionMode = true
            txtId.isEnabled = false
            val countries = resources.getStringArray(R.array.Countries).toList()
            spCountry.setSelection(countries.indexOf(contact.Country))
            val photo = BitmapFactory.decodeByteArray(contact.Photo, 0, contact.Photo!!.size)
            imgPhoto.setImageBitmap(photo)
            true
        } catch (e: Exception) {
            showToast(e.message.toString())
            false
        }
    }

    private fun selectGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_CONTACT)
    }

    private fun capturePhoto() {
        val filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
        filePhoto = File("$filePath/${System.currentTimeMillis()}.jpg")

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this@ContactActivity, PROVIDER, filePhoto))
        }
        startActivityForResult(intent, CAPTUREPHOTO_CONTACT)
    }

    private fun showConfirmationDialog(message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Confirmación")
            .setMessage(message)
            .setPositiveButton("Sí") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }
}
