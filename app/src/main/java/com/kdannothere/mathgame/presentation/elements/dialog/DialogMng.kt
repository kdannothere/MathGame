package com.kdannothere.mathgame.presentation.elements.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MainActivity

object DialogMng {

    fun showDialog(
        message: String,
        dialogType: String,
        activity: MainActivity,
        event: () -> Unit = {},
    ) {
        when (dialogType) {
            DialogType.basicDialog -> showBasicDialog(message, activity)
            DialogType.nextTaskDialog -> showNextTaskDialog(message, activity, event)
            DialogType.endLevelDialog -> showEndLevelDialog(message, activity, event)
        }
    }

    private fun showBasicDialog(message: String, activity: MainActivity) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_basic, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val displayMetrics = activity.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        dialog.window?.setLayout((width * 0.8).toInt(), (height * 0.8).toInt())

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)

        messageField.text = message

        val button = dialogLayout.findViewById<ConstraintLayout>(R.id.button_ok)
        button.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNextTaskDialog(
        message: String,
        activity: MainActivity,
        showNextQuestion: () -> Unit,
    ) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_next_task, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val displayMetrics = activity.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        dialog.window?.setLayout((width * 0.8).toInt(), (height * 0.8).toInt())

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)

        messageField.text = message

        val button = dialogLayout.findViewById<ImageView>(R.id.next_task)
        button.setOnClickListener {
            showNextQuestion.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEndLevelDialog(
        message: String,
        activity: MainActivity,
        showResults: () -> Unit,
    ) {
        val dialogLayout = activity.layoutInflater.inflate(R.layout.dialog_end_level, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val displayMetrics = activity.resources.displayMetrics
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels

        dialog.window?.setLayout((width * 0.8).toInt(), (height * 0.8).toInt())

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)

        messageField.text = message

        val button = dialogLayout.findViewById<ConstraintLayout>(R.id.button_results)
        button.setOnClickListener {
            showResults.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }
}