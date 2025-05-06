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
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mobileapp.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.app.Activity
import android.content.Intent


class qrscan : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var scan_button : Button
    private lateinit var resultTV: TextView
    private var barcode: String = ""

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
            startBarcodeScanning()
        }
    }
//        scan_button.setOnClickListener {
//            val options = ScanOptions()
//            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
//            options.setPrompt("Scan Product Barcode")
//            options.setBeepEnabled(true)
//            options.setOrientationLocked(false)
//            options.setCaptureActivity(CaptureActivity::class.java)
//            barcodeLauncher.launch(options)
//        }
//    }

    private fun startBarcodeScanning() {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setPrompt("Scan Product Barcode")
        options.setBeepEnabled(false)
        options.setOrientationLocked(false)
        options.setCaptureActivity(CaptureActivity::class.java)
        barcodeLauncher.launch(options)
    }

    // register for result from the add_product activity.
    private val addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val name = data?.getStringExtra("name")
            val description = data?.getStringExtra("description")
            val quantity = data?.getIntExtra("quantity", 0) ?: 0
            //add the new product to the database.
            val newProduct = Product(barcode, name, description, quantity)
            db.collection("products").document(barcode).set(newProduct)
            Log.d("Firestore", "Product added to database")
            val productInfo = "Product added:\n" +
                    "Barcode: ${newProduct.Barcode}\n" +
                    "Name: ${newProduct.Name}\n" +
                    "Description: ${newProduct.Description}\n" +
                    "Quantity: ${newProduct.Quantity}"
            this.resultTV.text = productInfo
        }
    }

    private fun handleBarcode(barcode: String) {
        Log.d("Barcode", "handleBarcode called with: $barcode")
        val productRef = db.collection("products").document(barcode)

        productRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    Log.d("Barcode", "Document Exists")
                    // Product exists, display info and offer edit option
                    val product = document.toObject(Product::class.java)
                    val productInfo = "Product exists:\n" +
                            "Barcode: ${product?.Barcode}\n" +
                            "Name: ${product?.Name}\n" +
                            "Description: ${product?.Description}\n" +
                            "Quantity: ${product?.Quantity}"
                    this.resultTV.text = productInfo

                    //Offer edit option
                } else {
                    Log.d("Barcode", "Document Doesn't Exist")
                    // Product does not exist, offer add option
                    val productInfo = "Product not found:\n" +
                            "Barcode: $barcode"
                    this.resultTV.text = productInfo
                    //start the AddProductActivity to create a new product.
                    val intent = Intent(this, add_product::class.java)
                    intent.putExtra("barcode", barcode)
                    addProductLauncher.launch(intent)
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
