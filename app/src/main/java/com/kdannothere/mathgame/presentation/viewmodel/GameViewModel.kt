package com.kdannothere.mathgame.presentation.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MathApp
import com.kdannothere.mathgame.presentation.Picture
import com.kdannothere.mathgame.presentation.Task
import com.kdannothere.mathgame.presentation.Util
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random

class GameViewModel : ViewModel() {

    private val random = Random(Date().time)

    private val taskList = mutableListOf<Task>()

    val pictureList = mutableListOf<Picture>()

    private val _currentTask = MutableStateFlow(Task())
    val currentTask = _currentTask.asStateFlow()

    private val _pictureParts = MutableStateFlow(0)
    val pictureParts = _pictureParts.asStateFlow()

    init {
        repeat(5) {
            addTask(genTask())
        }
        viewModelScope.launch(MathApp.dispatcherIO) {
            _currentTask.emit(taskList.first())
        }
    }

    private fun addPicture() {
        pictureList.add(Picture(id = pictureList.size, resId = R.drawable.image_1_moon))
    }

    fun check(
        activity: Activity,
        userAnswer: String,
        correctAnswer: String = currentTask.value.answer,
    ) {
        val isUserCorrect = userAnswer == correctAnswer

        when {
            isUserCorrect -> {
                val isAllParts = pictureParts.value + 1 == 4
                val message = if (isAllParts) "Correct! You got a new picture!" else "Correct!"
                if (isAllParts) addPicture()
                Util.showDialog(activity, message = message)
                showNextQuestion()
                updatePictureParts()
            }

            else -> {
                Util.showDialog(activity, message = "Wrong :(")
            }
        }
    }

    private fun showNextQuestion() {
        viewModelScope.launch(MathApp.dispatcherIO) {
            try {
                _currentTask.emit(taskList[currentTask.value.id])
            } catch (e: Exception) {
                _currentTask.emit(taskList.first())
            }
        }
    }

    private fun updatePictureParts() {
        viewModelScope.launch {
            val newValue = pictureParts.value + 1
            _pictureParts.emit(if (newValue == 4) 0 else newValue)
        }
    }

    private fun genTask(): Task {
        val valueN1 = random.nextInt(0, 5)
        val valueN2 = random.nextInt(0, 5)
        val question = "$valueN1 + $valueN2 = ?"

        return Task(
            id = taskList.size + 1,
            question = question,
            answer = (valueN1 + valueN2).toString()
        )
    }

    private fun addTask(task: Task) = taskList.add(task)
}