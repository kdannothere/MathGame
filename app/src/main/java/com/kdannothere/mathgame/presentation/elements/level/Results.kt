package com.kdannothere.mathgame.presentation.elements.level

class Results {

    var correct = 0
    var mistakes = 0
    var skipped = 0

    fun clear() {
        correct = 0
        mistakes = 0
        skipped = 0
    }

    fun addOneCorrect() = ++correct
    fun addOneMistake() = ++correct
    fun addOneSkipped() = ++correct
}