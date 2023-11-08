package com.kdannothere.mathgame.presentation.elements.dialog

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textview.MaterialTextView
import com.kdannothere.mathgame.R

object DialogMng {

    fun showDialog(
        message: String,
        dialogType: String,
        activity: Activity,
        closeDialog: () -> Unit,
        event: () -> Unit = {},
    ) {
        when (dialogType) {
            DialogType.basicDialog -> showBasicDialog(message, activity, closeDialog)
            DialogType.nextTaskDialog -> showNextTaskDialog(message, activity, closeDialog, event)
            DialogType.endLevelDialog -> showEndLevelDialog(message, activity, closeDialog, event)
        }
    }

    private fun showBasicDialog(message: String, activity: Activity, closeDialog: () -> Unit) {
        val dialogLayout =
            activity.layoutInflater.inflate(R.layout.dialog_basic, null)

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
            closeDialog.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showNextTaskDialog(
        message: String,
        activity: Activity,
        closeDialog: () -> Unit,
        showNextQuestion: () -> Unit,
    ) {
        val dialogLayout =
            activity.layoutInflater.inflate(R.layout.dialog_next_task, null)

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
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            closeDialog.invoke()
            showNextQuestion.invoke()
        }

        dialog.show()
    }

    private fun showEndLevelDialog(
        message: String,
        activity: Activity,
        closeDialog: () -> Unit,
        showResults: () -> Unit,
    ) {
        val dialogLayout =
            activity.layoutInflater.inflate(R.layout.dialog_end_level, null)

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
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            closeDialog.invoke()
            showResults.invoke()
        }

        dialog.show()
    }
}