package com.example.list

class Enrollee(initials: String, grades: Array<Int>) {
    var initials: String = initials
    var grades: Array<Int> = grades
    var averageGrade: Double = grades.sum().toDouble() / grades.size.toDouble()
}