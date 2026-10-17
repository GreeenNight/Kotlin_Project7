package com.footballkick.project7

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.michaelrocks.paranoid.Obfuscate
import java.util.*
import android.util.Base64
import android.util.Log
import com.footballkick.project7.GameActivity
import com.footballkick.project7.R
import com.footballkick.project7.WebFragment

@Obfuscate
class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    val base64url = "aHR0cDovLzE4NS4yNTMuNDcuMTUwL2hYOWhuOGM5"
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this, MenuActivity::class.java))

//        sharedPreferences = getSharedPreferences("main", 0)
//        val savedUrl = sharedPreferences.getString("url", "")
//
//        if(!savedUrl.isNullOrEmpty()){
//            Log.d("MyLog savedUrl", savedUrl)
//            showWebView(savedUrl)
//        }else{
//            val decodedBytes = Base64.decode(base64url, Base64.DEFAULT)
//            val url = String(decodedBytes)
//            if (isConnected()){
//                showWebView(url)
//            }else{
//                val dialogFragment = NoInternetDialogFragment()
//                dialogFragment.show(supportFragmentManager, "NoInternetDialogFragment")
//            }
//        }

    }

    fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    fun showWebView(url: String) {
        val fragment = WebFragment()
        val bundle = Bundle()
        bundle.putString("url", url)
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_main, fragment)
            .commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_main)
        if (fragment is WebFragment) {
            if (fragment.sWebView!!.canGoBack()) {
                fragment.sWebView!!.goBack()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }
}