package com.footballkick.project7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val button1:Button = findViewById(R.id.button1)
        val button2:Button = findViewById(R.id.button2)

        button1.setOnClickListener{
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        button2.setOnClickListener{
            finish()
        }


    }
}