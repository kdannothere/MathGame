package com.kdannothere.mathgame.presentation.elements.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.textview.MaterialTextView
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.presentation.MainActivity

object DialogMng {

    fun showDialog(
        message: String,
        dialogType: String,
        fragment: Fragment,
        event: () -> Unit = {},
    ) {
        Log.d("MyLog", "showDialog")
        when (dialogType) {
            DialogType.basicDialog -> showBasicDialog(message, fragment)
            DialogType.nextTaskDialog -> showNextTaskDialog(message, fragment, event)
            DialogType.endLevelDialog -> showEndLevelDialog(message, fragment, event)
        }
    }

    private fun showBasicDialog(message: String, fragment: Fragment) {
        val dialogLayout =
            fragment.activity?.layoutInflater?.inflate(R.layout.dialog_basic, null) ?: return
        val activity = fragment.activity as MainActivity

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
        fragment: Fragment,
        showNextQuestion: () -> Unit,
    ) {
        val dialogLayout =
            fragment.activity?.layoutInflater?.inflate(R.layout.dialog_next_task, null) ?: return
        val activity = fragment.activity as MainActivity

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
            showNextQuestion.invoke()
        }

        dialog.show()
    }

    private fun showEndLevelDialog(
        message: String,
        fragment: Fragment,
        showResults: () -> Unit,
    ) {
        Log.d("MyLog", "showEndLevelDialog")
        val dialogLayout =
            fragment.activity?.layoutInflater?.inflate(R.layout.dialog_end_level, null) ?: return
        val activity = fragment.activity as MainActivity

        val builder = AlertDialog.Builder(activity.baseContext)
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
            Log.d("MyLog", "setOnClickListener")
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            Log.d("MyLog", "setOnDismissListener")
            try {
                if (fragment.isAdded) {
                    showResults.invoke()
                }
            } catch (exception: Exception) {
                Log.d("MyLog", "exception: ${exception.message.toString()}")
            }
        }

        dialog.show()
    }
}