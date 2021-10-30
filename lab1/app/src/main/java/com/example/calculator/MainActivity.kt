package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator




class MainActivity : AppCompatActivity() {
    private lateinit var inputText : TextView
    private lateinit var resultText : TextView
    private var inputs = mutableListOf<String>("0")
    private var str = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText = findViewById(R.id.textInput)
        resultText = findViewById(R.id.textResult)
    }

    var factorial: Operator = object : Operator("!", 1, true, PRECEDENCE_POWER + 1) {
        override fun apply(vararg args: Double): Double {
            val arg = args[0].toInt()
            require(arg.toDouble() == args[0]) { "Operand for factorial has to be an integer" }
            require(arg >= 0) { "The operand of the factorial can not be less than zero" }
            var result = 1.0
            for (i in 1..arg) {
                result *= i.toDouble()
            }
            return result
        }
    }

    fun onClickDigit(v : View) {
        v as Button
        if (str == "0") {
            inputs.clear()
            str = ""
        }
        var buttonText = v.text.toString()
        str += buttonText
        inputs.add(buttonText)
        inputText.text = str
    }

    fun onClickAC(v : View) {
        str = "0"
        inputs.clear()
        inputs.add("0")
        inputText.text = "0"
        resultText.text = ""
    }

    fun onClickBackspace(v : View) {
        if (str == "0") {
            return
        }
        if (resultText.text.isNotEmpty()) {
            resultText.text = ""
        }
        var lastInput = inputs.last()
        inputs.removeLast()
        if (lastInput == "log10") {
            str = str.dropLast(5)
        }
        else if (lastInput == "log") {
            str = str.dropLast(3)
        }
        else {
            str = str.dropLast(lastInput.length)
        }
        if (str.isEmpty()) {
            str = "0"
            inputs.add("0")
        }
        inputText.text = str
    }

    fun onClickOperation(v : View) {
        v as Button
        if (resultText.text.isNotEmpty()) {
            resultText.text = ""
        }
        var buttonText = v.text.toString()
        var lastInput = inputs.last()

        if ((lastInput == "/") or (lastInput == "*") or (lastInput == "+") or (lastInput == "-")
                or (lastInput == "%") or (lastInput == "^")) {
            inputs.removeLast()
            str = str.dropLast(1)
        }
        inputs.add(buttonText)
        str += buttonText
        inputText.text = str
    }

    fun onClickFunction(v : View) {
        v as Button
        if (resultText.text.isNotEmpty()) {
            resultText.text = ""
        }
        if (str == "0") {
            inputs.clear()
            str = ""
        }
        var buttonText = v.text.toString()
        if (buttonText == "lg") {
            inputs.add("log10(")
        }
        else if (buttonText == "ln") {
            inputs.add("log(")
        }
        else {
            inputs.add("$buttonText(")
        }
        str += "$buttonText("
        inputText.text = str
    }

    fun onClickEqual(v : View) {
        try {
            var expression = ""
            for (elem in inputs) {
                expression += elem
            }
            val ex = ExpressionBuilder(expression).operator(factorial).build()
            var res = ex.evaluate()
            var resString = "%.15f".format(res)
            var pointIndex = resString.findLast { it == ',' }
            if (pointIndex != null) {
                resString = resString.dropLastWhile { it == '0'}
                resString = resString.dropLast(1)
            }
            resultText.text = resString
        }
        catch (e : Exception) {
            resultText.text = "error:${e.message}"
        }
    }
}