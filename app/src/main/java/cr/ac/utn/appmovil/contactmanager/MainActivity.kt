package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import cr.ac.utn.appmovil.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnContactList: Button = findViewById<Button>(R.id.main_btnContactList)
        btnContactList.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this, ContactListActivity::class.java)
            startActivity(intent)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnuAddContact -> {
                openActivity(ContactActivity::class.java)
                true
            }
            R.id.mnuViewContacts ->{
                openActivity(ContactListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun addContactButon(view: View) {
        openActivity(ContactActivity::class.java)
    }

    fun openActivity(objclass: Class<*>){
        util.openActivity(this,objclass)
        //val intent = Intent(this, objclass)
        //startActivity(intent)
    }
}