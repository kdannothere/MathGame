package com.kdannothere.mathgame.presentation.managers

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textview.MaterialTextView
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType

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
            activity.layoutInflater.inflate(R.layout.element_dialog, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)

        messageField.text = message

        val button = dialogLayout.findViewById<ImageView>(R.id.uni_button)
        button.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            closeDialog.invoke()
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
            activity.layoutInflater.inflate(R.layout.element_dialog, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)
        messageField.text = message

        val button = dialogLayout.findViewById<ImageView>(R.id.uni_button)
        button.setImageResource(R.drawable.next_task)

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
            activity.layoutInflater.inflate(R.layout.element_dialog, null)

        val builder = AlertDialog.Builder(activity)

        builder.setView(dialogLayout)

        val dialog = builder.create()

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val messageField = dialogLayout.findViewById<MaterialTextView>(R.id.message_field)
        messageField.text = message

        val button: ImageView = dialogLayout.findViewById(R.id.uni_button)
        button.setImageResource(R.drawable.next_level)

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