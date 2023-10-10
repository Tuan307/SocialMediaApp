package com.base.app.ui.group.your_group

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.base.app.databinding.FragmentGroupYourGroupBinding

class GroupYourGroupFragment : Fragment() {
    private lateinit var binding: FragmentGroupYourGroupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGroupYourGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(): GroupYourGroupFragment {
            return GroupYourGroupFragment()
        }
    }
}