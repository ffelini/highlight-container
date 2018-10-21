package vturcan.com.highlightcontainer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import highlightcontainer.vturcan.com.highlight_container.R
import vturcan.com.highligthcontainer.HighligthContainer

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsHighlightContainer = findViewById<HighligthContainer>(R.id.settings_highlight_container)
        val emailHighlightContainer = findViewById<HighligthContainer>(R.id.email_highlight_container)
        val settingsIcon = findViewById<View>(R.id.settings_icon).apply {
            setOnClickListener { settingsHighlightContainer.highlightView(this) }
        }
        val emailIcon = findViewById<View>(R.id.email_icon).apply {
            setOnClickListener { emailHighlightContainer.highlightView(this) }
        }
    }

}
