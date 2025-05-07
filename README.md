# mobileapp - Android Inventory Management Application

## Overview

`mobileapp` is an Android application designed to help a company manage their inventory efficiently. It allows users to add products, track quantities, and update product information. This application leverages Firebase for its backend services, including Firestore for data storage and Firebase Cloud Messaging (FCM) for push notifications.

This appliaction was tested and emulated on a Google Pixel 7a.

## Features

*   **Product Management:**
    *   Add new products with details like name, barcode, description, and quantity.
    *   Update product information.
*   **Barcode Scanning:**
    *   Scan barcodes to quickly add products or retrieve product information.
*   **Data Storage:**
    *   Utilizes Firebase Firestore for secure and scalable data storage.
*   **Push Notifications:**
    *   Implements Firebase Cloud Messaging (FCM) to send notifications to users.
*   **User Interface:**
    *   Clean and intuitive user interface for easy navigation and interaction.

## Technologies Used

*   **Kotlin:** The primary programming language for Android development.
*   **Android SDK:** For building the Android application.
*   **Firebase:**
    *   **Firestore:** For storing and retrieving product data.
    *   **Firebase Cloud Messaging (FCM):** For handling push notifications.
* **Gradle**: For dependency and build management.


## Activity Files

All files have imbedded comments to describe each code block's purpose. This section is an overview of what each activity file will contain.

### MainActivity

This file contains activities for the home page and uses the 'activity_main.xml' file for the layout. The homepage has a logo and 2 large buttons for ease of one handed use with either left or the right hand.
The activity class contains instructions for buttons if they are clicked.

### inventory

This file is run by 'MainActivity' when the 'INVENTORY' button is clicked. It contains functions for buttons and the results text view
detailed in the 'activity_inventory.xml' file. It also contains handling of searching for products in Firestore.

### CreateProductActivity

This file is run by 'inventory' when the user clicks 'yes' button when prompted to add product. It contains functions for buttons and the text fields
detailed in the 'activity_create_product.xml' file. It also handles saving the data into Firestore.

### EditPoductActivity

This file is run by 'inventory' and 'qrscan' when the 'UPDATE' button is clicked. It contains functions for buttons and the text fields
detailed in the 'activity_edit_product.xml' file. It also handles saving the data into Firestore.


### qrscan

This file is run by 'MainActivity' when the 'SCAN' button is clicked. It contains functions for buttons and results text view detailed
in the 'activity_qrscan.xml' file. It also contains barcode scanner configuration and handling of scanned barcodes.

### barcodeadd 

This file is run by 'qrscan' when the user clicks 'yes' button when prompted to add product. It contains functions for buttons and the text fields
detailed in the 'activity_create_product.xml' file. It also handles saving the data into Firestore.

### MyFirebaseMessagingService

This file is run when a notification is recieved from Firebase Cloud Messaging. It conatins creation and handling of the notification and uses the 
layout file 'activity_fcm' to format the notification.
