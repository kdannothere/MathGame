package com.kdannothere.mathgame.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.data.Result
import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.data.record.RecordRepository
import com.kdannothere.mathgame.domain.LevelGenerator
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.MathApp
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.elements.dialog.Message
import com.kdannothere.mathgame.presentation.elements.level.Level
import com.kdannothere.mathgame.presentation.elements.level.Task
import com.kdannothere.mathgame.presentation.managers.DataMng
import com.kdannothere.mathgame.presentation.managers.LangMng
import com.kdannothere.mathgame.presentation.managers.SoundMng
import com.kdannothere.mathgame.presentation.managers.TimeMng
import com.kdannothere.mathgame.presentation.util.basicLevelAmount
import com.kdannothere.mathgame.presentation.util.englishLanguageCode
import com.kdannothere.mathgame.presentation.util.ukrainianLanguageCode
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(private val repository: RecordRepository) : ViewModel() {

    var levelList = mutableListOf<Level>()
    var taskList = mutableListOf<Task>()

    val result = Result()

    private val _date = MutableStateFlow("")
    val date = _date.asStateFlow()

    private val _records = MutableStateFlow<List<Record>>(emptyList())
    val records = _records.asStateFlow()

    private val _message = MutableSharedFlow<Message>()
    val message = _message.asSharedFlow()

    private val _currentTask = MutableStateFlow(Task())
    val currentTask = _currentTask.asStateFlow()

    var currentLevel = 0

    var topic = ""

    private var isDialogShowing = false

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    var isMusicOn = false
    var isSoundOn = false
    var languageCode = "en"

    fun changeDate(newDate: String) {
        viewModelScope.launch {
            _date.emit(newDate)
        }
    }

    fun updateRecords() {
        viewModelScope.launch(MathApp.dispatcherIO) {
            _records.emit(repository.getRecordsFromDate(date.value))
        }
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
                message = getText(activity, R.string.you_did_not_write_anything)
                showNewMessage(Message(message, DialogType.basicDialog))
                false
            }

            isUserCorrect -> {
                message = getText(activity, R.string.correct_answer)
                showNewMessage(Message(message, DialogType.nextTaskDialog))
                result.addOneCorrect(getCurrentTaskId())
                true
            }

            else -> {
                message =
                    getText(activity, R.string.wrong) + "\n" +
                            getText(activity, R.string.the_correct_answer_is) + "\n" +
                            correctAnswer
                showNewMessage(Message(message, DialogType.nextTaskDialog))
                result.addOneMistake(getCurrentTaskId())
                false
            }
        }
    }

    private fun finishLevel(activity: MainActivity) {
        if (isDialogShowing) return
        showNewMessage(
            Message(
                getText(activity, R.string.congratulations_the_level_is_passed),
                DialogType.endLevelDialog
            )
        )
        viewModelScope.launch(MathApp.dispatcherIO) {
            val currentResult = result
            repository.upsertRecord(
                Record(
                    timeStamp = TimeMng.getCurrentTimeStamp(),
                    date = TimeMng.formatDateDb(TimeMng.getCurrentDate()),
                    topic = currentResult.topic,
                    level = currentResult.level.toString(),
                    correct = currentResult.correct.toString(),
                    mistakes = currentResult.mistakes.toString(),
                    skipped = currentResult.skipped.toString(),
                )
            )
        }
    }

    fun showNextQuestion(activity: MainActivity) {
        when (isLastTask()) {
            true -> {
                finishLevel(activity)
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
        result.level = lvl
        result.topic = topic
    }

    fun skipCurrentTask(activity: MainActivity, taskId: Int = getCurrentTaskId()) {
        result.addOneSkipped(taskId)
        showNextQuestion(activity)
    }

    fun restartLevel() {
        levelList[currentLevel - 1] = LevelGenerator.getOneLevel(
            lvl = currentLevel,
            operation = currentTask.value.operation
        )
        updateTaskList(currentLevel)
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
            DataMng.saveLanguage(context, languageCode)
        }
    }

    fun loadSettings(activity: MainActivity) {
        viewModelScope.launch(MathApp.dispatcherIO) {
            try {
                val languageCode = async { DataMng.loadLanguage(activity) }
                this@GameViewModel.languageCode = languageCode.await()
                val isMusicOn = async { DataMng.loadMusicSetting(activity) }
                this@GameViewModel.isMusicOn = isMusicOn.await()
                val isSoundOn = async { DataMng.loadSoundSetting(activity) }
                this@GameViewModel.isSoundOn = isSoundOn.await()
                withContext(MathApp.dispatcherMain) {
                    if (!isMusicOn.await()) {
                        SoundMng.pauseMusic(activity)
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
        return LangMng.getLocalizedContext(activity, languageCode).getString(stringResId)
    }
}