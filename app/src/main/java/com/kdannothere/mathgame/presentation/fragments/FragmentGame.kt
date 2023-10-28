package com.kdannothere.mathgame.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentGameBinding
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.elements.dialog.DialogMng
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.util.basicTaskAmount
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FragmentGame : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        setClickListeners()

        viewModel.currentTask.onEach { task ->
            binding.question.text = task.question
        }.launchIn(lifecycleScope)

        viewModel.taskId.onEach { currentNumber ->
            binding.taskNumber.text = "${currentNumber}/$basicTaskAmount"
        }.launchIn(lifecycleScope)

        viewModel.message.onEach { message ->
            DialogMng.showDialog(
                message.text,
                message.dialogType,
                requireActivity() as MainActivity,
                event = {
                    when (message.dialogType) {
                        DialogType.nextTaskDialog -> viewModel.showNextQuestion()
                        else -> findNavController().navigate(R.id.action_game_to_results)
                    }
                }
            )
        }.launchIn(lifecycleScope)

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            buttonCheck.setOnClickListener {
                viewModel.check(
                    userAnswer = binding.userAnswer.text.toString()
                )
                binding.userAnswer.setText("")
            }
            buttonSkip.setOnClickListener {
                viewModel.skipCurrentTask()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}