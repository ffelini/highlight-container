package vturcan.com.highligthcontainer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

class HighligthContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.HighligthContainer, 0, 0).apply {
            try {
                val headerLayout = getResourceId(R.styleable.HighligthContainer_highlight_source_layout, 0)
                View.inflate(context, headerLayout, this@HighligthContainer)
            } finally {
                recycle()
            }
        }
    }
}
