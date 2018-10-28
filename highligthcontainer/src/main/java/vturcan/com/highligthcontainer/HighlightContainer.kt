package vturcan.com.highligthcontainer

import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

private const val INVALID_RESOURCE = -1

class HighlightContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var onHighLightUpdate: ((HightLightView) -> Unit)? = null

    private var source: View? = null
    private var highLightView: View? = null
    private var containerGlobalRect: Rect? = null

    private val onGlobalLayoutChange = ViewTreeObserver.OnGlobalLayoutListener {
        refreshHighLightingView()
    }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.HighlightContainer, 0, 0).apply {
            try {
                val sourceLayoutResId = getResourceId(R.styleable.HighlightContainer_highlight_source_layout, INVALID_RESOURCE)
                initHighlightView(sourceLayoutResId)
            } finally {
                recycle()
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

    private fun refreshHighLightingView() {
        source?.let { highlightView(it) }
    }

    fun highlightView(
            source: View,
            @LayoutRes sourceLayoutResId: Int = INVALID_RESOURCE
    ) {
        initHighlightView(sourceLayoutResId)
        visibility = View.VISIBLE

        this.source = source
        this.containerGlobalRect = this.getGlobalVisibleRect()
        val sourceGlobalRect = source.getGlobalVisibleRect()
        highLightView?.apply {
            this.layoutParams = LayoutParams(sourceGlobalRect.width(), sourceGlobalRect.height())
            this.x = sourceGlobalRect.left.toFloat().minus(containerGlobalRect?.left ?: 0)
            this.y = sourceGlobalRect.top.toFloat().minus(containerGlobalRect?.top ?: 0)
            requestLayout()
            onHighLightUpdate?.invoke(HightLightView(this, sourceGlobalRect))
        }
    }

    override fun setVisibility(visibility: Int) {
        val oldVisibility = this.visibility
        super.setVisibility(visibility)
        if (visibility != oldVisibility) {
            if (visibility == View.VISIBLE) {
                viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutChange)
                refreshHighLightingView()
            } else {
                viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutChange)
            }
        }
    }

    private fun View.getGlobalVisibleRect(): Rect = Rect().also { this.getGlobalVisibleRect(it) }
}
