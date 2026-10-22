package name.boyle.chris.sgtpuzzles

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.util.TypedValueCompat.pxToDp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsCompat.Type.ime
import androidx.core.view.WindowInsetsCompat.Type.systemBars
import androidx.webkit.WebViewAssetLoader
import androidx.webkit.WebViewAssetLoader.AssetsPathHandler
import androidx.webkit.WebViewClientCompat
import name.boyle.chris.sgtpuzzles.NightModeHelper.ActivityWithNightMode
import name.boyle.chris.sgtpuzzles.databinding.ActivityHelpBinding
import java.io.IOException
import java.text.MessageFormat
import java.util.regex.Pattern

class HelpActivity : ActivityWithNightMode() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topic = intent.getStringExtra(TOPIC) ?: "index"
        if (!ALLOWED_TOPICS.matcher(topic).matches()) {
            finish()
            return
        }
        val binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        webView = binding.webView
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        ViewCompat.setOnApplyWindowInsetsListener(webView) { v, windowInsets ->
            val insets = windowInsets.getInsets(systemBars() or displayCutout() or ime())
            val displayMetrics = webView.context.resources.displayMetrics
            val padDp = pxToDp(insets.bottom.toFloat(), displayMetrics)
            webView.evaluateJavascript(
                """
                document.body.style.setProperty('padding-bottom', '${padDp}px');
                """, null)
            WindowInsetsCompat.CONSUMED
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(w: WebView, title: String) {
                supportActionBar?.title = getString(R.string.title_activity_help) + ": " + title
            }

            // onReceivedTitle doesn't happen on back button :-(
            override fun onProgressChanged(w: WebView, progress: Int) {
                if (progress == 100) {
                    supportActionBar?.title =
                        getString(R.string.title_activity_help) + ": " + w.title
                }
            }
        }
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler(ASSETS_PATH, AssetsPathHandler(this))
            .build()
        val onBackPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                when {
                    webView.canGoBack() -> webView.goBack()
                }
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        webView.webViewClient = object : WebViewClientCompat() {
            override fun shouldInterceptRequest(
                view: WebView,
                request: WebResourceRequest
            ): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }

            @Deprecated("Deprecated in Java")  // replace when we reach minSdk 24
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.startsWith(ASSETS_URL)) {
                    return false
                }
                // spawn other app
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(this@HelpActivity, R.string.activity_not_found, Toast.LENGTH_LONG).show()
                }
                return true
            }

            override fun onPageFinished(view: WebView, url: String) {
                view.requestApplyInsets()
                applyModernHelpStyles()
                applyNightCSSClass()
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                onBackPressedCallback.isEnabled = webView.canGoBack()
            }
        }
        with (webView.settings) {
            javaScriptEnabled = true
            // Setting this off for security. Off by default for SDK versions >= 16.
            @Suppress("DEPRECATION")
            allowFileAccessFromFileURLs = false
            // Off by default, deprecated for SDK versions >= 30.
            @Suppress("DEPRECATION")
            allowUniversalAccessFromFileURLs = false
            allowFileAccess = false
            allowContentAccess = false
            blockNetworkImage = true
            builtInZoomControls = true
            blockNetworkLoads = true
            displayZoomControls = false
        }
        // locales[0] needs minSdkVersion 24
        @Suppress("DEPRECATION") val lang = resources.configuration.locale.language
        val haveLocalised = try {
             resources.assets.list(lang)?.contains("$topic.html") ?: false
        } catch (_: IOException) {
            false
        }
        webView.loadUrl(ASSETS_URL + helpPath(if (haveLocalised) lang else "en", topic))
    }

    /** All local help pages, including Chinese translations, share one modern
     * presentation without having to alter the generated Halibut HTML files. */
    private fun applyModernHelpStyles() {
        webView.evaluateJavascript(
            """
            (function() {
              if (document.getElementById('modern-help-style')) return;
              var style = document.createElement('style');
              style.id = 'modern-help-style';
              style.textContent = `
                html { background: #f5f7fc; }
                body {
                  box-sizing: border-box; max-width: 850px;
                  margin: 0 auto; padding: 24px 22px 40px;
                  background: #ffffff; color: #17253b;
                  font: 16px/1.75 system-ui, -apple-system, sans-serif;
                  overflow-wrap: break-word;
                }
                h1, h2, h3 { line-height: 1.3; letter-spacing: -0.02em; }
                h1 { font-size: 1.65em; } h2 { font-size: 1.3em; }
                h3 { font-size: 1.1em; }
                a:link { color: #236ae0; }
                a:visited { color: #7954c5; }
                p, li { line-height: 1.75; }
                img { max-width: 100%; height: auto; }
                pre { overflow-x: auto; padding: 12px; border-radius: 12px; background: #edf3fa; }
                body.night { background: #1a2738; color: #edf3fc; }
                body.night a:link { color: #8db8ff; }
                body.night a:visited { color: #bea6ff; }
                body.night pre { background: #24354c; }
                html:has(body.night) { background: #111a29; }
              `;
              document.head.appendChild(style);
            })();
            """.trimIndent(),
            null
        )
    }

    private fun applyNightCSSClass() {
        webView.evaluateJavascript(
            if (NightModeHelper.isNight(resources.configuration)) {
                "document.body.className += ' night';"
            } else {
                "document.body.className = document.body.className.replace(/(?:^|\\s)night(?!\\S)/g, '');"
            },
            null
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        webView.setBackgroundColor(ContextCompat.getColor(this, R.color.webview_background))
        applyNightCSSClass()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val TOPIC = "name.boyle.chris.sgtpuzzles.TOPIC"
        private val ALLOWED_TOPICS = Pattern.compile("^[a-z]+$")
        const val ASSETS_PATH = "/assets/"
        const val ASSETS_URL = "https://" + WebViewAssetLoader.DEFAULT_DOMAIN + ASSETS_PATH
        private fun helpPath(lang: String, topic: String?): String {
            return MessageFormat.format("{0}/{1}.html", lang, topic)
        }
    }
}