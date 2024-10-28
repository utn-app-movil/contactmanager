package cr.ac.utn.contactmanager

import Util.util
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        setupInsets()
        setupButtons()
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupButtons() {
        val btnAddContact = findViewById<Button>(R.id.btnMainAddContact)
        val btnViewContact = findViewById<Button>(R.id.btnMainViewContacts)
        val btnCustomList = findViewById<Button>(R.id.btnCustomList)
        val btnMainDialog = findViewById<Button>(R.id.btnMainDialog)

        btnAddContact.setOnClickListener { openActivity(ContactActivity::class.java) }
        btnViewContact.setOnClickListener { openActivity(ContactListActivity::class.java) }
        btnCustomList.setOnClickListener { openActivity(CustomListActivity::class.java) }
        btnMainDialog.setOnClickListener { displayExitDialog() }
    }

    private fun openActivity(activityClass: Class<*>) {
        util.openActivity(this, activityClass)
    }

    private fun displayExitDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("¿Desea cerrar la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ -> finish() }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }

        val alert = dialogBuilder.create()
        alert.setTitle("Confirmación")
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.mnu_Contact -> {
                openActivity(ContactActivity::class.java)
                true
            }
            R.id.mnu_ContactList -> {
                openActivity(ContactListActivity::class.java)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
