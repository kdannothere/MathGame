package com.kdannothere.mathgame.presentation.fragments

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
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FragmentGame : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        setClickListeners()

        viewModel.currentTask.onEach { task ->
            binding.question.text = task.question
        }.launchIn(lifecycleScope)

        viewModel.pictureParts.onEach { currentNumber ->
            binding.parts.text = "${currentNumber}/4"
        }.launchIn(lifecycleScope)

        return binding.root
    }

    private fun setClickListeners() {
        binding.apply {
            buttonCheck.setOnClickListener {
                viewModel.check(
                    requireActivity(),
                    userAnswer = binding.userAnswer.text.toString()
                )
                binding.userAnswer.setText("")
            }
            buttonPictures.setOnClickListener {
                findNavController().navigate(R.id.action_game_to_pictures)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}