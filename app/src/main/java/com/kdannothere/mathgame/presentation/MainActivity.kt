package com.kdannothere.mathgame.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import com.google.android.material.textview.MaterialTextView
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.nav_host_fragment_container).navigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showDialog(message: String) {
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.message, null)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogLayout)

        val dialog = builder.create()

        val displayMetrics = resources.displayMetrics
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
}