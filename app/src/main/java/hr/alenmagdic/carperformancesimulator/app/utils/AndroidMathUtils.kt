package hr.alenmagdic.carperformancesimulator.app.utils

import android.graphics.PointF
import kotlin.math.sqrt
import kotlin.math.pow

fun PointF.distanceTo(x: Int, y: Int): Double =
    sqrt((this.x - x.toDouble()).pow(2.0) + (this.y - y.toDouble()).pow(2.0))