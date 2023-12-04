package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.data.record.Record
import com.kdannothere.mathgame.databinding.FragmentHistoryBinding
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.elements.record.RecordAdapter
import com.kdannothere.mathgame.presentation.managers.LangMng
import com.kdannothere.mathgame.presentation.managers.TimeMng
import com.kdannothere.mathgame.presentation.viewmodel.GameViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Calendar

class HistoryFragment : Fragment() {

    private lateinit var recordAdapter: RecordAdapter

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels()

    private val calendar = Calendar.getInstance()
    private val today = TimeMng.getDateToday()
    private val yesterday = TimeMng.getDateYesterday()

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
        binding.records.adapter = recordAdapter

        viewModel.date.onEach {
            viewModel.updateRecords()
            updateDateText()
        }.launchIn(lifecycleScope)

        viewModel.records.onEach { list: List<Record> ->
            recordAdapter.updateData(list)
        }.launchIn(lifecycleScope)

        setInitialData()

        setClickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recordAdapter.localizedContext = null
        _binding = null
    }

    private fun setClickListeners() {

        binding.dayBefore.setOnClickListener {
            viewModel.changeDate(TimeMng.getDateOneDayBefore(calendar))
        }

        binding.dayAfter.setOnClickListener {
            viewModel.changeDate(TimeMng.getDateOneDayAfter(calendar))
        }

        binding.calendar.setOnClickListener {
            TimeMng.showDialog(calendar, requireActivity(), viewModel::changeDate)
        }
    }

    private fun setInitialData() {
        val todayDate = TimeMng.getDateToday()
        viewModel.changeDate(todayDate)
    }

    private fun updateDateText() {
        binding.date.text = when (viewModel.date.value) {
            today -> viewModel.getText(requireActivity() as MainActivity, R.string.today)
            yesterday -> viewModel.getText(requireActivity() as MainActivity, R.string.yesterday)
            else -> viewModel.date.value
        }
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            hint.text = viewModel.getText(activity, R.string.choose_date)
            date.text = viewModel.getText(activity, R.string.today)
        }
    }
}