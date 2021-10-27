package com.assignment.clevertapassignment

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.assignment.clevertapassignment.constants.AppConstant
import com.assignment.clevertapassignment.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.clevertap.android.sdk.CleverTapAPI
import com.google.firebase.messaging.FirebaseMessaging
import java.util.*
import kotlin.collections.HashMap

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity(){

    lateinit var cleverTapDefaultInstance: CleverTapAPI
    private var _binding:ActivityMainBinding?=null
    private val binding get() = _binding!!
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initializeClevertapApi()
        }
        loadImage()
        setListener()
    }



    private fun setListener() {
        binding.outlinedButton.setOnClickListener {
            if(binding.outlinedButton.text.equals(getString(R.string.tap_me))){
                binding.outlinedButton.text=getString(R.string.product_viewed)
                sendEventsToWebApp()
            }
        }
    }

    private fun sendEventsToWebApp() {
        // event with properties
        val prodViewedAction = mapOf(
            "Product ID" to 1,
            "Product Image" to AppConstant.IMAGE_LINK,
            "Product Name" to "CleverTap",
            "Email" to "krishanakant.pandey@sensen.ai")
        cleverTapDefaultInstance.pushEvent("Product viewed", prodViewedAction)
    }

    private fun loadImage() {
        Glide.with(binding.root)
            .load(AppConstant.IMAGE_LINK)
            .error(R.drawable.ic_launcher_background)
            .into(binding.imageLoad)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeClevertapApi() {
        cleverTapDefaultInstance = CleverTapAPI.getDefaultInstance(this)!!

        val fcmRegId = FirebaseMessaging.getInstance().token.addOnSuccessListener {
            cleverTapDefaultInstance.pushFcmRegistrationId(it,true)
        }
        CleverTapAPI.createNotificationChannelGroup(applicationContext,"YourGroupId","YourGroupName");
        val profileUpdate = HashMap<String, Any>()
        profileUpdate["Name"] = "Krishna" // String

        profileUpdate["Identity"] = "884-5K7-RK6Z" // String or number

        profileUpdate["Email"] = "krishanakant.pandey@sensen.ai" // Email address of the user

        profileUpdate["Phone"] = "9955380066" // Phone (with the country code, starting with +)

        profileUpdate["Gender"] = "M" // Can be either M or F
        profileUpdate["DOB"] = Date().time

        profileUpdate["MSG-email"] = true // Disable email notifications

        profileUpdate["MSG-push"] = true // Enable push notifications

        profileUpdate["MSG-sms"] = true // Disable SMS notifications

        profileUpdate["MSG-whatsapp"] = true // Enable WhatsApp notifications
        cleverTapDefaultInstance.onUserLogin(profileUpdate)
    }


}