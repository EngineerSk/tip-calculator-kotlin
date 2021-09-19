package com.engineersk.kotlincalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.engineersk.kotlincalculator.viewmodels.BigDecimalViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class MainActivity : AppCompatActivity() {
    private lateinit var mResultTextInputEditText: TextInputEditText
    private lateinit var mNewNumberInputEditText: TextInputEditText
    private val mDisplayOperation: MaterialTextView by lazy(LazyThreadSafetyMode.NONE) {
        findViewById(
            R.id.operatorTextView
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mResultTextInputEditText = findViewById(R.id.resultInputEditText)
        mNewNumberInputEditText = findViewById(R.id.rightOperandInputEditText)
        val viewModel = ViewModelProvider(this).get(BigDecimalViewModel::class.java)

        viewModel.stringResult.observe(this, { stringResult ->
            mResultTextInputEditText.setText(stringResult)
        })

        viewModel.stringNewNumber.observe(this, { stringNumber ->
            mNewNumberInputEditText.setText(stringNumber)
        })

        viewModel.stringDisplayOperation.observe(this, { stringOperation ->
            mDisplayOperation.text = stringOperation
        })
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
        val buttonNeg = findViewById<MaterialButton>(R.id.negButton)
        val buttonClear = findViewById<MaterialButton>(R.id.clearButton)

        val listener = View.OnClickListener { view ->
            viewModel.digitPressed((view as MaterialButton).text.toString())
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
        buttonNeg.setOnClickListener {
            viewModel.negPressed()
        }

        buttonClear.setOnClickListener {
            viewModel.clearPressed()
        }

        val opListener = View.OnClickListener { view ->
            viewModel.operandPressed((view as MaterialButton).text.toString())
        }

        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)
    }
}