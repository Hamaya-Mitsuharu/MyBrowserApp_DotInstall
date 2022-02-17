package com.example.mybrowserapp_dotinstall

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import com.example.mybrowserapp_dotinstall.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var myWebView: WebView? = null
    private lateinit var urlText : EditText
    private val INITIAL_WEBSITE = "http://dotinstall.com"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        myWebView = binding.myWebView
        urlText = binding.urlText

        binding.browseButton.setOnClickListener(){
            var url = urlText.getText().toString().trim()
            // WebサイトURLのパターンにマッチしない場合はエラー
            if (!Patterns.WEB_URL.matcher(url).matches()){
                urlText.setError("Invalid URL")
            }
            else {
                // http、httpsで始まらないURLはhttpを付けたものにする
                if (!url.startsWith("http://") && !url.startsWith("https://")){
                    url = "http://" + url
                }
                myWebView?.loadUrl(url)
            }
        }

        urlText.setOnClickListener(){
            urlText.setText("")
        }

        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.setWebViewClient(object: WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?){
                super.onPageFinished(view, url)
                supportActionBar?.setSubtitle(view?.getTitle())
                urlText.setText(url)
            }
        })
        myWebView?.loadUrl(INITIAL_WEBSITE)
    }

    override fun onBackPressed() {
        if (myWebView?.canGoBack() == true){
            myWebView?.goBack()
            return
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (myWebView != null) {
            myWebView?.stopLoading()
            myWebView?.webViewClient = WebViewClient()
            myWebView?.destroy()
        }
        myWebView = null
    }

    // Itemが作られた時に呼ばれる
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Itemがタップされた時に呼ばれる
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        
        when (id) {
            R.id.action_reload  -> {
                myWebView?.reload()
                return true
            }
            R.id.action_forward -> {
                myWebView?.goForward()
                return true
            }
            R.id.action_back    -> {
                myWebView?.goBack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}