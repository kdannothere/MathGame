package com.kdannothere.mathgame.presentation.elements.level

class Results {

    private val correctTaskIdList = mutableSetOf<Int>()
    private val mistakesTaskIdList = mutableSetOf<Int>()
    private val skippedTaskIdList = mutableSetOf<Int>()

    val correct get() = correctTaskIdList.size
    val mistakes get() = mistakesTaskIdList.size
    val skipped get() = skippedTaskIdList.size

    fun clear() {
        correctTaskIdList.clear()
        mistakesTaskIdList.clear()
        skippedTaskIdList.clear()
    }

    fun addOneCorrect(taskId: Int) = correctTaskIdList.add(taskId)
    fun addOneMistake(taskId: Int) = mistakesTaskIdList.add(taskId)
    fun addOneSkipped(taskId: Int) = skippedTaskIdList.add(taskId)
}