package cr.ac.utn.contactmanager


import Util.util
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAddContact= findViewById<Button>(R.id.btnMainAddContact)
        btnAddContact.setOnClickListener(View.OnClickListener {
            util.openActivity(this, ContactActivity::class.java)
        })

        val btnViewContact= findViewById<Button>(R.id.btnMainViewContacts)
        btnViewContact.setOnClickListener(View.OnClickListener {
            util.openActivity(this, ContactListActivity::class.java)
        })

        val btnLlamarCustomList= findViewById<Button>(R.id.btnCustomList)
        btnLlamarCustomList.setOnClickListener(View.OnClickListener {
            util.openActivity(this, CustomListActivity::class.java)
        })

        val btnMainDialog = findViewById<Button>(R.id.btnMainDialog)
        btnMainDialog.setOnClickListener(View.OnClickListener {
            DisplayDialog()
        })
    }

    private fun DisplayDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Desea cerrar la aplicación?")
            .setCancelable(false)
            .setPositiveButton("Si", DialogInterface.OnClickListener{
                dialog, id -> finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Titulo del dialog")
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mnu_Contact ->{
                util.openActivity(this, ContactActivity::class.java)
                return true
            }
            R.id.mnu_ContactList ->{
                util.openActivity(this, ContactListActivity::class.java)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }









}