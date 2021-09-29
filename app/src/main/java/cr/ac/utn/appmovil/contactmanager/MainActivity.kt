package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btnAddContact: Button = findViewById<Button>(R.id.main_btnContactList)
        btnAddContact.setOnClickListener(View.OnClickListener { view ->
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    fun openContact(view: View){
        val intent = Intent(this, ContactActivity::class.java)
        startActivity(intent)
    }
}