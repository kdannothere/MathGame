package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentLevelsBinding
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.elements.level.LevelAdapter

class LevelsFragment : Fragment() {

    private var _binding: FragmentLevelsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var levelAdapter: LevelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentLevelsBinding.inflate(inflater, container, false)
        setText()
        levelAdapter = LevelAdapter(viewModel.levelList
        ) { level ->
            viewModel.apply {
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
                updateTaskList(level.id)
                currentLevel = level.id
            }
            findNavController().navigate(R.id.action_levels_to_game)
        }
        binding.levels.adapter = levelAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.result.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            titleLevels.text = viewModel.getText(activity, R.string.title_levels)
        }
    }
}