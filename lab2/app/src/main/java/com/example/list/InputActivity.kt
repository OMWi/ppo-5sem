package com.example.list

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class InputActivity : AppCompatActivity() {
    private lateinit var buttonInput: Button
    private lateinit var editGrade1: EditText
    private lateinit var editGrade2: EditText
    private lateinit var editGrade3: EditText
    private lateinit var editName: EditText
    private lateinit var editSurname: EditText
    private lateinit var action: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        buttonInput = findViewById(R.id.button_input)
        editGrade1 = findViewById(R.id.edit_grade1)
        editGrade2 = findViewById(R.id.edit_grade2)
        editGrade3 = findViewById(R.id.edit_grade3)
        editName = findViewById(R.id.edit_name)
        editSurname = findViewById(R.id.edit_surname)

        action = intent.extras!!.getString("action", resources.getString(R.string.add))
        if (action == resources.getString(R.string.edit)) {
            val enrollee = intent.extras!!.get("enrollee") as Enrollee
            val initials = enrollee.initials.split(" ")
            editName.setText(initials[1], TextView.BufferType.EDITABLE)
            editSurname.setText(initials[0], TextView.BufferType.EDITABLE)
            editGrade1.setText(enrollee.grades[0].toString(), TextView.BufferType.EDITABLE)
            editGrade2.setText(enrollee.grades[1].toString(), TextView.BufferType.EDITABLE)
            editGrade3.setText(enrollee.grades[2].toString(), TextView.BufferType.EDITABLE)
        }
        buttonInput.text = action
    }

    private fun validateInput(): Boolean {
        if (editGrade1.text.toString().isBlank() or editGrade2.text.toString().isBlank() or
                editGrade3.text.toString().isBlank() or editName.text.toString().isBlank() or
                editSurname.text.toString().isBlank()) {
            Toast.makeText(applicationContext, resources.getString(R.string.blank_input), Toast.LENGTH_SHORT).show()
            return false
        }
        // check non alpha symbols in edit
        val grade1 = editGrade1.text.toString().toInt()
        val grade2 = editGrade2.text.toString().toInt()
        val grade3 = editGrade3.text.toString().toInt()
        if ((grade1 > 5) or (grade2 > 5) or (grade3 > 5)) {
            Toast.makeText(applicationContext, resources.getString(R.string.grade_incorrect), Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun onClickInput(v: View) {
        if (!validateInput()) return
        val initials = "${editName.text} ${editSurname.text}"
        val grade1 = editGrade1.text.toString().toInt()
        val grade2 = editGrade2.text.toString().toInt()
        val grade3 = editGrade3.text.toString().toInt()
        val grades = arrayOf(grade1, grade2, grade3)
        //val grades = arrayOf(editGrade1.text.toString().toInt(), editGrade2.text.toString().toInt(),
          //      editGrade3.toString().toInt())
        when(action) {
            resources.getString(R.string.add) -> {
                val intent = Intent()
                intent.putExtra("enrollee", Enrollee(initials, grades))
                setResult(0, intent)
                finish()
            }
            resources.getString(R.string.edit) -> {
                val intent = Intent()
                intent.putExtra("enrollee", Enrollee(initials, grades))
                setResult(1, intent)
                finish()
            }
        }
    }

}