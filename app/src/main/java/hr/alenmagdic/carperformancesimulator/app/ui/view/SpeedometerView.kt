package hr.alenmagdic.carperformancesimulator.app.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet

class SpeedometerView(context: Context, attributeSet: AttributeSet) :
    AnalogMeterView(context, attributeSet) {

    init {
        valueMarkColor = Color.BLACK
        labelFontSizeFactor = LABEL_FONT_SIZE_FACTOR
        labeledMarksStep = LABELED_MARKS_STEP
    }

    companion object {
        private const val LABEL_FONT_SIZE_FACTOR = 0.06f
        private const val LABELED_MARKS_STEP = 2
    }
}