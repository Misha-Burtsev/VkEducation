package com.example.intenttaskvk

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editText = findViewById(R.id.editText)

        findViewById<Button>(R.id.buttonOpen).setOnClickListener { openSecondActivity() }
        findViewById<Button>(R.id.buttonCall).setOnClickListener { callFriend() }
        findViewById<Button>(R.id.buttonShare).setOnClickListener { shareText() }
    }

    private fun openSecondActivity() {
        val text = getTextOrShowError() ?: return
        val intent = Intent(this, SecondActivity::class.java)
        intent.putExtra(SecondActivity.EXTRA_TEXT, text)
        startActivity(intent)
    }

    private fun callFriend() {
        val phone = getTextOrShowError() ?: return
        if (!Patterns.PHONE.matcher(phone).matches()) {
            editText.error = "Введите корректный номер телефона"
            return
        }
        val intent = Intent(Intent.ACTION_DIAL, "tel:$phone".toUri())
        startSafely(intent)
    }

    private fun shareText() {
        val text = getTextOrShowError() ?: return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startSafely(Intent.createChooser(intent, "Поделиться через"))
    }

    private fun getTextOrShowError(): String? {
        val text = editText.text.toString().trim()
        if (text.isEmpty()) {
            editText.error = "Поле не должно быть пустым"
            return null
        }
        return text
    }

    private fun startSafely(intent: Intent) {
        try {
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            Toast.makeText(this, "Нет приложения для этого действия", Toast.LENGTH_SHORT).show()
        }
    }
}