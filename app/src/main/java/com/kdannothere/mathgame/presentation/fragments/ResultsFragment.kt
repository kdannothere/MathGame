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
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
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
            correctValue.text = viewModel.results.correct.toString()
            mistakesValue.text = viewModel.results.mistakes.toString()
            skippedValue.text = viewModel.results.skipped.toString()
        }
    }

    private fun setClickListeners() {
        binding.apply {

            // Analogous to when the user presses the system Back button
            buttonAllLevels.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonNextLevel.apply {
                when (viewModel.levelList.last().id == viewModel.currentLevelId) { // is last level?
                     true -> {
                         // hide button
                        visibility = View.GONE
                    }
                    false -> {
                        setOnClickListener {
                            ++viewModel.currentLevelId
                            viewModel.updateTaskList(viewModel.currentLevelId)
                            viewModel.results.clear()
                            findNavController().navigate(R.id.action_results_to_game)
                        }
                    }
                }
            }
        }
    }
}