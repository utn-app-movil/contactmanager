package cr.ac.utn.contactmanager

import Entity.Contact
import Model.ContactModel
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnShowMessage: Button = findViewById<Button>(R.id.btnShowMessage)
        btnShowMessage.setOnClickListener(View.OnClickListener { view ->
            Toast.makeText(this, getString(R.string.mainMessage).toString()
                    , Toast.LENGTH_LONG).show()
        })


        val btnShowContact: Button = findViewById<Button>(R.id.btnShowContact_main)
        btnShowContact.setOnClickListener(View.OnClickListener { view ->
            openActivity (ContactActivity::class.java)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean{
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.showContact_mainmenu -> {
                openActivity (ContactActivity::class.java)
                true
            }
            R.id.showContactList_mainmenu -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openActivity(objclass: Class<*>){
        val contactIntent = Intent(this, objclass)
        startActivity(contactIntent, null)
    }
}