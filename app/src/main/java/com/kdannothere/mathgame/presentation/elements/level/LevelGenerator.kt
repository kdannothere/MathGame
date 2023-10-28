package com.kdannothere.mathgame.presentation.elements.level

import com.kdannothere.mathgame.presentation.util.basicLevelAmount

object LevelGenerator {

    // the id of the first level and the first task is 1

    fun getLevels(operation: String, amount: Int = basicLevelAmount): MutableList<Level> {
        return when (operation) {
            "+" -> addition(amount)
            "-" -> subtraction(amount)
            "*" -> multiplication(amount)
            "/" -> division(amount)
            else -> throw Exception("Invalid operation")
        }
    }

    private fun generateLevels(amount: Int, operation: String): MutableList<Level> {
        val levelList = mutableListOf<Level>()
        repeat(amount) {
            val taskList = mutableListOf<Task>()
            repeat(amount) { taskId ->
                taskList.add(
                    TaskGenerator.getTask(
                        id = taskId + 1,
                        lvl = levelList.size + 1,
                        operation = operation
                    )
                )
            }

            levelList.add(
                Level(
                    id = levelList.size + 1,
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