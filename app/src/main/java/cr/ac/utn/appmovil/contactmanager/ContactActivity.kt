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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
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

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    lateinit var imgPhoto: ImageView
    var isEdit: Boolean = false
    var contactIdEdit: String=""
    private val takePicture = 100
    private val selectImage = 101
    lateinit var filePhoto: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        txtName = findViewById<EditText>(R.id.txtContactName)
        txtLastName = findViewById<EditText>(R.id.txtContactLastName)
        txtPhone = findViewById<EditText>(R.id.txtContactPhone)
        txtEmail = findViewById<EditText>(R.id.txtContactEmail)
        txtAddress = findViewById<EditText>(R.id.txtContactAddress)
        imgPhoto = findViewById(R.id.imgPhoto_Contact)

        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") isEdit = loadEditContact(contactId.toString())

        val btnTakePhoto: Button = findViewById<Button>(R.id.btnTakePicture)
        btnTakePhoto.setOnClickListener(View.OnClickListener { view ->
            TakePhoto()
        })

        val btnSelectPhoto: Button = findViewById<Button>(R.id.btnSelectPhoto)
        btnSelectPhoto.setOnClickListener(View.OnClickListener { view ->
           selectPhoto()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.mnuDelete)?.setVisible(isEdit)
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

    fun saveContact(){
        try {
            val contact = Contact()
            contact.Name = txtName.text.toString()
            contact.LastName = txtLastName.text.toString()
            contact.Phone = txtPhone.text.toString()?.toInt()
            contact.Email = txtEmail.text.toString()
            contact.Address = txtAddress.text.toString()
            contact.Photo = (imgPhoto?.drawable as BitmapDrawable).bitmap

            if (dataValidation(contact)){
                if (!isEdit)
                    ContactModel.addContact(contact)
                else
                    ContactModel.updateContact(contactIdEdit, contact)

                cleanScreen()
                Toast.makeText(this, getString(R.string.msgSave).toString(),Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun deleteContact(){
        //if (dataValidation()){

            Toast.makeText(this, getString(R.string.msgDelete).toString(),Toast.LENGTH_LONG).show()
        //}else{
          //  Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
        //}
    }

    fun dataValidation(contact: Contact): Boolean{
        return contact.Name.length > 0 && contact.LastName.length > 0 && contact.Address.length > 0 && contact.Email.length > 0 && contact.Phone > 0
    }

    fun cleanScreen(){
        contactIdEdit = ""
        isEdit=false
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
    }

    fun loadEditContact(id: String): Boolean{
        try{
            val contact = ContactModel.getContact(id)
            contactIdEdit= contact.FullName.trim()
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
            imgPhoto.setImageBitmap(contact.Photo)

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

                ContactModel.deleteContact(contactIdEdit)
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
   
}