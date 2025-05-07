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

// Activity for handling QR code scanning and product retrieval
class qrscan : AppCompatActivity() {

    // Firebase Firestore database reference
    private val db = Firebase.firestore

    // UI components
    private lateinit var scan_button : Button
    private lateinit var resultTV: TextView
    private lateinit var btnUpdateqr: Button

    // Stores scanned barcode values
    private var barcode: String = ""
    private var currentBarcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge screen layout
        enableEdgeToEdge()

        // Set the activity layout
        setContentView(R.layout.activity_qrscan)

        // Adjust padding to avoid overlap with system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI elements
        btnUpdateqr = findViewById(R.id.btnUpdateqr)
        resultTV = findViewById(R.id.resultTV)
        scan_button = findViewById(R.id.scan_button)

        // Set click listener to start barcode scanning
        scan_button.setOnClickListener {
            startBarcodeScanning()
        }

        // Set click listener to update product if barcode is scanned
        btnUpdateqr.setOnClickListener {
            currentBarcode?.let {
                val intent = Intent(this, EditProductActivity::class.java)
                intent.putExtra("barcode", it)
                startActivity(intent)
            }
        }
    }

    // Configures and starts the barcode scanner
    private fun startBarcodeScanning() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES) // Allows for wide range of barcode formats
        options.setPrompt("Scan Product Barcode")
        options.setBeepEnabled(true)
        options.setOrientationLocked(false)
        options.setCaptureActivity(CaptureActivity::class.java)
        barcodeLauncher.launch(options) // Launch the scanner
    }

    // Processes scanned barcode and fetches product from Firestore
    private fun handleBarcode(barcode: String) {
        Log.d("Barcode", "handleBarcode called with: $barcode")

        val productRef = db.collection("products").document("$barcode")

        productRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // If product exists, display details
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
                    // If product doesn't exist, prompt to create it
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
                }
            }
            .addOnFailureListener { e ->
                // Handle errors while fetching document
                Log.w("Firestore", "Error getting document", e)
            }
    }

    // Barcode scanning result callback
    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result: ScanIntentResult ->
        if (result.contents != null) {
            // Handle scanned content
            handleBarcode(result.contents)
        } else {
            // Show a toast if scan was cancelled
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
