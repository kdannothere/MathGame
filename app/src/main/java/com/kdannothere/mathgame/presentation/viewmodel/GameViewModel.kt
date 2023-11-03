package com.kdannothere.mathgame.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MathApp
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.elements.dialog.Message
import com.kdannothere.mathgame.presentation.elements.level.Level
import com.kdannothere.mathgame.presentation.elements.level.LevelGenerator
import com.kdannothere.mathgame.presentation.elements.level.Results
import com.kdannothere.mathgame.presentation.elements.level.Task
import com.kdannothere.mathgame.presentation.elements.picture.Picture
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    var levelList = mutableListOf<Level>()
    var taskList = mutableListOf<Task>()
    val pictureList = mutableListOf<Picture>()

    val results = Results()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private val _currentTask = MutableStateFlow(Task(0, "", "", ""))
    val currentTask = _currentTask.asStateFlow()

    private val _taskId = MutableStateFlow(1)
    val taskId = _taskId.asStateFlow()

    var currentLevelId = 0
    private var isFinished = true

    private fun addPicture() {
        pictureList.add(Picture(id = pictureList.size + 1, resId = R.drawable.image_1_moon))
    }

    private fun showNewMessage(message: Message) {
        viewModelScope.launch {
            _message.emit(message)
        }
    }

    fun check(
        userAnswer: String,
        correctAnswer: String = currentTask.value.answer,
    ) {
        val isUserCorrect = userAnswer == correctAnswer
        val message: String
        when {
            userAnswer.isBlank() -> {
                message = "You didn't write anything. Just skip if you don't know the answer."
                showNewMessage(Message(message, DialogType.basicDialog))
            }

            isUserCorrect -> {
                message = "Correct!"
                results.addOneCorrect(getCurrentTaskId())
                showNewMessage(Message(message, DialogType.nextTaskDialog))
            }

            else -> {
                message = "Wrong :(\nThe correct answer is: $correctAnswer"
                results.addOneMistake(getCurrentTaskId())
                showNewMessage(Message(message, DialogType.nextTaskDialog))
            }
        }
    }

    fun showNextQuestion() {
        when (isLastTask()) {
            true -> {
                if (isFinished) return
                isFinished = true
                showNewMessage(
                    Message(
                        "Congratulations! The level is passed. You got a new picture! :)",
                        DialogType.endLevelDialog
                    )
                )
                addPicture()
            }
            false -> {
                isFinished = false
                updateCurrentTask(task = taskList[currentTask.value.id])
            }
        }

    }

    private fun updateTaskId(id: Int) {
        viewModelScope.launch {
            _taskId.emit(id)
        }
    }

    private fun updateCurrentTask(task: Task) {
        viewModelScope.launch(MathApp.dispatcherIO) {
            _currentTask.emit(task)
            updateTaskId(id = task.id)
        }
    }

    fun createLevelList(operation: String) {
        levelList = LevelGenerator.getLevels(operation)
    }

    fun updateTaskList(lvl: Int) {
        taskList = levelList[lvl - 1].taskList.toMutableList()
        updateCurrentTask(task = taskList.first())
    }

    fun skipCurrentTask(taskId: Int = getCurrentTaskId()) {
        results.addOneSkipped(taskId)
        showNextQuestion()
    }

    private fun isLastTask(): Boolean = currentTask.value.id == taskList.last().id
    private fun getCurrentTaskId(): Int = currentTask.value.id
}