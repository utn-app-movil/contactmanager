package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun saveContact(){
        Toast.makeText(this, getString(R.string.msgSuccess).toString(),Toast.LENGTH_LONG).show()
    }
}