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
    private var taskList = mutableListOf<Task>()
    val pictureList = mutableListOf<Picture>()

    val results = Results()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private val _currentTask = MutableStateFlow(Task())
    val currentTask = _currentTask.asStateFlow()

    private val _taskId = MutableStateFlow(1)
    val taskId = _taskId.asStateFlow()

    var currentLevelId = 0

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
            isUserCorrect -> {
                message = "Correct!"
                results.addOneCorrect()
            }
            else -> {
                message = "Wrong :("
                results.addOneMistake()
            }
        }
        showNewMessage(Message(message, DialogType.nextTaskDialog))
    }

    fun showNextQuestion() {
        viewModelScope.launch(MathApp.dispatcherIO) {

            // is the last task?
            when (_currentTask.value.id == taskList.size) {
                true -> {
                    showNewMessage(
                        Message(
                            "Congratulations! The level is passed. You got a new picture! :)",
                            DialogType.endLevelDialog
                            )
                    )
                    addPicture()
                }
                false -> {
                    updateCurrentTask(taskList[currentTask.value.id])
                }
            }
        }
    }

    private fun updateTaskId(id: Int) {
        viewModelScope.launch {
            _taskId.emit(id)
        }
    }

    fun createLevelList(operation: String) {
        levelList = LevelGenerator.getLevels(operation)
    }

    fun updateTaskList(lvl: Int) {
        taskList = levelList[lvl - 1].taskList.toMutableList()
        updateCurrentTask(taskList.first())
    }

    private fun updateCurrentTask(task: Task) {
        viewModelScope.launch(MathApp.dispatcherIO) {
            _currentTask.emit(task)
            updateTaskId(_currentTask.value.id)
        }
    }

    fun skipCurrentTask() {
        results.addOneSkipped()
        showNextQuestion()
    }
}