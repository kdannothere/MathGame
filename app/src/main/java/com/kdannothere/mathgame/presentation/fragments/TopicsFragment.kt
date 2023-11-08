package com.kdannothere.mathgame.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentTopicsBinding
import com.kdannothere.mathgame.managers.LangManager
import com.kdannothere.mathgame.presentation.util.operationAddition
import com.kdannothere.mathgame.presentation.util.operationDivision
import com.kdannothere.mathgame.presentation.util.operationMultiplication
import com.kdannothere.mathgame.presentation.util.operationSubtraction
import com.kdannothere.mathgame.presentation.GameViewModel

class TopicsFragment : Fragment() {

    private var _binding: FragmentTopicsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTopicsBinding.inflate(inflater, container, false)
        setText()
        setClickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.apply {
            buttonAddition.setOnClickListener {
                viewModel.createLevelList(operationAddition)
                findNavController().navigate(R.id.action_topics_to_levels)
            }
            buttonSubtraction.setOnClickListener {
                viewModel.createLevelList(operationSubtraction)
                findNavController().navigate(R.id.action_topics_to_levels)
            }
            buttonMultiplication.setOnClickListener {
                viewModel.createLevelList(operationMultiplication)
                findNavController().navigate(R.id.action_topics_to_levels)
            }
            buttonDivision.setOnClickListener {
                viewModel.createLevelList(operationDivision)
                findNavController().navigate(R.id.action_topics_to_levels)
            }
        }
    }

    private fun setText() {
        binding.apply {

            val localizedContext: Context =
                LangManager.getLocalizedContext(requireContext(), viewModel.languageCode)

            titleTopics.text = localizedContext.getString(R.string.title_topics)
            buttonAddition.text = localizedContext.getString(R.string.title_addition)
            buttonSubtraction.text = localizedContext.getString(R.string.title_subtraction)
            buttonMultiplication.text = localizedContext.getString(R.string.title_multiplication)
            buttonDivision.text = localizedContext.getString(R.string.title_division)
        }
    }
}