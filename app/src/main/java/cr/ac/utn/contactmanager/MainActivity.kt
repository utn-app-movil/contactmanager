package cr.ac.utn.contactmanager

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnShowMessage: Button = findViewById<Button>(R.id.btnMainShowMessage)
        btnShowMessage.setOnClickListener(View.OnClickListener { view ->
            Toast.makeText(this, getString(R.string.mainMessage).toString()
                    , Toast.LENGTH_LONG).show()
        })

        val clmain: ConstraintLayout = findViewById(R.id.scrnMain)
        class myUndoListener : View.OnClickListener {
            override fun onClick(v: View) {
                val snackbar =
                    Snackbar.make(clmain, R.string.CancelledSending, Snackbar.LENGTH_SHORT)
                snackbar.show()
            }
        }

        val btnShowMessageSnack: Button = findViewById<Button>(R.id.btnMainDisplaySnack)
        btnShowMessageSnack.setOnClickListener(View.OnClickListener { view ->
            val mySnackbar = Snackbar.make(clmain,
                R.string.SendingMessage, Snackbar.LENGTH_LONG)
            mySnackbar.setAction(R.string.UndoMessage, myUndoListener())
            mySnackbar.show()
        })

        val btnDisplayScreen: Button = findViewById<Button>(R.id.btnMainShowScreen)
        btnDisplayScreen.setOnClickListener(View.OnClickListener { view ->
            val intentScreen2 = Intent(this, Screen2Activity::class.java)
            startActivity(intentScreen2)
        })
    }
}