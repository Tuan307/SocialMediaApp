package com.base.app.ui.explore_city.detail_city

import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.databinding.ActivityDetailExploreBinding


class DetailExploreActivity : AppCompatActivity() {
    private var cityId = ""
    private var url = ""
    private lateinit var binding: ActivityDetailExploreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_explore)
        setContentView(binding.root)
        val intent = intent
        cityId = intent.getStringExtra("id").toString()
        url = intent.getStringExtra("url").toString()
        binding.progressBarDetailCity.visibility = View.VISIBLE
        binding.imageBack.setOnClickListener {
            finish()
        }
        setUpWebView()
    }

    private fun setUpWebView() = with(binding) {
        webViewDetailCity.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webViewDetailCity.clearCache(true)
        webViewDetailCity.clearHistory()
        webViewDetailCity.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webViewDetailCity.settings.javaScriptEnabled = true
        webViewDetailCity.webChromeClient = WebChromeClient()
        webViewDetailCity.settings.domStorageEnabled = true
        webViewDetailCity.settings.useWideViewPort = true
        webViewDetailCity.settings.loadWithOverviewMode = true
        webViewDetailCity.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.cancel()
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                val script2 = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section top-header sticky');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[0]); }" +
                        "})()"
                view?.loadUrl(script2)
                val script = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section page-detail');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[1]); }" +
                        "})()"
                view?.loadUrl(script)
                val script1 = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section footer');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[0]); }" +
                        "})()"
                view?.loadUrl(script1)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBarDetailCity.visibility = View.GONE
                }, 1000)
            }

            override fun onPageFinished(view: WebView, url: String) {
                val script2 = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section top-header sticky');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[0]); }" +
                        "})()"
                view.loadUrl(script2)
                val script = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section page-detail');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[1]); }" +
                        "})()"
                view.loadUrl(script)
                val script1 = "javascript:(function() { " +
                        "var elements = document.getElementsByClassName('section footer');" +
                        "while(elements.length > 0){ elements[0].parentNode.removeChild(elements[0]); }" +
                        "})()"
                view.loadUrl(script1)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressBarDetailCity.visibility = View.GONE
                }, 1000)
            }
        }
        webViewDetailCity.loadUrl(url)
    }
}