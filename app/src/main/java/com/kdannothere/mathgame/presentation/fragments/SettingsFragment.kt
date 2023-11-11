package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentSettingsBinding
import com.kdannothere.mathgame.presentation.managers.DataMng
import com.kdannothere.mathgame.presentation.managers.SoundMng
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.MathApp
import kotlinx.coroutines.launch


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<GameViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        setText()
        setClickListeners()
        updateSettings()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateSettings() {
        binding.apply {
            musicValue.isChecked = viewModel.isMusicOn
            soundValue.isChecked = viewModel.isSoundOn
        }
    }

    private fun setClickListeners() {
        binding.apply {
            musicValue.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                val newValue = !viewModel.isMusicOn
                musicValue.isChecked = newValue
                viewModel.isMusicOn = newValue
                lifecycleScope.launch(MathApp.dispatcherIO) {
                    DataMng.saveMusicSetting(requireContext(), newValue)
                    when (newValue) {
                        true -> {
                            SoundMng.playMusic(requireActivity() as MainActivity)
                        }

                        false -> {
                            SoundMng.pauseMusic(requireActivity() as MainActivity)
                        }
                    }

                }
            }
            soundValue.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )

                val newValue = !viewModel.isSoundOn
                soundValue.isChecked = newValue
                viewModel.isSoundOn = newValue
                lifecycleScope.launch(MathApp.dispatcherIO) {
                    DataMng.saveSoundSetting(requireContext(), newValue)
                }
            }
            languageValue.setOnClickListener {
                SoundMng.playSoundClick(
                    requireActivity() as MainActivity,
                    viewModel.isSoundOn
                )
                viewModel.changeLanguage(requireContext())
                setText()
            }
        }
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            titleSettings.text = viewModel.getText(activity, R.string.button_settings_text)
            music.text = viewModel.getText(activity, R.string.music)
            sound.text = viewModel.getText(activity, R.string.sounds)
            language.text = viewModel.getText(activity, R.string.language)
            languageValue.text = viewModel.getText(activity, R.string.language_current)
        }
    }
}