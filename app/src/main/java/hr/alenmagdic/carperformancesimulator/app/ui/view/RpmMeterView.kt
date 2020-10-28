package hr.alenmagdic.carperformancesimulator.app.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import kotlin.math.roundToInt

class RpmMeterView(context: Context, attributeSet: AttributeSet) : AnalogMeterView(context, attributeSet) {
    var criticallyHighRpmThreshold = DEFAULT_CRITICALLY_HIGH_RPM_THRESHOLD
        set(value) {
            field = value
            invalidate()
        }
    var rpmLabelMultiplicator =
        DEFAULT_RPM_LABEL_MULTIPLICATOR // actual value represented by a label is the label value multiplied by this multiplicator
        set(value) {
            field = value
            invalidate()
        }

    init {
        markLineLengthFactor = MARK_LINE_LENGTH_FACTOR
        segmentsNumber = DEFAULT_SEGMENTS_NUMBER
        minValue = MIN_RPM
        maxValue = DEFAULT_MAX_RPM
        labelFontSizeFactor = LABEL_FONT_SIZE_FACTOR
        labeledMarksStep = DEFAULT_LABELED_MARKS_STEP
    }

    override fun generateMarks(): List<Mark> {
        val marks = super.generateMarks()

        return marks.map {
            val markColor = if (it.representingValue < criticallyHighRpmThreshold) {
                it.color
            } else {
                CRITICALLY_HIGH_RPM_MARK_COLOR
            }
            val label = if (it.label != null) {
                (it.representingValue / rpmLabelMultiplicator).roundToInt().toString()
            } else {
                null
            }

            Mark(
                it.angle,
                markColor,
                it.thicknessFactor,
                it.lengthFactor,
                label,
                it.representingValue
            )
        }
    }

    companion object {
        const val MARK_LINE_LENGTH_FACTOR = 0.08f
        const val LABEL_FONT_SIZE_FACTOR = 0.09f
        const val DEFAULT_RPM_LABEL_MULTIPLICATOR = 100
        const val DEFAULT_CRITICALLY_HIGH_RPM_THRESHOLD = 6600
        const val DEFAULT_SEGMENTS_NUMBER = 40
        const val DEFAULT_MAX_RPM = 8000
        const val MIN_RPM = 0
        const val DEFAULT_LABELED_MARKS_STEP = 5
        const val CRITICALLY_HIGH_RPM_MARK_COLOR = Color.RED
    }
}