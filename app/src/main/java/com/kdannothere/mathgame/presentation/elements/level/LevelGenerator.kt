package com.kdannothere.mathgame.presentation.elements.level

import com.kdannothere.mathgame.presentation.util.basicLevelAmount
import com.kdannothere.mathgame.presentation.util.basicTaskAmount

object LevelGenerator {

    // The id of the first level and the first task is 1

    fun getLevels(operation: String, amount: Int = basicLevelAmount): MutableList<Level> {
        return when (operation) {
            "+" -> addition(amount)
            "-" -> subtraction(amount)
            "*" -> multiplication(amount)
            "/" -> division(amount)
            else -> throw Exception("Invalid operation")
        }
    }

    private fun generateLevels(lvlAmount: Int, operation: String): MutableList<Level> {
        val levelList = mutableListOf<Level>()
        repeat(lvlAmount) {
            val lvlId = it + 1

            val taskList = TaskGenerator.getTaskList(
                taskAmount = basicTaskAmount,
                lvl = lvlId,
                operation = operation
            )


            levelList.add(
                Level(
                    id = lvlId,
                    taskList
                )
            )
        }
        return levelList
    }

    private fun addition(amount: Int): MutableList<Level> {
        val operation = "+"
        return generateLevels(amount, operation)
    }

    private fun subtraction(amount: Int): MutableList<Level> {
        val operation = "-"
        return generateLevels(amount, operation)
    }

    private fun multiplication(amount: Int): MutableList<Level> {
        val operation = "*"
        return generateLevels(amount, operation)
    }

    private fun division(amount: Int): MutableList<Level> {
        val operation = "/"
        return generateLevels(amount, operation)
    }
}