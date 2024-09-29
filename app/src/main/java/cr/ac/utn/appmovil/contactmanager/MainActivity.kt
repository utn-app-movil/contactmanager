package cr.ac.utn.appmovil.contactmanager

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

        val btnContact: Button = findViewById<Button>(R.id.main_btnAddContact)
        btnContact.setOnClickListener(View.OnClickListener { view ->
            openActivity(ContactActivity::class.java)
        })

        val btnContactList: Button = findViewById<Button>(R.id.main_btnContactList)
        btnContactList.setOnClickListener(View.OnClickListener { view ->
            openActivity(ContactListActivity::class.java)
        })

        val btnContactListCustom: Button = findViewById<Button>(R.id.main_btnContactListCustom)
        btnContactListCustom.setOnClickListener(View.OnClickListener { view ->
            openActivity(ContactListCustomActivity::class.java)
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

    private fun openActivity(objClass: Class<*>){
        util.openActivity(this,objClass, "", "")
    }
}