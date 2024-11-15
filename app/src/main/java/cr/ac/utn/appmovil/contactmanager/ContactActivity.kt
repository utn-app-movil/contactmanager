package cr.ac.utn.appmovil.contactmanager

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import java.io.File
import java.lang.Exception

private const val FILE_NAME = "photo.jpg"
private const val PROVIDER = "cr.ac.utn.appmovil.contactmanager.fileprovider"

class ContactActivity : AppCompatActivity() {

    private lateinit var txtId: EditText
    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    private val takePicture = 100
    private val selectImage = 101    
    lateinit var imgPhoto: ImageView
    lateinit var filePhoto: File
    lateinit var spCountries: Spinner
    lateinit var countries: List<String>
    lateinit var contactMod: ContactModel
    private var isEditionMode: Boolean = false
    private lateinit var menuitemDelete: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        txtId = findViewById<EditText>(R.id.txtContact_Id)
        txtName = findViewById<EditText>(R.id.txtContactName)
        txtLastName = findViewById<EditText>(R.id.txtContactLastName)
        txtPhone = findViewById<EditText>(R.id.txtContactPhone)
        txtEmail = findViewById<EditText>(R.id.txtContactEmail)
        txtAddress = findViewById<EditText>(R.id.txtContactAddress)
        imgPhoto = findViewById(R.id.imgPhoto_Contact)
        spCountries = findViewById<Spinner>(R.id.spCountries_contact)

        contactMod = ContactModel(this)
        loadCountries()

        spCountries.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                Toast.makeText(this@ContactActivity, countries[position].toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val btnTakePhoto: Button = findViewById<Button>(R.id.btnTakePicture)
        btnTakePhoto.setOnClickListener(View.OnClickListener { view ->
            TakePhoto()
        })

        val btnSelectPhoto: Button = findViewById<Button>(R.id.btnSelectPhoto)
        btnSelectPhoto.setOnClickListener(View.OnClickListener { view ->
           selectPhoto()
        })

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") isEditionMode = loadEditContact(contactId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete)?.setVisible(isEditionMode)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnuSave -> {
                saveContact()
                true
            }
            R.id.mnuDelete -> {
                confirmDelete()
                true
            }
            R.id.mnuCancel -> {
                cleanScreen()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveContact(){
        try {
            val contact = Contact()
            contact.Id = txtId.text.toString()
            contact.Name = txtName.text.toString()
            contact.LastName = txtLastName.text.toString()
            contact.Phone = txtPhone.text.toString()?.toInt()!!
            contact.Email = txtEmail.text.toString()
            contact.Address = txtAddress.text.toString()
            contact.Photo = (imgPhoto?.drawable as BitmapDrawable).bitmap
            contact.Country = spCountries.selectedItem.toString()

            if (dataValidation(contact)){
                if (!isEditionMode)
                    contactMod.addContact(contact)
                else
                    contactMod.updateContact(contact)

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
        return contact.Id.isNotEmpty() && contact.Name.isNotEmpty() &&
                contact.LastName.isNotEmpty() && contact.Address.isNotEmpty() &&
                contact.Email.isNotEmpty() &&
                (contact.Phone != null && contact.Phone > 0)
    }

    fun cleanScreen(){
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

    fun loadEditContact(id: String): Boolean{
        try{
            val contact = contactMod.getContact(id)
            txtId.setText(contact.Id)
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
            spCountries.setSelection(countries.indexOf(contact.Country.trim()))
            imgPhoto.setImageBitmap(contact.Photo)
            isEditionMode = true
            txtId.isEnabled = false

            return true
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
        return false
    }

    fun confirmDelete(){
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setMessage(getString(R.string.ConfirmDelete).toString())
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Ok), DialogInterface.OnClickListener {
                    dialog, id ->

                contactMod.removeContact(txtId.text.toString())
                cleanScreen()
                Toast.makeText(this, getString(R.string.msgDelete).toString(), Toast.LENGTH_LONG).show()

            })
            .setNegativeButton(getString(R.string.Cancel), DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle(getString(R.string.TitleDialogQuestion).toString())
        alert.show()
    }

    fun selectPhoto(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, selectImage)
    }

    fun TakePhoto(){
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        filePhoto = getPhotoFile(FILE_NAME)
        val providerFile = FileProvider.getUriForFile(this,PROVIDER, filePhoto)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        startActivityForResult(takePhotoIntent, takePicture)
    }

    private fun getPhotoFile(fileName: String): File{
        val directoryStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", directoryStorage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == selectImage){            
            val imageUri = data?.data
            imgPhoto.setImageURI(imageUri)
        }
        else if (resultCode == RESULT_OK && requestCode == takePicture){
            val takenPhoto = BitmapFactory.decodeFile(filePhoto.absolutePath)
            imgPhoto.setImageBitmap(takenPhoto)
        }
    }

    fun loadCountries(){
        countries = resources.getStringArray(R.array.Countries).toList()
        if (spCountries != null) {
            val adapter = ArrayAdapter.createFromResource(this, R.array.Countries, android.R.layout.simple_spinner_item)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spCountries.adapter = adapter
        }
    }
}