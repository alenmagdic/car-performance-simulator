package hr.alenmagdic.carperformancesimulator.app.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class VerticalSliderView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val paint = Paint()
    private val strokeRect = Rect()
    private val fillRect = Rect()
    private val maxValueRect = Rect()
    private var input = 0.0
        set(value) {
            field = value
            listener?.onSliderChanged(value, this)
        }
    private var listener: SliderChangeListener? = null

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        drawFill(canvas)
        drawMaxValueSpace(canvas)
        drawStroke(canvas)
    }

    private fun drawFill(canvas: Canvas?) {
        paint.color = FILL_COLOR
        paint.style = Paint.Style.FILL

        val maxValueY = height * MAX_VALUE_RESERVED_SPACE
        val spaceForSliderHeight = height * (1 - MAX_VALUE_RESERVED_SPACE)
        fillRect.left = 0
        fillRect.top = (maxValueY + spaceForSliderHeight * (1 - input / MAX_INPUT)).toInt()
        fillRect.right = width
        fillRect.bottom = height
        canvas?.drawRect(fillRect, paint)
    }

    private fun drawMaxValueSpace(canvas: Canvas?) {
        paint.style = Paint.Style.FILL
        paint.color = MAX_VALUE_SPACE_COLOR

        maxValueRect.left = 0
        maxValueRect.top = 0
        maxValueRect.right = width
        maxValueRect.bottom = (height * MAX_VALUE_RESERVED_SPACE).toInt()
        canvas?.drawRect(maxValueRect, paint)
    }

    private fun drawStroke(canvas: Canvas?) {
        paint.color = STROKE_COLOR
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = STROKE_WIDTH_DP * context.resources.displayMetrics.density

        strokeRect.left = 0
        strokeRect.top = 0
        strokeRect.right = width
        strokeRect.bottom = height
        canvas?.drawRect(strokeRect, paint)
    }

    fun setSliderChangeListener(listener: SliderChangeListener) {
        this.listener = listener
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN || event?.action == MotionEvent.ACTION_MOVE) {
            input = if (event.x > 0 && event.x < width && event.y > 0 && event.y < height) {
                when {
                    event.y < height * MAX_VALUE_RESERVED_SPACE -> MAX_INPUT
                    else -> {
                        val maxValueY = height * MAX_VALUE_RESERVED_SPACE
                        val spaceForSliderHeight = height * (1 - MAX_VALUE_RESERVED_SPACE)

                        (spaceForSliderHeight - event.y + maxValueY) / spaceForSliderHeight * MAX_INPUT
                    }
                }
            } else {
                0.0
            }
            invalidate()
            return true
        }

        if (event?.action == MotionEvent.ACTION_UP) {
            input = 0.0
            invalidate()
            return true
        }

        return super.onTouchEvent(event)
    }

    companion object {
        const val MAX_INPUT = 100.0
        const val MAX_VALUE_RESERVED_SPACE = 0.21
        const val STROKE_WIDTH_DP = 4
        const val STROKE_COLOR = Color.BLACK
        val MAX_VALUE_SPACE_COLOR = Color.rgb(40, 40, 40)
        val FILL_COLOR = Color.rgb(190, 0, 0)
    }

    interface SliderChangeListener {
        fun onSliderChanged(value: Double, slider: VerticalSliderView)
    }
}