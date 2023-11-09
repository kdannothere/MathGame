package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentMenuBinding
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import kotlin.system.exitProcess

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        setText()
        setClickListeners()
        setCallbackToExitApp()

        return binding.root
    }

    private fun setCallbackToExitApp() {
        requireActivity().apply {
            onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
                val currentFragment = navHostFragment.childFragmentManager.fragments[0]
                if (currentFragment is MenuFragment) {
                    // If you're currently viewing the MenuFragment, don't go back to the LoadingFragment
                    exitProcess(0)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setClickListeners() {
        binding.apply {
            buttonPlay.setOnClickListener {
                findNavController().navigate(R.id.action_menu_to_topics)
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
            }
            buttonPictures.setOnClickListener {
                findNavController().navigate(R.id.action_menu_to_pictures)
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
            }
            buttonSettings.setOnClickListener {
                findNavController().navigate(R.id.action_menu_to_settings)
                SoundManager.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
            }
        }
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            buttonPlay.text = viewModel.getText(activity, R.string.button_play_text)
            buttonPictures.text = viewModel.getText(activity, R.string.pictures)
            buttonSettings.text = viewModel.getText(activity, R.string.button_settings_text)
        }
    }
}