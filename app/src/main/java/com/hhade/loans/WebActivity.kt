package com.hhade.loans

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

@SuppressLint("SetJavaScriptEnabled")
class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        close_button.setOnClickListener {
            this.finish()
        }

        web_view.webViewClient = WebViewClient()
        web_view.settings.javaScriptEnabled = true
        val url = intent.getStringExtra("url")
        web_view.loadUrl(url)
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
