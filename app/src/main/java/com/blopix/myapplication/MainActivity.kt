package com.blopix.myapplication

import Util.util
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
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

        val btnPantallaContact: Button = findViewById<Button>(R.id.main_AddContact)
        btnPantallaContact.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, ContactActivity::class.java)

            Toast.makeText(this, getString(R.string.MensajeContacto).toString(), Toast.LENGTH_LONG)
                .show()
        })

        val btnPantallaContactList: Button = findViewById<Button>(R.id.main_viewContactList)
        btnPantallaContactList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, ContactListActivity::class.java)

            Toast.makeText(
                this,
                getString(R.string.MensajeListaContactos).toString(),
                Toast.LENGTH_LONG
            ).show()
        })

        val btnPantallaCustomList: Button = findViewById<Button>(R.id.btnCustomList)
        btnPantallaCustomList.setOnClickListener(View.OnClickListener { view ->
            util.openActivity(this, CustomListActivity::class.java)
        })

        val btnMainDialog = findViewById<Button>(R.id.btnMainDialog)
        btnMainDialog.setOnClickListener(View.OnClickListener {
            DisplayDialog()
        })
    }

    private fun DisplayDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("Desea cerrar la aplicacion?")
            .setCancelable(false)
            .setPositiveButton("si", DialogInterface.OnClickListener { dialog, id ->
                finish()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Titulo del dialogo")
        alert.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_contact -> {
                util.openActivity(this, ContactActivity::class.java)
                return true
            }

            R.id.menu_viewContactList -> {
                util.openActivity(this, ContactListActivity::class.java)
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /*private fun openActivity(objClass: Class<*>) {
        val intentContent = Intent(this, objClass )
        startActivity(intentContent)
    }*/
}