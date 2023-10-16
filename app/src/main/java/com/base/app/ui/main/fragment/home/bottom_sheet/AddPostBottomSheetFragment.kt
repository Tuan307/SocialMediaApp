package com.base.app.ui.main.fragment.home.bottom_sheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.databinding.FragmentAddPostBottomSheetBinding
import com.base.app.ui.add_post.PostActivity
import com.base.app.ui.add_video_post.AddVideoActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class AddPostBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddPostBottomSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_post_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            textPostImage.setOnClickListener {
                val intent = Intent(requireContext(), PostActivity::class.java)
                intent.putExtra("from", "home")
                startActivity(intent)
                dismiss()
            }
            textPostVideo.setOnClickListener {
                startActivity(Intent(requireContext(), AddVideoActivity::class.java))
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance() = AddPostBottomSheetFragment()
    }
}