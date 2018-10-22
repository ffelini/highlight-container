package vturcan.com.highlightcontainer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.BounceInterpolator
import highlightcontainer.vturcan.com.highlight_container.R
import vturcan.com.highligthcontainer.HighlightContainer

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsHighlightContainer = findViewById<HighlightContainer>(R.id.settings_highlight_container)
        val emailHighlightContainer = findViewById<HighlightContainer>(R.id.email_highlight_container)
        val asHighlightContainer = findViewById<HighlightContainer>(R.id.as_highlight_container)
        val settingsIcon = findViewById<View>(R.id.settings_icon).apply {
            setOnClickListener { settingsHighlightContainer.highlightView(this) }
        }
        val animatedAsIcon = findViewById<View>(R.id.animated_as_icon).apply {
            setOnClickListener {
                asHighlightContainer.highlightView(this)
                this
                        .animate()
                        .scaleX(2f)
                        .scaleY(2f)
                        .setInterpolator(BounceInterpolator())
                        .setDuration(3000)
                        .start()
            }
        }
        val emailIcon = findViewById<View>(R.id.email_icon).apply {
            setOnClickListener { emailHighlightContainer.highlightView(this) }
        }
    }

}
