package com.base.app.ui.main.fragment.reel

import com.base.app.R
import com.base.app.base.fragment.BaseFragment
import com.base.app.databinding.FragmentReelBinding


class ReelFragment : BaseFragment<FragmentReelBinding>() {

    companion object {
        fun newInstance(): ReelFragment {
            return ReelFragment()
        }
    }

    override fun getContentLayout(): Int {
        return R.layout.fragment_reel
    }

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun observerLiveData() {
    }

}