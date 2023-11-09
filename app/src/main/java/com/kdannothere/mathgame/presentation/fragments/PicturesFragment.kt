package com.kdannothere.mathgame.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kdannothere.mathgame.R
import com.kdannothere.mathgame.databinding.FragmentPicturesBinding
import com.kdannothere.mathgame.presentation.GameViewModel
import com.kdannothere.mathgame.presentation.MainActivity
import com.kdannothere.mathgame.presentation.elements.picture.PictureAdapter

class PicturesFragment : Fragment() {

    private var _binding: FragmentPicturesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var pictureAdapter: PictureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPicturesBinding.inflate(inflater, container, false)
        setText()
        pictureAdapter = PictureAdapter(viewModel.pictureList)
        binding.pictures.adapter = pictureAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setText() {
        binding.apply {

            val activity = requireActivity() as MainActivity

            title.text = viewModel.getText(activity, R.string.your_pictures)
        }
    }
}