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

    fun getTask(id: Int, lvl: Int, operation: String, previousQuestion: String = ""): Task {
        val valueN1 = getValue(lvl, operation = operation)
        val valueN2 = getValue(lvl, valueN1, operation = operation)

        return when (val question = "$valueN1 $operation $valueN2 = ?") {
            previousQuestion -> {
                getTask(id, lvl, operation, previousQuestion)
            }
            else -> {
                Task(
                    id = id,
                    operation = operation,
                    question = question,
                    answer = getAnswer(valueN1, valueN2, operation)
                )
            }
        }
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
        previousValue: Int = -1,
        operation: String,
    ): Int {
        return when (operation) {

            operationAddition -> random.nextInt(lvl, lvl * 3 + 1)

            operationSubtraction -> {
                val value = random.nextInt(lvl, lvl * 3 + 1)
                when {
                    previousValue < value -> random.nextInt(lvl, previousValue + 1)
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