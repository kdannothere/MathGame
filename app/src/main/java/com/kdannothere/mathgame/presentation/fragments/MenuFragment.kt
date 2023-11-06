package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentMenuBinding
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.loadSettings(activity as MainActivity)
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        setClickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.apply {
            buttonPlay.setOnClickListener { findNavController().navigate(R.id.action_menu_to_topics) }
            buttonPictures.setOnClickListener { findNavController().navigate(R.id.action_menu_to_pictures) }
            buttonSettings.setOnClickListener { findNavController().navigate(R.id.action_menu_to_settings) }
        }
    }
}