package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator


class MainActivity : AppCompatActivity() {
    private lateinit var inputText: TextView
    private lateinit var resultText: TextView
    private lateinit var scrollView: ScrollView
    private lateinit var sinButton: Button
    private lateinit var cosButton: Button
    private lateinit var tanButton: Button
    private lateinit var rootButton: Button

    private var inputs = mutableListOf<String>("0")
    private val funcs = arrayOf("sin(", "cos(", "tan(", "sinh(", "cosh(", "tanh(", "sqrt(",
        "cbrt(", "log(", "log10(")
    private var str = "0"
    private var bracketCount = 0
    private var isAltButtons = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        inputText = findViewById(R.id.textInput)
        resultText = findViewById(R.id.textResult)
        scrollView = findViewById(R.id.scrollView)
        sinButton = findViewById(R.id.buttonSin)
        cosButton = findViewById(R.id.buttonCos)
        tanButton = findViewById(R.id.buttonTan)
        rootButton = findViewById(R.id.buttonRoot)

        inputText.doAfterTextChanged { _ ->
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private var factorial: Operator = object : Operator("!", 1, true, PRECEDENCE_POWER + 1) {
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
        if (buttonText == "(")
            bracketCount++
        if (buttonText == ")")
            bracketCount--
        resultText.text = "brackets = $bracketCount"
        inputText.text = str
    }

    fun onClickAC(v : View) {
        str = "0"
        inputs.clear()
        inputs.add("0")
        inputText.text = "0"
        resultText.text = ""
        bracketCount = 0
        resultText.text = "brackets = $bracketCount"
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
        str = when (lastInput) {
            "log10(" -> {
                bracketCount--
                str.dropLast(resources.getString(R.string._lg).length + 1)
            }
            "log(" -> {
                bracketCount--
                str.dropLast(resources.getString(R.string._ln).length + 1)
            }
            "cbrt(" -> {
                bracketCount--
                str.dropLast(resources.getString(R.string._cbrt).length + 1)
            }
            "sqrt(" -> {
                bracketCount--
                str.dropLast(resources.getString(R.string._sqrt).length + 1)
            }
            "(" -> {
                bracketCount--
                str.dropLast(1)
            }
            ")" -> {
                bracketCount++
                str.dropLast(1)
            }
            else -> {
                if (funcs.contains(lastInput))
                    bracketCount--
                str.dropLast(lastInput.length)
            }
        }
        resultText.text = "brackets = $bracketCount; str=" + str
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
        when (buttonText) {
            resources.getString(R.string._lg) -> {
                inputs.add("log10(")
                str += "${resources.getString(R.string._lg)}("
            }
            resources.getString(R.string._ln) -> {
                inputs.add("log(")
                str += "${resources.getString(R.string._ln)}("
            }
            resources.getString(R.string._sqrt) -> {
                inputs.add("sqrt(")
                str += "${resources.getString(R.string._sqrt)}("
            }
            resources.getString(R.string._cbrt) -> {
                inputs.add("cbrt(")
                str += "${resources.getString(R.string._cbrt)}("
            }
            else -> {
                inputs.add("$buttonText(")
                str += "$buttonText("
            }
        }
        inputText.text = str
        bracketCount++
        resultText.text = "brackets = $bracketCount"
    }

    fun onClick2nd(v : View) {
        v as Button
        if (isAltButtons) {
            sinButton.text = resources.getString(R.string._sin)
            cosButton.text = resources.getString(R.string._cos)
            tanButton.text = resources.getString(R.string._tan)
            rootButton.text = resources.getString(R.string._sqrt)
        }
        else {
            sinButton.text = resources.getString(R.string._sinh)
            cosButton.text = resources.getString(R.string._cosh)
            tanButton.text = resources.getString(R.string._tanh)
            rootButton.text = resources.getString(R.string._cbrt)
        }
        isAltButtons = !isAltButtons
    }

    fun onClickEqual(v : View) {
        try {
            while (bracketCount > 0) {
                inputs.add(")")
                str = "$str)"
                bracketCount--
            }
            resultText.text = "brackets = $bracketCount"
            inputText.text = str
            var expression = ""
            for (elem in inputs) {
                expression += elem
            }
            val ex = ExpressionBuilder(expression).operator(factorial).build()
            var res = ex.evaluate()
            var resString = "%.8f".format(res)
            var pointIndex = resString.indexOfLast { it == ','}
            if (pointIndex != -1) {
                resString = resString.dropLastWhile { it == '0'}
                if (resString.last() == ',') {
                    resString = resString.dropLast(1)
                    if (resString == "-0") resString = "0"
                }
            }
            resultText.text = resString
        }
        catch (e : Exception) {
            val errMsg : String = resources.getString(R.string._errMsg)
            resultText.text = errMsg
        }
    }
}