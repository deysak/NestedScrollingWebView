package com.deysak.nestedscrollingwebview

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appBarLayout = findViewById(R.id.app_bar)

        webView = findViewById(R.id.webview)
        with (webView) {
            isNestedScrollingEnabled = true
            with (settings) {
                displayZoomControls = false
                builtInZoomControls = true
                setSupportZoom(true)
            }
        }

        webView.loadUrl("file:///android_asset/example.html")
    }
}
