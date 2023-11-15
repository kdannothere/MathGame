package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.databinding.FragmentHistoryBinding
import com.kdannothere.mathgame.presentation.elements.record.RecordAdapter
import com.kdannothere.mathgame.presentation.managers.LangMng
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private lateinit var recordAdapter: RecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        setText()
        recordAdapter = RecordAdapter(
            recordList = viewModel.records.value,
            localizedContext = LangMng.getLocalizedContext(
                requireActivity(),
                viewModel.languageCode
            )
        )
        viewModel.updateRecords(viewModel.dateFrom, viewModel.dateTo)

        viewModel.records.onEach { list: List<Record> ->
            recordAdapter.updateData(list)
        }.launchIn(lifecycleScope)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recordAdapter.localizedContext = null
        _binding = null
    }

    private fun setText() {
        binding.apply {

            //explanation.text = viewModel.getText(requireActivity(), R.string.solve_the_problem)
        }
    }
}