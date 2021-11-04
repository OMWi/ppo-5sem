package com.example.list

import java.io.Serializable

class Enrollee(initials: String, grades: Array<Int>): Serializable {
    var globalID: Int = 0
    var id: Int = globalID++
    var initials: String = initials
    var grades: Array<Int> = grades
    var averageGrade: Double = grades.sum().toDouble() / grades.size.toDouble()
}