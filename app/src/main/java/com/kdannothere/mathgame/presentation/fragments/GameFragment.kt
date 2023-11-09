package com.kdannothere.mathgame.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
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
import com.kdannothere.mathgame.managers.LangManager
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.elements.dialog.DialogMng
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        setText()

        setClickListeners()

        viewModel.currentTask.onEach { task ->
            @SuppressLint("SetTextI18n")
            binding.taskNumber.text = "${task.id}/${viewModel.taskList.size}"
            binding.question.text = task.question
        }.launchIn(lifecycleScope)

        viewModel.message.onEach { message ->
            DialogMng.showDialog(
                message.text,
                message.dialogType,
                requireActivity(),
                closeDialog = { viewModel.closeDialog() },
                event = {
                    when (message.dialogType) {
                        DialogType.nextTaskDialog -> {
                            viewModel.showNextQuestion(requireActivity() as MainActivity)
                        }

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
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                when (viewModel.check(
                    requireActivity() as MainActivity,
                    userAnswer = binding.userAnswer.text.toString()
                )) {
                    true -> SoundManager.playSoundCorrect(
                        requireActivity() as MainActivity,
                        viewModel.isSoundOn
                    )

                    false -> SoundManager.playSoundWrong(
                        requireActivity() as MainActivity,
                        viewModel.isSoundOn
                    )
                }
                binding.userAnswer.setText("")
            }
            buttonSkip.setOnClickListener {
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                viewModel.skipCurrentTask(requireActivity() as MainActivity)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setText() {
        val activity = requireActivity() as MainActivity
        binding.apply {
            explanation.text = viewModel.getText(activity, R.string.do_you_know_the_answer)
        }
    }
}