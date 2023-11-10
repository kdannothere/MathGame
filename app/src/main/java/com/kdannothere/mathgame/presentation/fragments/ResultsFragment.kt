package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentResultsBinding
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        setText()
        setData()
        setClickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setData() {
        binding.apply {
            correctValue.text = viewModel.result.correct.toString()
            mistakesValue.text = viewModel.result.mistakes.toString()
            skippedValue.text = viewModel.result.skipped.toString()
        }
    }

    private fun setClickListeners() {
        binding.apply {

            // Analogous to when the user presses the system Back button
            buttonAllLevels.setOnClickListener {
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
                findNavController().popBackStack()
            }

            buttonRestart.setOnClickListener {
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
                viewModel.restartLevel()
                viewModel.result.clear()
                findNavController().navigate(R.id.action_results_to_game)
            }

            buttonNextLevel.apply {
                when (viewModel.isLastLevel()) {
                     true -> {
                         // hide button
                        visibility = View.GONE
                    }
                    false -> {
                        setOnClickListener {
                            SoundManager.playSoundClick(
                                requireActivity() as MainActivity,
                                viewModel.isSoundOn
                            )
                            ++viewModel.currentLevel
                            viewModel.updateTaskList(viewModel.currentLevel)
                            viewModel.result.clear()
                            findNavController().navigate(R.id.action_results_to_game)
                        }
                    }
                }
            }
        }
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            titleResults.text = viewModel.getText(activity, R.string.title_results)
            correct.text = viewModel.getText(activity, R.string.correct)
            mistakes.text = viewModel.getText(activity, R.string.mistakes)
            skipped.text = viewModel.getText(activity, R.string.skipped)
        }
    }
}