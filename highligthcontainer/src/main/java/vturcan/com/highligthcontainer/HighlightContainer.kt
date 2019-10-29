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

private const val NO_RESOURCE = -1

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
                refresh()
            }
            if (hideContainer) {
                visibility = View.GONE
            }
        }
        setOnClickListener { visibility = View.GONE }
    }

    private fun initHighlightView(@LayoutRes viewResourceId: Int): View {
        return View.inflate(context, viewResourceId, null)
    }

    private fun initHighlightView(source: View): ImageView {
        return ImageView(context).apply {
            setImageBitmap(source.toBitmap())
        }
    }

    fun highlightView(source: View, @LayoutRes sourceLayoutResId: Int = NO_RESOURCE) {
        removeAllViews()
        highLightView = when (sourceLayoutResId) {
            NO_RESOURCE -> initHighlightView(source)
            else -> initHighlightView(sourceLayoutResId)
        }
        addView(highLightView)
        refresh()

        this.source = source
        visibility = View.VISIBLE
    }

    private fun refresh() {
        source?.let {
            val sourceScreenRect = it.getGlobalVisibleRect()
            highLightView?.apply {
                this.layoutParams = LayoutParams(sourceScreenRect.width(), sourceScreenRect.height())
                this.x = sourceScreenRect.left.toFloat().minus(containerGlobalRect?.left ?: 0)
                this.y = sourceScreenRect.top.toFloat().minus(containerGlobalRect?.top ?: 0)
                requestLayout()
            }
        }
    }

    private fun View.getGlobalVisibleRect(): Rect = Rect().also { this.getGlobalVisibleRect(it) }

    private fun View.toBitmap(): Bitmap {
        val b = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        layout(this.left, this.top, this.right, this.bottom)
        this.draw(c)
        return b
    }
}
