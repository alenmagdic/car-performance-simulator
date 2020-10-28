package hr.alenmagdic.carperformancesimulator.app.ui.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import hr.alenmagdic.carperformancesimulator.app.utils.distanceTo
import hr.alenmagdic.carperformancesimulator.domain.toDegrees
import hr.alenmagdic.carperformancesimulator.domain.solveQuadraticEquation
import kotlin.math.*

open class AnalogMeterView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var value = 0.0
        set(value) {
            field = value
            invalidate()
        }

    var minValue = DEFAULT_MIN_VALUE
        set(value) {
            field = value
            invalidate()
        }

    var maxValue = DEFAULT_MAX_VALUE
        set(value) {
            field = value
            invalidate()
        }

    var segmentsNumber = DEFAULT_SEGMENTS_NUMBER
        set(value) {
            field = value
            invalidate()
        }

    var labeledMarksStep = DEFAULT_LABELED_MARKS_STEP
        set(value) {
            field = value
            invalidate()
        }

    var valueMarkColor = DEFAULT_VALUE_MARK_COLOR
        set(value) {
            field = value
            invalidate()
        }

    private var minValueAngle = DEFAULT_MIN_VALUE_ANGLE
    private var maxValueAngle = DEFAULT_MAX_VALUE_ANGLE
    private var markLineWidthFactor = DEFAULT_MARK_LINE_WIDTH_FACTOR
    protected var markLineLengthFactor = DEFAULT_MARK_LINE_LENGTH_FACTOR
    private var needleColor = DEFAULT_NEEDLE_COLOR
    protected var labelFontSizeFactor = DEFAULT_LABEL_FONT_SIZE_FACTOR
    private var markOuterPointToLabelCenterDistanceFactor =
        DEFAULT_MARK_OUTER_POINT_TO_LABEL_DISTANCE_FACTOR

    private var paint = Paint().apply {
        isAntiAlias = true
    }

    private val angleBetweenMinAndMaxValue: Double
        get() = if (maxValueAngle >= minValueAngle) {
            FULL_ANGLE - (maxValueAngle - minValueAngle)
        } else {
            minValueAngle - maxValueAngle
        }

    private val centerX: Int
        get() = width / 2

    private val centerY: Int
        get() = height / 2

    private val radius: Int
        get() = (min(width, height) * RADIUS_FACTOR).toInt()

    private var marks: List<Mark>? = null

    override fun invalidate() {
        marks = generateMarks()
        super.invalidate()
    }

    protected open fun generateMarks(): List<Mark> {
        val marks = mutableListOf<Mark>()
        val angleBetweenSegments = angleBetweenMinAndMaxValue / segmentsNumber
        val valueDiffBetweenSegments = (maxValue - minValue) / segmentsNumber.toDouble()

        for (i in 0..segmentsNumber) {
            val angle = minValueAngle - i * angleBetweenSegments
            val value = minValue + i * valueDiffBetweenSegments

            var thicknessFactor = markLineWidthFactor
            var label: String? = null
            if (i % labeledMarksStep == 0) {
                thicknessFactor = markLineWidthFactor * BOLD_MARK_LINE_WIDTH_FACTOR
                label = value.roundToInt().toString()
            }

            val mark = Mark(
                angle,
                valueMarkColor,
                thicknessFactor,
                markLineLengthFactor,
                label,
                value
            )
            marks.add(mark)
        }

        return marks
    }

    override fun onDraw(canvas: Canvas?) {
        marks?.forEach {
            drawMarkAtAngle(it, canvas)
        }

        drawNeedle(canvas)
    }

    private fun drawNeedle(canvas: Canvas?) {
        val needlePath = generateNeedlePath()

        paint.color = needleColor
        paint.style = Paint.Style.FILL
        canvas?.drawPath(needlePath, paint)

        paint.color = NEEDLE_ORIGIN_CIRCLE_COLOR
        canvas?.drawCircle(
            centerX.toFloat(), centerY.toFloat(),
            NEEDLE_ORIGIN_CIRCLE_RADIUS_FACTOR * radius, paint
        )
    }

    private fun generateNeedlePath(): Path {
        val originPointX = NEEDLE_ORIGIN_POINT_X_FACTOR * radius
        val originPoint1Y = NEEDLE_ORIGIN_POINT_Y_FACTOR * radius
        val originPoint2 = PointF(originPointX, -originPoint1Y)
        val apexPointX = NEEDLE_APEX_POINT_X_FACTOR * radius
        val apexPoint1Y = NEEDLE_APEX_POINT_Y_FACTOR * radius
        val apexPoint2 = PointF(apexPointX, -apexPoint1Y)

        val path = Path()
        path.setLastPoint(originPointX, originPoint1Y)
        path.lineTo(originPointX, originPoint2.y)
        path.lineTo(apexPointX, apexPoint2.y)
        path.lineTo(apexPointX, apexPoint1Y)

        val rotateMatrix = Matrix().apply { setRotate(-getAngleForValue(value).toDegrees().toFloat()) }
        path.transform(rotateMatrix)

        val translateMatrix = Matrix().apply { setTranslate(centerX.toFloat(), centerY.toFloat()) }
        path.transform(translateMatrix)

        return path
    }

    private fun getAngleForValue(value: Double) =
        minValueAngle - value / (maxValue - minValue) * angleBetweenMinAndMaxValue

    private fun calculateMarkLineStartAndEndPoint(
        angle: Double,
        lengthFactor: Float
    ): Pair<PointF, PointF> {
        val centerY = -centerY // adjusting y coordinate value to cartesian coordinate system to make it easier to do calculations using standard math formulas

        val markLineOuterPoint = PointF().apply {
            x = (centerX + cos(angle) * radius).toFloat()
            y = (centerY + sin(angle) * radius).toFloat()
        }
        markLineOuterPoint.y *= -1

        val markLineInnerPoint =
            getPointBetweenPointOnCircleAndCenter(markLineOuterPoint, lengthFactor * width)

        return markLineInnerPoint to markLineOuterPoint
    }

    private fun drawMarkAtAngle(mark: Mark, canvas: Canvas?) {
        val (markLineInnerPoint, markLineOuterPoint) = calculateMarkLineStartAndEndPoint(
            mark.angle,
            mark.lengthFactor
        )
        paint.color = mark.color
        paint.strokeWidth = mark.thicknessFactor * width
        paint.style = Paint.Style.FILL
        canvas?.drawLine(
            markLineInnerPoint.x,
            markLineInnerPoint.y,
            markLineOuterPoint.x,
            markLineOuterPoint.y,
            paint
        )

        mark.label?.let {
            drawLabelForMarkAtPoint(it, markLineOuterPoint, canvas)
        }
    }

    private fun drawLabelForMarkAtPoint(label: String, markPoint: PointF, canvas: Canvas?) {
        val labelPosition = getPointBetweenPointOnCircleAndCenter(
            markPoint,
            distanceFromPointOnCircle = markOuterPointToLabelCenterDistanceFactor * width
        )
        paint.textSize = labelFontSizeFactor * width
        val labelBounds = Rect().apply {
            paint.getTextBounds(label, 0, label.length, this)
        }

        val horizontalOffset = labelBounds.width() * (labelPosition.x - centerX) / radius.toFloat()
        val verticalOffset = labelBounds.height() * (labelPosition.y - centerY) / radius.toFloat()
        canvas?.drawText(
            label,
            labelPosition.x - labelBounds.width() / 2f - horizontalOffset,
            labelPosition.y + labelBounds.height() / 2f - verticalOffset,
            paint
        )
    }

    private fun getPointBetweenPointOnCircleAndCenter(
        point: PointF,
        distanceFromPointOnCircle: Float
    ): PointF {
        val centerY = -centerY // adjusting y coordinate value to cartesian coordinate system to make it easier to do calculations using standard math formulas
        val pointOnCircle = PointF(point.x, -point.y)

        // y = centerToTargetPointLineSlope*x + centerToTargetLineYIntercept  - linear function representing line that connects center point to the target point (a point defined by method argument)
        val centerToTargetPointLineSlope = (pointOnCircle.y - centerY) / (pointOnCircle.x - centerX).toDouble()
        val centerToTargetLineYIntercept = centerY - centerX * (pointOnCircle.y - centerY) / (pointOnCircle.x - centerX).toDouble()

        val outerY = pointOnCircle.y.toDouble()
        val outerX = pointOnCircle.x.toDouble()

        val resultingPoint = when (centerToTargetPointLineSlope) {
            Double.POSITIVE_INFINITY -> PointF(
                pointOnCircle.x,
                pointOnCircle.y - distanceFromPointOnCircle
            )
            Double.NEGATIVE_INFINITY -> PointF(
                pointOnCircle.x,
                pointOnCircle.y + distanceFromPointOnCircle
            )
            else -> {
                val possibleResultingPointXValues =
                    solveQuadraticEquation(
                        a = 1 + centerToTargetPointLineSlope.pow(2.0),
                        b = 2 * (centerToTargetPointLineSlope * (centerToTargetLineYIntercept - outerY) - outerX),
                        c = outerX.pow(2.0) + (centerToTargetLineYIntercept - outerY).pow(2.0)
                                - distanceFromPointOnCircle.toDouble().pow(2.0)
                    )

                val pointOnCircleToCenterFunction = { x: Double -> centerToTargetPointLineSlope * x + centerToTargetLineYIntercept }
                val possibleResultingPointYValues = Pair(
                    pointOnCircleToCenterFunction(possibleResultingPointXValues.first),
                    pointOnCircleToCenterFunction(possibleResultingPointXValues.second)
                )

                val possibleResultingPoint1 = PointF(
                    possibleResultingPointXValues.first.toFloat(),
                    possibleResultingPointYValues.first.toFloat()
                )
                val possibleResultingPoint2 = PointF(
                    possibleResultingPointXValues.second.toFloat(),
                    possibleResultingPointYValues.second.toFloat()
                )

                if (possibleResultingPoint1.distanceTo(centerX, centerY) < possibleResultingPoint2.distanceTo(centerX, centerY)) {
                    possibleResultingPoint1
                } else {
                    possibleResultingPoint2
                }
            }
        }

        resultingPoint.y *= -1 // adjusting back from cartesian coordinate system to system used by Android UI framework
        return resultingPoint
    }


    companion object {
        private const val FULL_ANGLE = 2 * Math.PI
        private const val DEFAULT_MIN_VALUE_ANGLE = 5.0 / 4 * Math.PI
        private const val DEFAULT_MAX_VALUE_ANGLE = 7.0 / 4 * Math.PI
        private const val DEFAULT_VALUE_MARK_COLOR = Color.BLACK
        private const val DEFAULT_NEEDLE_COLOR = Color.RED
        private const val RADIUS_FACTOR = 0.48 // relative to component width
        private const val DEFAULT_MARK_LINE_WIDTH_FACTOR = 0.01f
        private const val DEFAULT_MARK_LINE_LENGTH_FACTOR = 0.07f
        private const val DEFAULT_SEGMENTS_NUMBER = 10
        private const val DEFAULT_MIN_VALUE = 0
        private const val DEFAULT_MAX_VALUE = 100
        private const val DEFAULT_LABEL_FONT_SIZE_FACTOR = 0.03f
        private const val DEFAULT_MARK_OUTER_POINT_TO_LABEL_DISTANCE_FACTOR = 0.075f
        private const val DEFAULT_LABELED_MARKS_STEP = 2
        private const val BOLD_MARK_LINE_WIDTH_FACTOR = 2.2f // relative to standard mark line width factor
        private const val NEEDLE_ORIGIN_CIRCLE_COLOR = Color.BLACK
        private const val NEEDLE_ORIGIN_CIRCLE_RADIUS_FACTOR = 0.1f // relative to radius of the view
        private const val NEEDLE_ORIGIN_POINT_X_FACTOR = 0f
        private const val NEEDLE_ORIGIN_POINT_Y_FACTOR = 0.05f
        private const val NEEDLE_APEX_POINT_X_FACTOR = 0.9f
        private const val NEEDLE_APEX_POINT_Y_FACTOR = 0.02f
    }

    class Mark(
        val angle: Double,
        val color: Int,
        val thicknessFactor: Float,
        val lengthFactor: Float,
        val label: String?,
        val representingValue: Double
    )
}