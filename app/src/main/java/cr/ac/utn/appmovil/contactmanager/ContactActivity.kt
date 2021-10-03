package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import cr.ac.utn.appmovil.controller.ContactController
import cr.ac.utn.appmovil.model.*

class ContactActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

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
        if (dataValidation()){
            val contact = Contact()
            contact.Name = findViewById<EditText>(R.id.txtContactName).toString()
            contact.LastName = findViewById<EditText>(R.id.txtContactLastName).toString()
            contact.Phone = findViewById<EditText>(R.id.txtContactPhone).toString()?.toInt()
            contact.Email = findViewById<EditText>(R.id.txtContactEmail).toString()
            contact.Address = findViewById<EditText>(R.id.txtContactAddress).toString()
            ContactController.addContact(contact)
            Toast.makeText(this, getString(R.string.msgSave).toString(),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun deleteContact(){
        if (dataValidation()){

            Toast.makeText(this, getString(R.string.msgDelete).toString(),Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(this, getString(R.string.msgInvalidData).toString(),Toast.LENGTH_LONG).show()
        }
    }

    fun dataValidation(): Boolean{

        return true
    }
}