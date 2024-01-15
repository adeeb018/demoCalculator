package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calc)

        initializeViews()

        setupNumberButtons()
        setupOperationButtons()
        setupClearButton()
        setupBackspaceButton()
        setupEqualsButton()
    }

    private fun initializeViews() {
        textView = findViewById(R.id.textView)
//        textView.isCursorVisible = true
    }

    @SuppressLint("SetTextI18n")
    private fun setupNumberButtons() {
        val numberButtonIds = arrayOf(
            R.id.button1, R.id.button2, R.id.button3,
            R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9,
            R.id.button0, R.id.button00, R.id.buttondo
        )

        for (buttonId in numberButtonIds) {
            val button = findViewById<Button>(buttonId)

            button.setOnClickListener {
                val stringValue: String = textView.text.toString()
                val buttonText = (it as Button).text.toString()
                textView.text = stringValue + buttonText
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupOperationButtons() {
        val operationButtonIds = arrayOf(
            R.id.buttonpl, R.id.buttonmi, R.id.buttonmu,
            R.id.buttondiv, R.id.buttonmo
        )

        for (buttonId in operationButtonIds) {
            val button = findViewById<Button>(buttonId)

            button.setOnClickListener {
                var stringValue = textView.text.toString()

                if (stringValue.isEmpty()) {
                    // Prevent adding operation as the first character
                    return@setOnClickListener
                }

                val buttonText = (it as Button).text.toString()

                if (stringValue.last().toString() == buttonText) {
                    // Same operation cannot be repeated twice
                    Toast.makeText(this, "Same operation", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // If text view contains any unsolved quantities, solve it first
                if (containsOperator(stringValue)) {
                    textView.text = equate(stringValue)
                    stringValue = textView.text.toString()
                }

                textView.text = stringValue + buttonText
            }
        }
    }

    private fun setupClearButton() {
        val buttonAC = findViewById<Button>(R.id.buttonAC)

        buttonAC.setOnClickListener {
            textView.text = ""
        }
    }

    private fun setupBackspaceButton() {
        val buttonBA = findViewById<Button>(R.id.buttonba)

        buttonBA.setOnClickListener {
            val stringValue = textView.text.toString()
            if (stringValue.isNotEmpty()) {
                textView.text = stringValue.dropLast(1)
            }
        }
    }

    private fun setupEqualsButton() {
        val buttonEq = findViewById<Button>(R.id.buttoneq)

        buttonEq.setOnClickListener {
            val stringVal = textView.text.toString()
            textView.text = equate(stringVal)
        }
    }

    private fun containsOperator(input: String): Boolean {
        return input.contains('+') || input.contains('-') ||
                input.contains('*') || input.contains('/') || input.contains('%')
    }

    private fun equate(input: String): String {

        val regex = "([0-9]+(?:\\.[0-9]+)?|[+\\-*/%])".toRegex()

        val matches = regex.findAll(input)

        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        for (match in matches) {
            val value = match.value
            if (value.matches(Regex("[0-9]+(?:\\.[0-9]+)?"))) {
                numbers.add(value.toDouble())
            } else {
                operators.add(value)
            }
        }

        //retrieve first number of array if array is not empty
        var result = numbers.firstOrNull() ?: 0.0

        for ((index, operator) in operators.withIndex()) {
            //retrieve next number if the index is not outOfbound
            val nextNumber = numbers.getOrElse(index + 1) { 0.0 }
            when (operator) {
                "+" -> result += nextNumber
                "-" -> result -= nextNumber
                "*" -> result *= nextNumber
                "/" -> result /= nextNumber
                "%" -> result %= nextNumber
            }
        }
        return result.toString()

    }
}