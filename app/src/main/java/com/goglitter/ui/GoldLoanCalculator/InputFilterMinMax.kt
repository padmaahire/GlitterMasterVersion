package com.goglitter.ui.GoldLoanCalculator

import android.text.InputFilter
import android.text.Spanned

class InputFilterMinMax(private val minValue: Int, private val maxValue: Int) : InputFilter {

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = (dest.toString() + source.toString()).toInt()
            if (isInRange(minValue, maxValue, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            // Handle the exception if necessary
        }
        return ""
    }

    private fun isInRange(min: Int, max: Int, value: Int): Boolean {
        return value in min..max
    }
}