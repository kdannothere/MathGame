package com.kdannothere.mathgame.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.util.englishLanguageCode
import com.kdannothere.mathgame.managers.DataManager
import com.kdannothere.mathgame.managers.LangManager
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.elements.dialog.Message
import com.kdannothere.mathgame.presentation.elements.level.Level
import com.kdannothere.mathgame.presentation.elements.level.LevelGenerator
import com.kdannothere.mathgame.presentation.elements.level.Results
import com.kdannothere.mathgame.presentation.elements.level.Task
import com.kdannothere.mathgame.presentation.elements.picture.Picture
import com.kdannothere.mathgame.presentation.util.basicLevelAmount
import com.kdannothere.mathgame.presentation.util.ukrainianLanguageCode
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// fragment history
// change image logic and design

class GameViewModel : ViewModel() {

    var levelList = mutableListOf<Level>()
    var taskList = mutableListOf<Task>()
    val pictureList = mutableListOf<Picture>()

    val results = Results()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private val _currentTask = MutableStateFlow(Task())
    val currentTask = _currentTask.asStateFlow()

    var currentLevel = 0
    var currentOperation = ""
    private var isDialogShowing = false

    private val _isLoading = MutableSharedFlow<Boolean>()
    val isLoading = _isLoading.asSharedFlow()

    var isMusicOn = false
    var isSoundOn = false
    var languageCode = "en"

    private fun addPicture() {
        pictureList.add(Picture(id = pictureList.size + 1, resId = R.drawable.image_1_moon))
    }

    private fun showNewMessage(newMessage: Message) {
        viewModelScope.launch {
            if (isDialogShowing) return@launch
            isDialogShowing = true
            _message.emit(newMessage)
        }
    }

    fun check(
        activity: MainActivity,
        userAnswer: String,
        correctAnswer: String = currentTask.value.answer,
    ): Boolean {
        val isUserCorrect = userAnswer == correctAnswer
        val message: String
        return when {
            userAnswer.isBlank() -> {
                message = getText(activity, R.string.you_didn_t_write_anything)
                showNewMessage(Message(message, DialogType.basicDialog))
                false
            }

            isUserCorrect -> {
                message = getText(activity, R.string.correct_answer)
                results.addOneCorrect(getCurrentTaskId())
                showNewMessage(Message(message, DialogType.nextTaskDialog))
                true
            }

            else -> {
                message =
                    getText(activity, R.string.wrong) + "\n" +
                            getText(activity, R.string.the_correct_answer_is) + "\n" +
                            correctAnswer
                results.addOneMistake(getCurrentTaskId())
                showNewMessage(Message(message, DialogType.nextTaskDialog))
                false
            }
        }
    }

    fun showNextQuestion(activity: MainActivity) {
        when (isLastTask()) {
            true -> {
                showNewMessage(
                    Message(
                        getText(activity, R.string.congratulations_the_level_is_passed),
                        DialogType.endLevelDialog
                    )
                )
                addPicture()
            }

            false -> {
                updateCurrentTask(task = taskList[currentTask.value.id])
            }
        }

    }

    private fun updateCurrentTask(task: Task) {
        viewModelScope.launch {
            _currentTask.emit(task)
        }
    }

    fun createLevelList(operation: String) {
        levelList = LevelGenerator.getLevels(basicLevelAmount, operation)
    }

    fun updateTaskList(lvl: Int) {
        taskList = levelList[lvl - 1].taskList.toMutableList()
        updateCurrentTask(task = taskList.first())
    }

    fun skipCurrentTask(activity: MainActivity, taskId: Int = getCurrentTaskId()) {
        results.addOneSkipped(taskId)
        showNextQuestion(activity)
    }

    fun isLastLevel(): Boolean = levelList.last().id == currentLevel
    private fun isLastTask(): Boolean = currentTask.value.id == taskList.last().id
    private fun getCurrentTaskId(): Int = currentTask.value.id
    fun closeDialog() = run { isDialogShowing = false }

    fun changeLanguage(context: Context) {
        languageCode = when (languageCode) {
            englishLanguageCode -> ukrainianLanguageCode
            ukrainianLanguageCode -> englishLanguageCode
            else -> englishLanguageCode
        }
        viewModelScope.launch(MathApp.dispatcherIO) {
            DataManager.saveLanguage(context, languageCode)
        }
    }

    fun loadSettings(activity: MainActivity) {
        viewModelScope.launch(MathApp.dispatcherIO) {
            try {
                val languageCode = async { DataManager.loadLanguage(activity) }
                this@GameViewModel.languageCode = languageCode.await()
                val isMusicOn = async { DataManager.loadMusicSetting(activity) }
                this@GameViewModel.isMusicOn = isMusicOn.await()
                val isSoundOn = async { DataManager.loadSoundSetting(activity) }
                this@GameViewModel.isSoundOn = isSoundOn.await()
                withContext(MathApp.dispatcherMain) {
                    if (!isMusicOn.await()) {
                        SoundManager.pauseMusic(activity)
                    }
                }
                _isLoading.emit(false)
            } catch (e: Exception) {
                showNewMessage(
                    Message(
                        text = getText(activity, R.string.loading_went_wrong),
                        dialogType = DialogType.basicDialog
                    )
                )
                println(e.message)
            }
        }
    }

    fun getText(activity: MainActivity, stringResId: Int): String {
        return LangManager.getLocalizedContext(activity, languageCode).getString(stringResId)
    }

    fun restartLevel() {
        levelList[currentLevel - 1] = LevelGenerator.getOneLevel(currentLevel, currentOperation)
        updateTaskList(currentLevel)
    }

}