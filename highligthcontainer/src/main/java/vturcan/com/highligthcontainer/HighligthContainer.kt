package vturcan.com.highligthcontainer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class HighligthContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var source: View
    private lateinit var highLightView: View

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.HighligthContainer, 0, 0).apply {
            try {
                val headerLayout = getResourceId(R.styleable.HighligthContainer_highlight_source_layout, 0)
                highLightView = View.inflate(context, headerLayout, this@HighligthContainer)
            } finally {
                recycle()
            }
        }
        visibility = View.GONE

        setOnClickListener { visibility = View.GONE }
    }

    fun highlightView(source: View) {
        this.source = source
        highLightView.apply {
            layoutParams = source.layoutParams
            x = source.x
            y = source.y
        }

        visibility = View.VISIBLE
    }
}
