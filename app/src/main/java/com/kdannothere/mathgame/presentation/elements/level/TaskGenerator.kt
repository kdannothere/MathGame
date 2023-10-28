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

    fun getTask(id: Int, lvl: Int, operation: String): Task {
        val valueN1 = getValue(lvl, operation = operation)
        val valueN2 = getValue(lvl, valueN1, operation = operation)
        val question = "$valueN1 $operation $valueN2 = ?"

        return Task(
            id = id,
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

    private fun getValue(lvl: Int, previousValue: Int = -1, operation: String): Int {
        val number = random.nextInt(1, 5)
        val value = number * (1 + lvl / 3)
        return when (operation) {

            operationAddition -> value

            operationSubtraction -> {
                when {
                    previousValue == -1 -> value
                    previousValue < value -> value - previousValue
                    else -> value
                }
            }

            operationMultiplication -> {
                when (previousValue) {
                    -1 -> value
                    else -> number
                }
            }

            operationDivision -> {
                when {
                    previousValue == -1 -> value
                    previousValue % value != 0 -> getValue(lvl, previousValue, operation)
                    else -> value
                }
            }

            else -> throw Exception(invalidOperation)
        }
    }
}