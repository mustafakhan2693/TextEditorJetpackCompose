package com.technomapps.texteditorcompose

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.technomapps.texteditorcompose.ui.theme.OnStateChangeListener
import com.technomapps.texteditorcompose.ui.theme.SetTextEditorToolbar
import com.technomapps.texteditorcompose.ui.theme.Type
import com.technomapps.texteditorcompose.ui.theme.handleStateChange
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {

    companion object {
        private const val SETUP_HTML_EDITOR = "file:///android_asset/editor.html"
        private const val CALLBACK_SEPARATOR = "~!~!~!"
        private const val SCRIPT_INTERFACE_NAME = "JSInterface"
    }

    private var isReady = false
    private var webView: WebView? = null
    private var mContents: String = ""
    private var mStateChangeListener: OnStateChangeListener? = null
    private val isBoldEnabled = mutableStateOf(false)
    private val isItalicEnabled = mutableStateOf(false)
    private val isUnderlineEnabled = mutableStateOf(false)
    private val isStrikeThroughEnabled = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = stringResource(id = R.string.heading),
                        modifier = Modifier.fillMaxWidth()
                            .padding(15.dp)
                            .align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                    SetTextEditorToolbar(
                        isBoldEnabled = isBoldEnabled,
                        isItalicEnabled = isItalicEnabled,
                        isUnderlineEnabled = isUnderlineEnabled,
                        isStrikeThroughEnabled = isStrikeThroughEnabled
                    ) { onBoldClick, onItalicClick, onUnderlineClick, onStrikeThroughClick,
                        alignLeft, alignCenter, alignRight ->
                        executeEditorToolbar(
                            onBoldClick,
                            onItalicClick,
                            onUnderlineClick,
                            onStrikeThroughClick,
                            alignLeft,
                            alignCenter,
                            alignRight
                        )
                    }
                    SetEditTextView()
                    Button(
                        onClick = {
                            val htmlString = html.toString()
                            Toast.makeText(this@MainActivity, htmlString, Toast.LENGTH_LONG)
                                .show()
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .align(Alignment.CenterHorizontally),
                    ) {
                        Text(text = stringResource(id = R.string.button_submit))
                    }
                }
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Composable
    private fun SetEditTextView() {
        AndroidView(
            modifier = Modifier
                .padding(8.dp)
                .border(color = Color.Gray, width = 2.dp)
                .padding(8.dp),
            factory = {
                WebView(it).apply {
                    webView = this
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        400
                    )
                    isVerticalScrollBarEnabled = false
                    isHorizontalScrollBarEnabled = false
                    addJavascriptInterface(EditorScriptInterface(), SCRIPT_INTERFACE_NAME)
                    settings.javaScriptEnabled = true
                    setStateChangeListener(
                        object : OnStateChangeListener {
                            override fun onStateChanged(text: String?, types: List<Type>?) {
                                Handler(Looper.getMainLooper()).post {
                                    handleStateChange(
                                        types,
                                        isBoldEnabled,
                                        isItalicEnabled,
                                        isStrikeThroughEnabled,
                                        isUnderlineEnabled
                                    )
                                }
                            }
                        }
                    )
                    loadUrl(SETUP_HTML_EDITOR)
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isReady = url.equals(SETUP_HTML_EDITOR, ignoreCase = true)
                        }

                    }
                }
            },
            update = { webView = it }
        )
    }

    private var html: String?
        get() = mContents
        set(contents) {
            var newContents = contents
            if (contents.isNullOrBlank())
                newContents = ""
            try {
                if (webView != null) {
                    execute(
                        "javascript:customET.setHtml('" + URLEncoder.encode(
                            newContents,
                            "UTF-8"
                        ) + "');"
                    )
                }
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            newContents?.let {
                mContents = it
            }
        }

    private fun executeEditorToolbar(
        bold: Boolean, italic: Boolean, underline: Boolean, strikeThrough: Boolean,
        alignLeft: Boolean, alignCenter: Boolean, alignRight: Boolean
    ) {
        when {
            bold -> execute("javascript:customET.setBold();")
            italic -> execute("javascript:customET.setItalic();")
            underline -> execute("javascript:customET.setUnderline();")
            strikeThrough -> execute("javascript:customET.setStrikeThrough();")
            alignLeft -> execute("javascript:customET.setJustifyLeft();")
            alignCenter -> execute("javascript:customET.setJustifyCenter();")
            alignRight -> execute("javascript:customET.setJustifyRight();")
        }
    }

    inner class EditorScriptInterface {
        @JavascriptInterface
        fun callback(callBackString: String?) {
            if (TextUtils.isEmpty(callBackString))
                return
            try {
                handleCallback(URLDecoder.decode(callBackString, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
        }
    }

    private fun handleCallback(text: String) {
        val stringParts: List<String> = text.split(CALLBACK_SEPARATOR)
        var enabledString = ""
        if (stringParts.isNotEmpty()) {
            if (stringParts.size > 1) {
                enabledString = stringParts[1]
                if (stringParts.size > 2) {
                    if (stringParts.size > 3) {
                        if (stringParts.size > 4) {
                            mContents = stringParts[4]
                        }
                    }
                }
            }
        }
        val types: MutableList<Type> = ArrayList()
        for (type in Type.values()) {
            if (TextUtils.indexOf(enabledString, type.name) != -1) {
                types.add(type)
            }
        }
        mStateChangeListener?.onStateChanged(enabledString, types)
    }

    private fun setStateChangeListener(listener: OnStateChangeListener) {
        mStateChangeListener = listener
    }

    private fun execute(trigger: String) {
        if (isReady) {
            webView?.evaluateJavascript(trigger, null)
        } else {
            Looper.myLooper()?.let {
                Handler(it).postDelayed({ execute(trigger) }, 100)
            }
        }
    }
}