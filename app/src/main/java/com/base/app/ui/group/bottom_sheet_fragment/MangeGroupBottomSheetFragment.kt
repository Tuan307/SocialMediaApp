package com.base.app.ui.group.bottom_sheet_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.base.app.R
import com.base.app.databinding.FragmentMangeGroupBottomSheetBinding
import com.base.app.ui.group.detail_group.GroupAllRequestActivity
import com.base.app.ui.group.update_group.UpdateGroupActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MangeGroupBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentMangeGroupBottomSheetBinding? = null
    val binding: FragmentMangeGroupBottomSheetBinding
        get() = _binding!!
    private val viewModel by viewModels<ManageBottomSheetViewModel>()
    private var groupId: Long? = 0.toLong()
    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mange_group_bottom_sheet,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val bundle = arguments
            groupId = bundle?.getLong("groupId", 0)
            if (bundle?.getString("from") == "manage_group") {
                cardEditGroup.visibility = View.VISIBLE
                textEditGroup.visibility = View.VISIBLE
                cardDeleteGroup.visibility = View.VISIBLE
                textDeleteGroup.visibility = View.VISIBLE
                cardVerifyGroup.visibility = View.VISIBLE
                textVerifyGroup.visibility = View.VISIBLE
                cardLogOut.visibility = View.GONE
                textLogOut.visibility = View.GONE
            } else {
                cardEditGroup.visibility = View.GONE
                textEditGroup.visibility = View.GONE
                cardDeleteGroup.visibility = View.GONE
                textDeleteGroup.visibility = View.GONE
                cardVerifyGroup.visibility = View.GONE
                textVerifyGroup.visibility = View.GONE
                cardLogOut.visibility = View.VISIBLE
                textLogOut.visibility = View.VISIBLE
            }
            cardEditGroup.setOnClickListener {
                val intent = Intent(requireContext(), UpdateGroupActivity::class.java)
                intent.putExtra("groupId", groupId)
                startActivity(intent)
            }
            cardDeleteGroup.setOnClickListener {
                groupId?.let { it1 -> viewModel.deleteGroup(it1) }
            }
            cardVerifyGroup.setOnClickListener {
                val intent = Intent(requireContext(), GroupAllRequestActivity::class.java)
                intent.putExtra("groupId", groupId)
                startActivity(intent)
            }
        }
        observeData()
    }

    private fun observeData() = with(viewModel) {
        deleteGroupResponse.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Xóa nhóm thành công", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Đã có lỗi xảy ra, vui lòng thử lại",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = MangeGroupBottomSheetFragment()
    }
}