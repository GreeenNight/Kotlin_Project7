package com.footballkick.project7

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.*
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.onesignal.OneSignal
import java.util.*
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class WebFragment : Fragment() {
    private var webviewstate: Bundle? = null
    var sWebView: WebView? = null
    private val FILE_CHOOSER_REQUEST_CODE = 1
    private var fileChooser: ValueCallback<Array<Uri>>? = null
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onPause() {
        super.onPause()
        webviewstate = Bundle()
        sWebView?.saveState(webviewstate!!)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireActivity().getSharedPreferences("main", 0)
        val rootView: View = inflater.inflate(R.layout.fragment_web, container, false)
        sWebView = rootView.findViewById(R.id.sWebView)

        sWebView!!.canGoBack()
        sWebView!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action === MotionEvent.ACTION_UP && sWebView!!.canGoBack()) {
                sWebView!!.goBack()
                return@OnKeyListener true
            }
            false
        })

        progressBar = rootView.findViewById(R.id.progressBar)

        setSettings(sWebView!!.settings)
        sWebView!!.webViewClient = object : WebViewClient() {
                @Deprecated("Deprecated in Java")
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (Uri.parse(url).host!!.startsWith("localhost")) {
                    runGame()
                    activity?.finish()
                } else if (url.startsWith("http://") || url.startsWith("https://")) {
                    sWebView!!.visibility = View.VISIBLE
                    saveData(Uri.parse(url).toString())
                    initOS()
                    return false
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)

            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.GONE
                sWebView!!.visibility = View.VISIBLE
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
            }
        }

        //file chooser
        sWebView!!.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                if (fileChooser != null) {
                    fileChooser?.onReceiveValue(null)
                    fileChooser = null
                }
                fileChooser = filePathCallback

                val intent = fileChooserParams?.createIntent()
                intent?.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE)
                } catch (e: ActivityNotFoundException) {
                    fileChooser = null
                    return false
                }

                return true
            }
        }

        if(webviewstate==null){
            val url = requireArguments().getString("url")
            if (url != null) {
                sWebView!!.loadUrl(url)
            }
        }else{
            sWebView?.restoreState(webviewstate!!)
        }
        return rootView
    }

    private fun setSettings(settings: WebSettings) {
        val manager = CookieManager.getInstance()
        manager.setAcceptCookie(true)
        settings.javaScriptEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.setSupportZoom(false)
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true
    }

    private fun noConnection() {
        sWebView!!.visibility = View.GONE
    }

    fun initOS(){
        val uuid = UUID.randomUUID().toString()
        OneSignal.initWithContext(requireContext())
        OneSignal.setAppId("***")
        OneSignal.setExternalUserId(uuid)
    }

    fun saveData(value: String) {
        sharedPreferences.edit()
            .putString("url", value)
            .apply();
    }

    private fun runGame() {
        val intent = Intent(requireContext(), GameActivity::class.java)
        startActivity(intent)
    }
}