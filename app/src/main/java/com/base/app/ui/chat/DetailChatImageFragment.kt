package com.base.app.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.base.app.databinding.FragmentDetailChatImageBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailChatImageFragment : Fragment() {
    private lateinit var binding: FragmentDetailChatImageBinding
    private val args: DetailChatImageFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailChatImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(args.chatUrl).into(binding.imageDetail)
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}