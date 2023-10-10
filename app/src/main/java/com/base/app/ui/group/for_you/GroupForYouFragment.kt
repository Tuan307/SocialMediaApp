package com.base.app.ui.group.for_you

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.base.app.databinding.FragmentGroupForYouBinding
import com.base.app.ui.main.fragment.home.HomeFragment

class GroupForYouFragment : Fragment() {
    private lateinit var binding: FragmentGroupForYouBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupForYouBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object {
        fun newInstance(): GroupForYouFragment {
            return GroupForYouFragment()
        }
    }
}