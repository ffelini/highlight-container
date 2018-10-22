package vturcan.com.highligthcontainer

import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

private const val INVALID_RESOURCE = -1

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
                val sourceLayoutResId = getResourceId(R.styleable.HighlightContainer_highlight_source_layout, INVALID_RESOURCE)
                initHighlightView(sourceLayoutResId)
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

    private fun initHighlightView(@LayoutRes viewResourceId: Int) {
        if (viewResourceId != INVALID_RESOURCE) {
            removeAllViews()
            highLightView = View.inflate(context, viewResourceId, null).also { addView(it) }
        }
    }

    fun highlightView(source: View, @LayoutRes sourceLayoutResId: Int = INVALID_RESOURCE) {
        initHighlightView(sourceLayoutResId)

        this.source = source
        val sourceScreenRect = source.getGlobalVisibleRect()
        highLightView?.apply {
            this.layoutParams = LayoutParams(sourceScreenRect.width(), sourceScreenRect.height())
            this.x = sourceScreenRect.left.toFloat().minus(containerGlobalRect?.left ?: 0)
            this.y = sourceScreenRect.top.toFloat().minus(containerGlobalRect?.top ?: 0)
            requestLayout()

        }
        visibility = View.VISIBLE
    }

    private fun View.getGlobalVisibleRect(): Rect = Rect().also { this.getGlobalVisibleRect(it) }
}
