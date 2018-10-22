package vturcan.com.highligthcontainer

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class HighlightContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var source: View? = null
    private var highLightView: View? = null
    private var containerGlobalRect: Rect? = null

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.HighlightContainer, 0, 0).apply {
            try {
                val sourceLayoutResId = getResourceId(R.styleable.HighlightContainer_highlight_source_layout, 0)
                highLightView = View.inflate(context, sourceLayoutResId, null).also { addView(it) }
            } finally {
                recycle()
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener {
            val hideContainer = containerGlobalRect == null
            containerGlobalRect = this@HighlightContainer.getGlobalVisibleRect()
            if (visibility == View.VISIBLE) {
                source?.let { highlightView(it) }
            }
            if (hideContainer) {
                visibility = View.GONE
            }
        }
        setOnClickListener { visibility = View.GONE }
    }

    fun highlightView(source: View) {
        this.source = source
        val anchor = source.getGlobalVisibleRect()
        highLightView?.apply {
            this.layoutParams = LayoutParams(anchor.width(), anchor.height())
            this.x = anchor.left.toFloat().minus(containerGlobalRect?.left ?: 0)
            this.y = anchor.top.toFloat().minus(containerGlobalRect?.top ?: 0)
            requestLayout()

        }
        visibility = View.VISIBLE
    }

    private fun View.getGlobalVisibleRect(): Rect = Rect().also { this.getGlobalVisibleRect(it) }
}
