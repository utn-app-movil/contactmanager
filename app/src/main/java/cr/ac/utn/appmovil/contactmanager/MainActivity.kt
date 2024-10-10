package cr.ac.utn.appmovil.contactmanager

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnContactList: Button = findViewById<Button>(R.id.main_btnContactList)
        btnContactList.setOnClickListener(View.OnClickListener { view ->
            openActivity(ContactListActivity::class.java)
        })

        val btnContactListCustom: Button = findViewById<Button>(R.id.main_btnContactListCustom)
        btnContactListCustom.setOnClickListener(View.OnClickListener { view ->
            openActivity(ContactListCustomActivity::class.java)
        })

        val btnRecyclerView: Button = findViewById<Button>(R.id.btnRecycleView)
        btnRecyclerView.setOnClickListener(View.OnClickListener { view ->
            openActivity(RecyclerViewActivity::class.java)
        })

        val btnDisplayDialog: Button = findViewById<Button>(R.id.btngetDialog)
        btnDisplayDialog.setOnClickListener(View.OnClickListener { view ->
            DisplayDialog()
        })

        val btnCustomDisplayDialog: Button = findViewById<Button>(R.id.btnDisplayCustomDialog)
        btnCustomDisplayDialog.setOnClickListener(View.OnClickListener { view ->
            DisplayCustomeDialog()
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
        util.openActivity(this,objclass, "", "")
    }

    fun DisplayDialog(){
        val dialogBuilder = AlertDialog.Builder(this)

        //dialogBuilder.apply { setTitle("Hello") }.create().show()

        dialogBuilder.setMessage(getString(R.string.QuestionCloseApp).toString())
            .setCancelable(false)
            .setPositiveButton(getString(R.string.Ok).toString(), DialogInterface.OnClickListener {
                    dialog, id -> finish()
            })
            .setNegativeButton(getString(R.string.Cancel).toString(), DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })
            .setNeutralButton("Neutral"){ _, _ ->
                Toast.makeText(this, "Clicking neutral button", Toast.LENGTH_LONG).show()
            }

        // create dialog box
        val alert = dialogBuilder.create()
        // set title for alert dialog box
        alert.setTitle(getString(R.string.TitleDialogQuestion).toString())
        // show alert dialog
        alert.show()
    }

    fun DisplayCustomeDialog() {
        val inflater: LayoutInflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogBinding = inflater.inflate(R.layout.custom_dialog, null, false)

        val dialogBuilder = AlertDialog.Builder(this, 0).create()

        dialogBuilder.apply {
            setView(dialogBinding)
            setCancelable(false)
        }.show()

        var btnOk: Button = dialogBinding.findViewById(R.id.btnOk_CustomDialog)

        btnOk.setOnClickListener(View.OnClickListener { view ->
            dialogBuilder.dismiss()
        })
    }
}
