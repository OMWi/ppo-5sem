package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var str : String = ""
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textview)
    }

    fun onClickDigit(v : View) {
        v as Button
        str += v.text.toString()
        textView.text = str
    }

    fun onClickAC(v : View) {
        if (str.isEmpty()) return
        str = ""
        textView.text = "0"
    }

    fun onClickBackspace(v : View) {
        if (str.isEmpty()) return
        str = str.dropLast(1)
        textView.text = str
    }

    fun onClickE(v : View) {
        str += kotlin.math.E
        textView.text = str
    }

    fun onClickOperation(v : View) {
        v as Button
        var lastChar : Char = str[str.length-1]
        if (lastChar.isDigit() or (lastChar == '(') or (lastChar == ')')) {
            str += v.text.toString()
        }
        else {
            str = str.dropLast(1)
            str += v.text.toString()
        }
        textView.text = str
    }

    fun onClickFunction(v : View) {
        v as Button
        str += v.text.toString() + "("
        textView.text = str
    }

    fun onClickEquals(v : View) {
        val engine = ScriptEngineManager.
    }
}