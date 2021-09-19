package com.engineersk.kotlincalculator.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import kotlin.Double.Companion.NaN

class BigDecimalViewModel : ViewModel() {
    // variables to hold the operands and type of calculations
    private var mOperand1: BigDecimal? = null
    private var pendingOperator: String = "="
    private val mResult = MutableLiveData<BigDecimal>()
    val stringResult: LiveData<String>
        get() {
            return Transformations.map(mResult) { it.toString() }
        }
    private val mNewNumber = MutableLiveData<String>()
    val stringNewNumber: LiveData<String> get() = mNewNumber

    private val mDisplayOperation = MutableLiveData<String>()
    val stringDisplayOperation: LiveData<String>
        get() {
            return mDisplayOperation
        }

    fun digitPressed(caption: String) {
        if (mNewNumber.value != null) {
            mNewNumber.value = mNewNumber.value + caption
        } else {
            mNewNumber.value = caption
        }
    }

    fun operandPressed(operand: String) {
        try {
            val value = mNewNumber.value?.toBigDecimal()
            if (value != null) {
                performOperation(operand, value)
            }
        } catch (e: NumberFormatException) {
            mNewNumber.value = ""
        }
        pendingOperator = operand
        mDisplayOperation.value = pendingOperator
    }

    fun negPressed() {
        val value = mNewNumber.value
        if (value == null || value.isEmpty())
            mNewNumber.value = "-"
        else {
            try {
                var doubleValue = value.toBigDecimal()
                doubleValue *= BigDecimal.valueOf(-1)
                mNewNumber.value = doubleValue.toString()
            } catch (e: NumberFormatException) {
                mNewNumber.value = ""
            }
        }
    }

    private fun performOperation(operator: String, value: BigDecimal) {
        if (mOperand1 == null) {
            mOperand1 = value
        } else {
            if (pendingOperator == "=") {
                pendingOperator = operator
            }
            mOperand1 = when (pendingOperator) {
                "=" -> value
                "/" -> {
                    if ((BigDecimal.valueOf(0.0) == value) || (BigDecimal.valueOf(-0.0) == value)) {
                        BigDecimal.valueOf(NaN)
                    } else {
                        mOperand1!! / value
                    }
                }
                "*" -> mOperand1!! * value
                "+" -> mOperand1!! + value
                else -> mOperand1!! - value
            }
        }

        mResult.value = mOperand1!!
        mNewNumber.value = ""
    }

    fun clearPressed() {
        mResult.value = BigDecimal.valueOf(0.0)
        mNewNumber.value = ""
        mDisplayOperation.value = "="
        pendingOperator = "="
        mOperand1 = BigDecimal.valueOf(0.0)
    }
}