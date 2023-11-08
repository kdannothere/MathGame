package com.kdannothere.mathgame.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentSettingsBinding
import com.kdannothere.mathgame.managers.DataManager
import com.kdannothere.mathgame.managers.LangManager
import com.kdannothere.mathgame.managers.SoundManager
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.MathApp
import com.kdannothere.mathgame.presentation.GameViewModel
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
                musicValue.isChecked = !viewModel.isMusicOn
                viewModel.isMusicOn = musicValue.isChecked
                lifecycleScope.launch(MathApp.dispatcherIO) {
                    DataManager.saveMusicSetting(requireContext(), viewModel.isMusicOn)
                    when (viewModel.isMusicOn) {
                        true -> {
                            SoundManager.playMusic(
                                mediaPlayer = (activity as MainActivity).musicPlayer,
                                requireContext(),
                                lifecycleScope
                            )
                        }

                        false -> {
                            SoundManager.pauseMusic(
                                mediaPlayer = (activity as MainActivity).musicPlayer,
                                lifecycleScope
                            )
                        }
                    }

                }
            }
            soundValue.setOnClickListener {
                musicValue.isChecked = !viewModel.isSoundOn
                viewModel.isSoundOn = soundValue.isChecked
                lifecycleScope.launch(MathApp.dispatcherIO) {
                    DataManager.saveSoundSetting(requireContext(), viewModel.isSoundOn)
                }
            }
            languageValue.setOnClickListener {
                viewModel.changeLanguage(requireContext())
                setText()
            }
        }
    }

    private fun setText() {
        binding.apply {

            val localizedContext: Context =
                LangManager.getLocalizedContext(requireContext(), viewModel.languageCode)

            titleSettings.text = localizedContext.getString(R.string.button_settings_text)
            music.text = localizedContext.getString(R.string.music)
            sound.text = localizedContext.getString(R.string.sounds)
            language.text = localizedContext.getString(R.string.language)
            languageValue.text = localizedContext.getString(R.string.language_current)
        }
    }
}