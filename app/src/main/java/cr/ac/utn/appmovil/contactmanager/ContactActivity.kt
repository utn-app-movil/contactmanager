package cr.ac.utn.appmovil.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel
import cr.ac.utn.appmovil.util.EXTRA_MESSAGE_CONTACTID
import java.lang.Exception

class ContactActivity : AppCompatActivity() {

    lateinit var txtName: EditText
    lateinit var txtLastName: EditText
    lateinit var txtPhone: EditText
    lateinit var txtEmail: EditText
    lateinit var txtAddress: EditText
    lateinit var contactModel: ContactModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        contactModel= ContactModel(this)
        txtName = findViewById<EditText>(R.id.txtContactName)
        txtLastName = findViewById<EditText>(R.id.txtContactLastName)
        txtPhone = findViewById<EditText>(R.id.txtContactPhone)
        txtEmail = findViewById<EditText>(R.id.txtContactEmail)
        txtAddress = findViewById<EditText>(R.id.txtContactAddress)

        /*-------------TEMPORAL- DESPUES HAY QUE BORRARLO--------------------------*/
        addContact()

        val btnSaveContact: Button = findViewById<Button>(R.id.btnSaveContact)
        btnSaveContact.setOnClickListener(View.OnClickListener { view ->
            val contactM= ContactModel(this)
            try{
                var contact = contactM.getContact("A100")
                Toast.makeText(this, contact.FullName, Toast.LENGTH_LONG).show()

            }catch (e: Exception){
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        })
        //--------------------------------------------


        val contactId = intent.getStringExtra(EXTRA_MESSAGE_CONTACTID)
        if (contactId != null && contactId != "") loadEditContact(contactId.toString())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.contact_menu, menu)
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

            if (dataValidation(contact)){
                contactModel.addContact(contact)
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
        txtName.setText("")
        txtLastName.setText("")
        txtPhone.setText("")
        txtEmail.setText("")
        txtAddress.setText("")
    }

    fun loadEditContact(id: String){
        try{
            val contact = contactModel.getContact(id)
            txtName.setText(contact.Name)
            txtLastName.setText(contact.LastName)
            txtPhone.setText(contact.Phone.toString())
            txtEmail.setText(contact.Email)
            txtAddress.setText(contact.Address)
        }catch (e: Exception){
            Toast.makeText(this, e.message.toString(),Toast.LENGTH_LONG).show()
        }
        //val btnDelete = findViewById<MenuItem>(R.id.mnuDelete)
        //btnDelete.setVisible(false)
    }


    fun addContact(){
        val contactM= ContactModel(this)
        var contact = Contact("A100", "Ever", "Barahona", 12354, "ebarahona@utn.ac.cr", "Puntarenas", "Costa Rica")
        contactM.addContact(contact)
    }
}