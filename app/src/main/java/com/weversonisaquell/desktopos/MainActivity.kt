package com.weversonisaquell.desktopos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var cursor: ImageView

    private var cursorX = 400f
    private var cursorY = 300f
    private var touchStartX = 0f
    private var touchStartY = 0f
    private var touchStartTime = 0L
    private var moved = false

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        cursor = findViewById(R.id.cursor)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.loadUrl("file:///android_asset/index.html")

        cursor.x = cursorX
        cursor.y = cursorY

        webView.setOnTouchListener { _, event ->
            handleTrackpad(event)
            true
        }
    }

    private fun handleTrackpad(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = event.x
                touchStartY = event.y
                touchStartTime = System.currentTimeMillis()
                moved = false
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - touchStartX
                val dy = event.y - touchStartY
                if (Math.abs(dx) > 4 || Math.abs(dy) > 4) {
                    moved = true
                }
                cursorX += dx
                cursorY += dy

                cursorX = cursorX.coerceIn(0f, webView.width.toFloat() - cursor.width)
                cursorY = cursorY.coerceIn(0f, webView.height.toFloat() - cursor.height)

                cursor.x = cursorX
                cursor.y = cursorY

                touchStartX = event.x
                touchStartY = event.y
            }
            MotionEvent.ACTION_UP -> {
                val duration = System.currentTimeMillis() - touchStartTime
                if (!moved) {
