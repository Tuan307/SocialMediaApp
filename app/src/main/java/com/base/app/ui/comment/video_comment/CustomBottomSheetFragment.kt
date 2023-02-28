package com.base.app.ui.comment.video_comment

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.app.R
import com.base.app.data.models.Comment
import com.base.app.databinding.BottomSheetCommentBinding
import com.base.app.ui.comment.CommentViewModel
import com.base.app.ui.comment.adapter.CommentAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CustomBottomSheetFragment : BottomSheetDialogFragment() {
    companion object {
        const val TAG = "ActionBottomDialog"
        fun newInstance(): CustomBottomSheetFragment {

            return CustomBottomSheetFragment()
        }
    }

    private var list: ArrayList<Comment> = ArrayList()
    private lateinit var binding: BottomSheetCommentBinding
    private lateinit var commentAdapter: CommentAdapter
    private val viewModel by viewModels<CommentViewModel>()
    private var videoId = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.bottom_sheet_comment, container, false
        )
        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val dm = Resources.getSystem().displayMetrics
//        val rect = dm.run { Rect(0, 0, heightPixels, widthPixels) }
//        view.minimumHeight = rect.height()
        binding.apply {
            rcvVideoComeent.layoutManager = LinearLayoutManager(requireContext())
            rcvVideoComeent.setHasFixedSize(true)
            commentAdapter = CommentAdapter(list, requireContext(), viewModel)
            rcvVideoComeent.adapter = commentAdapter
        }
        val bundle = this.arguments
        if (bundle != null) {
            videoId = bundle.getString("videoId").toString()
            viewModel.getVideoComment(videoId)
        }
        initListener()
        observeData()
    }

    private fun initListener() {
        binding.apply {
            btnPostComment.setOnClickListener {
                val comment = edtVideoComment.text.toString().trim()
                if (comment.isEmpty() || comment == "") {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.strInputVideoComment),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.addVideoComments(videoId, comment)
                    edtVideoComment.text.clear()
                }
            }
        }
    }


    private fun observeData() {
        viewModel.getVideoCommentResponse.observe(this@CustomBottomSheetFragment) {
            list.clear()
            list.addAll(it)
            commentAdapter.notifyDataSetChanged()
            binding.rcvVideoComeent.scrollToPosition(list.size - 1)
        }
        viewModel.addVideoCommentResponse.observe(this@CustomBottomSheetFragment) {
            if (!it) {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.str_error),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.str_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        val window: Window? = dialog.window

        if (window != null) {
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        }
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}