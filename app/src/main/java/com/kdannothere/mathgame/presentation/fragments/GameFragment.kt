package com.kdannothere.mathgame.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentGameBinding
import com.kdannothere.mathgame.presentation.elements.dialog.DialogType
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.managers.DialogMng
import com.kdannothere.mathgame.presentation.managers.SoundMng
import com.kdannothere.mathgame.presentation.util.Util
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
        binding.lvlValue.text = viewModel.currentLevel.toString()
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

                        DialogType.basicDialog -> {} // do not touch this line

                        else -> findNavController().navigate(R.id.action_game_to_results)
                    }
                    SoundMng.playSoundClick(
                        requireActivity() as MainActivity,
                        viewModel.isSoundOn
                    )
                }
            )
        }.launchIn(lifecycleScope)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.userAnswer.postDelayed({
            binding.userAnswer.let {
                it.isFocusableInTouchMode = true
                it.requestFocus()
                val imm =
                    it.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
            }
        }, 200)
    }

    private fun setClickListeners() {
        binding.apply {


            buttonCheck.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                when (viewModel.check(
                    requireActivity() as MainActivity,
                    userAnswer = binding.userAnswer.text.toString()
                )) {
                    true -> SoundMng.playSoundCorrect(
                        requireActivity() as MainActivity,
                        viewModel.isSoundOn
                    )

                    false -> SoundMng.playSoundWrong(
                        requireActivity() as MainActivity,
                        viewModel.isSoundOn
                    )
                }
                binding.userAnswer.setText("")
            }
            buttonSkip.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                viewModel.skipCurrentTask(requireActivity() as MainActivity)
            }

            buttonAllLevels.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
                Util.hideSoftKeyboard(binding.userAnswer, this@GameFragment)
                findNavController().popBackStack()
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
            explanation.text = viewModel.getText(activity, R.string.task)
        }
    }
}