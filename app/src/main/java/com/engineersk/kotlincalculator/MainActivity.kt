package com.engineersk.kotlincalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

private const val STATE_PENDING_OPERATION = "PendingOperation"
private const val STATE_OPERAND1 = "Operand1"
private const val STATE_OPERAND1_STORED = "Operand1_Stored"

class MainActivity : AppCompatActivity() {
    private lateinit var mResultTextInputEditText: TextInputEditText
    private lateinit var mNewNumberInputEditText: TextInputEditText
    private val mDisplayOperation: MaterialTextView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(
            R.id.operatorTextView
        )
    }

    // variables to hold the operands and type of calculations
    private var mOperand1: Double? = null
    private var mOperand2: Double = 0.0
    private var pendingOperator: String = "="
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mResultTextInputEditText = findViewById(R.id.resultInputEditText)
        mNewNumberInputEditText = findViewById(R.id.rightOperandInputEditText)

        // Data input buttons
        val button0: MaterialButton = findViewById(R.id.button0)
        val button1: MaterialButton = findViewById(R.id.button1)
        val button2: MaterialButton = findViewById(R.id.button2)
        val button3: MaterialButton = findViewById(R.id.button3)
        val button4: MaterialButton = findViewById(R.id.button4)
        val button5: MaterialButton = findViewById(R.id.button5)
        val button6: MaterialButton = findViewById(R.id.button6)
        val button7: MaterialButton = findViewById(R.id.button7)
        val button8: MaterialButton = findViewById(R.id.button8)
        val button9: MaterialButton = findViewById(R.id.button9)
        val buttonDot: MaterialButton = findViewById(R.id.buttonPoint)

        // operation buttons
        val buttonEquals = findViewById<MaterialButton>(R.id.buttonEquals)
        val buttonPlus = findViewById<MaterialButton>(R.id.buttonPlus)
        val buttonMinus = findViewById<MaterialButton>(R.id.buttonMinus)
        val buttonMultiply = findViewById<MaterialButton>(R.id.buttonMultiply)
        val buttonDivide = findViewById<MaterialButton>(R.id.buttonDivide)

        val listener = View.OnClickListener { view ->
            val button: MaterialButton = view as MaterialButton
            mNewNumberInputEditText.append(button.text)
        }

        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { view ->
            val button: MaterialButton = view as MaterialButton
            val operator = button.text.toString()

            try {
                val value = mNewNumberInputEditText.text.toString().toDouble()
                performOperation(operator, value)
            } catch (e: NumberFormatException) {
                mNewNumberInputEditText.setText("")
            }
            pendingOperator = operator
            mDisplayOperation.text = pendingOperator
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
    }

    private fun performOperation(operator: String, value: Double) {

        mDisplayOperation.text = operator
        if (mOperand1 == null) {
            mOperand1 = value
        } else {
            mOperand2 = value
            if (pendingOperator == "=") {
                pendingOperator = operator
            }
            mOperand1 = when (pendingOperator) {
                "=" -> mOperand2
                "/" -> {
                    if (mOperand2 == 0.0) {
                        Double.NaN
                    } else {
                        mOperand1!! / mOperand2
                    }
                }
                "*" -> mOperand1!! * mOperand2
                "+" -> mOperand1!! + mOperand2
                else -> mOperand1!! - mOperand2
            }
        }

        mResultTextInputEditText.setText(mOperand1.toString())
        mNewNumberInputEditText.setText("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (mOperand1 != null) {
            outState.putDouble(STATE_OPERAND1, mOperand1!!)
            outState.putBoolean(STATE_OPERAND1_STORED, true)
        }

        outState.putString(STATE_PENDING_OPERATION, pendingOperator)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mOperand1 = if (savedInstanceState.getBoolean(STATE_OPERAND1_STORED, false))
            savedInstanceState.getDouble(STATE_OPERAND1)
        else
            null
        pendingOperator = savedInstanceState.getString(STATE_PENDING_OPERATION)!!
    }
}