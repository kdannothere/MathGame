package com.kdannothere.mathgame.presentation.elements.level

import com.kdannothere.mathgame.presentation.util.operationAddition
import com.kdannothere.mathgame.presentation.util.operationDivision
import com.kdannothere.mathgame.presentation.util.operationMultiplication
import com.kdannothere.mathgame.presentation.util.operationSubtraction
import java.util.Date
import kotlin.random.Random

object TaskGenerator {

    private val random = Random(Date().time)
    private const val invalidOperation = "ERROR - invalid operation"

    fun getTaskList(taskAmount: Int, lvl: Int, operation: String): MutableList<Task> {
        val taskList = mutableListOf<Task>()
        repeat(taskAmount) { index ->
            val previousQuestion = when (index) {
                0 -> ""
                else -> taskList[index - 1].question
            }
            val task = getTask(
                id = index + 1,
                lvl = lvl,
                operation = operation,
                previousQuestion = previousQuestion
            )
            taskList += task
        }
        return taskList
    }

    private fun getTask(
        id: Int,
        lvl: Int,
        operation: String,
        previousQuestion: String = "",
    ): Task {
        var valueN1: Int
        var valueN2: Int
        var question: String

        do {
            valueN1 = getValue(lvl, operation)
            valueN2 = getValue(lvl, operation, valueN1)
            question = "$valueN1 $operation $valueN2 = ?"
        } while (question == previousQuestion)

        return Task(
            id = id,
            operation = operation,
            question = question,
            answer = getAnswer(valueN1, valueN2, operation)
        )
    }


    private fun getAnswer(valueN1: Int, valueN2: Int, operation: String): String {
        val answer = when (operation) {
            operationAddition -> valueN1 + valueN2
            operationSubtraction -> valueN1 - valueN2
            operationMultiplication -> valueN1 * valueN2
            operationDivision -> valueN1 / valueN2
            else -> invalidOperation
        }
        return answer.toString()
    }

    private fun getValue(
        lvl: Int,
        operation: String,
        previousValue: Int = -1,
    ): Int {
        return when (operation) {

            operationAddition -> random.nextInt(lvl, lvl * 3 + 1)

            operationSubtraction -> {
                val value = random.nextInt(lvl, lvl * 3 + 1)
                when {
                    previousValue < value && previousValue != -1 -> {
                        random.nextInt(previousValue / 5, previousValue + 1)
                    }

                    else -> value
                }
            }

            operationMultiplication -> {
                when (previousValue) {
                    -1 -> lvl
                    else -> random.nextInt(1, 10 + 1)
                }
            }

            operationDivision -> {
                when (previousValue) {
                    -1 -> random.nextInt(1, 10 + 1) * lvl
                    else -> lvl
                }
            }

            else -> throw Exception(invalidOperation)
        }
    }
}