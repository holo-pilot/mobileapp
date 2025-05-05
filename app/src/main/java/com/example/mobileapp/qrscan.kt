package com.example.mobileapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class qrscan : AppCompatActivity() {

    lateinit var scan_button : Button
    lateinit var resultTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrscan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultTV = findViewById(R.id.resultTV)
        scan_button = findViewById(R.id.scan_button)
        scan_button.setOnClickListener {
            val options = ScanOptions()
            options.setPrompt("Scan Product Barcode")
            options.setBeepEnabled(true)
            options.setOrientationLocked(true)
            options.setCaptureActivity(CaptureActivity::class.java)
            barcodeLauncher.launch(options)
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()){ result ->
        if (result.contents != null) {
            resultTV.text = "Result: ${result.contents}"
        }else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }

    }

}