package com.example.list

class Enrollee(initials: String, grades: Array<Int>) {
    var initials: String = initials
    var grades: Array<Int> = grades
    var averageGrade = grades.sum() / grades.size
}