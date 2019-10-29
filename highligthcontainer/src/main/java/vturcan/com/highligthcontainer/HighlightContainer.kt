package vturcan.com.highligthcontainer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView

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
            highLightView = View.inflate(context, viewResourceId, null)
        }
    }

    private fun initHighlightView(source: View) {
        highLightView = ImageView(context).apply {
            setImageBitmap(source.toBitmap())
        }
    }

    fun highlightView(source: View, @LayoutRes sourceLayoutResId: Int = INVALID_RESOURCE) {
        removeAllViews()
        when (sourceLayoutResId) {
            INVALID_RESOURCE -> initHighlightView(source)
            else -> initHighlightView(sourceLayoutResId)
        }

        this.source = source
        val sourceScreenRect = source.getGlobalVisibleRect()
        highLightView?.let { addView(it) }
        highLightView?.apply {
            this.layoutParams = LayoutParams(sourceScreenRect.width(), sourceScreenRect.height())
            this.x = sourceScreenRect.left.toFloat().minus(containerGlobalRect?.left ?: 0)
            this.y = sourceScreenRect.top.toFloat().minus(containerGlobalRect?.top ?: 0)
            requestLayout()

        }
        visibility = View.VISIBLE
    }

    private fun View.getGlobalVisibleRect(): Rect = Rect().also { this.getGlobalVisibleRect(it) }

    private fun View.toBitmap(): Bitmap {
        val b = Bitmap.createBitmap(
                this.layoutParams.width,
                this.layoutParams.height,
                Bitmap.Config.ARGB_8888
        )
        val c = Canvas(b)
        layout(this.left, this.top, this.right, this.bottom)
        this.draw(c)
        return b
    }
}
