package com.info.makup

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.info.makup.R

// NE PAS importer android.R (ça masque la R de ton application)

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private val HOME_URL = "https://elitemakeup.minfoma.online/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // référence explicite possible

        webView = findViewById(R.id.webview)
        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.loadWithOverviewMode = true
        settings.useWideViewPort = true

        WebView.setWebContentsDebuggingEnabled(true)

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                val host = request.url.host ?: return true
                return if (host.endsWith("elitemakeup.minfoma.online")) {
                    false // rester dans la WebView
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    true
                }
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                Toast.makeText(this@MainActivity, "Erreur de chargement", Toast.LENGTH_SHORT).show()
                Log.e("WebView", "Erreur: ${error.description}")
            }
        }

        webView.loadUrl(HOME_URL)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}