package com.kdannothere.mathgame.domain

import com.kdannothere.mathgame.presentation.elements.level.Level
import com.kdannothere.mathgame.presentation.util.basicTaskAmount

object LevelGenerator {

    // The id of the first level and the first task is 1

    fun getLevels(lvlAmount: Int, operation: String): MutableList<Level> {
        val levelList = mutableListOf<Level>()
        repeat(lvlAmount) {
            val lvl = it + 1

            val taskList = TaskGenerator.getTaskList(
                taskAmount = basicTaskAmount,
                lvl = lvl,
                operation = operation
            )


            levelList.add(
                Level(
                    id = lvl,
                    taskList,
                    operation
                )
            )
        }
        return levelList
    }

    fun getOneLevel(lvl: Int, operation: String): Level {
        val taskList = TaskGenerator.getTaskList(
            taskAmount = basicTaskAmount,
            lvl = lvl,
            operation = operation
        )
        return Level(lvl, taskList, operation)
    }
}