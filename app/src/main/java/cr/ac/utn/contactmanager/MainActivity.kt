package cr.ac.utn.contactmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    }
}