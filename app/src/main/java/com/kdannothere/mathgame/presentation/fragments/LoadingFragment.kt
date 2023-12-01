package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.data.GameDatabase
import com.kdannothere.mathgame.data.record.RecordRepository
import com.kdannothere.mathgame.databinding.FragmentLoadingBinding
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.managers.DialogMng
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class LoadingFragment : Fragment() {

    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val activity = requireActivity()
        val dao = GameDatabase.getDatabase(activity).getRecordDao()
        val repository = RecordRepository(dao)
        val factory = GameViewModelFactory(repository)

        viewModel = ViewModelProvider(activity, factory)[GameViewModel::class.java]
        viewModel.loadSettings(requireActivity() as MainActivity)
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)

        viewModel.message.onEach { message ->
            DialogMng.showDialog(
                message.text,
                message.dialogType,
                requireActivity(),
                closeDialog = { viewModel.closeDialog() }
            )
        }.launchIn(lifecycleScope)

        viewModel.isLoading.onEach { isLoading ->
            when (isLoading) {
                true -> {
                    return@onEach
                }
                false -> {
                    delay(500)
                    findNavController().navigate(R.id.action_loading_to_menu)
                }
            }
        }.launchIn(lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}