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
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import android.util.Log
import com.example.mobileapp.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.app.AlertDialog
import android.content.Intent


class qrscan : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var scan_button : Button
    private lateinit var resultTV: TextView
    private lateinit var btnUpdateqr: Button
    private var barcode: String = ""
    private var currentBarcode: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_qrscan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnUpdateqr = findViewById(R.id.btnUpdateqr)

        resultTV = findViewById(R.id.resultTV)
        scan_button = findViewById(R.id.scan_button)
        scan_button.setOnClickListener {
            startBarcodeScanning()
        }
        btnUpdateqr.setOnClickListener {
            currentBarcode?.let {
                val intent = Intent(this, EditProductActivity::class.java)
                intent.putExtra("barcode", it)
                startActivity(intent)
            }
        }
    }

    private fun startBarcodeScanning() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan Product Barcode")
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)
        options.setCaptureActivity(CaptureActivity::class.java)
        barcodeLauncher.launch(options)
    }




    private fun handleBarcode(barcode: String) {
        Log.d("Barcode", "handleBarcode called with: $barcode")
        val productRef = db.collection("products").document("$barcode")

        productRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Barcode", "Document Exists")
                    currentBarcode = barcode
                    val product = document.toObject(Product::class.java)
                    val productInfo = "Product exists:\n" +
                            "Barcode: $barcode\n" +
                            "Name: ${product?.Name}\n" +
                            "Description: ${product?.Description}\n" +
                            "Quantity: ${product?.Quantity}"
                    this.resultTV.text = productInfo

                } else {
                    Log.d("Barcode", "Document Doesn't Exist")
                    val productInfo = "Product not found:\n" +
                            "Barcode: $barcode"
                    this.resultTV.text = productInfo


                    AlertDialog.Builder(this)
                        .setTitle("Product not found")
                        .setMessage("Would you like to create it?")
                        .setPositiveButton("Yes") { _, _ ->
                            val intent = Intent(this, barcodeadd::class.java)
                            intent.putExtra("defaultName", barcode)
                            startActivity(intent)
                        }
                        .setNegativeButton("No", null)
                        .show()

//
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error getting document", e)
            }
    }


    private val barcodeLauncher = registerForActivityResult(ScanContract()){ result: ScanIntentResult ->
        if (result.contents != null) {
            handleBarcode(result.contents)
        }else {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

        }

    }


}
