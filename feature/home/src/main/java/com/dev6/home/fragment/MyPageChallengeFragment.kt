package com.dev6.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev6.common.uistate.UiState
import com.dev6.core.BindingFragment
import com.dev6.core.util.extension.repeatOnStarted
import com.dev6.domain.model.challenge.ChallengeReviewResult
import com.dev6.home.R
import com.dev6.home.adapter.ChallengeRecyclerAdapter
import com.dev6.home.bottomsheet.OptionBottomSheetFragment
import com.dev6.home.databinding.FragmentMyPageChallengeBinding
import com.dev6.home.viewmodel.MyPageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageChallengeFragment :
    BindingFragment<FragmentMyPageChallengeBinding>(R.layout.fragment_my_page_challenge) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private lateinit var MychallengeRc: RecyclerView
    private lateinit var challengeRecyclerAdapter: ChallengeRecyclerAdapter
    private var mutableList: MutableList<ChallengeReviewResult> = arrayListOf()
    private var count = 0
    private var index = 0
    lateinit var bottomSheet : BottomSheetDialogFragment
    override fun initView() {
        super.initView()
        myPageViewModel.clearChallCount()
        MychallengeRc = binding.MychallengeRc

        //확인필요
        MychallengeRc.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                when (action) {
                    MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    override fun initViewModel() {
        super.initViewModel()
        repeatOnStarted {
            myPageViewModel.getChaalengeListWithUid(0, 5)
        }
        myPageViewModel.challengeRefreshFlag.observe(viewLifecycleOwner){
            if(it){
                index =0
                mutableList.clear()
                myPageViewModel.clearChallCount()
                challengeRecyclerAdapter.notifyDataSetChanged()
                myPageViewModel.challengeRefreshFlag(false)
            }
        }
    }

    override fun initListener() {
        super.initListener()

    }

    override fun afterViewCreated() {
        super.afterViewCreated()
        repeatOnStartedFragment {
            myPageViewModel.myPageChallengeFlow.collect{ event ->
                if(event is MyPageViewModel.ChallEvent.GetChallengeListWithUid){
                    when (event.uistate) {
                        is UiState.Success -> {
                            mutableList.addAll(index, event.uistate.data.content)
                            challengeRecyclerAdapter = ChallengeRecyclerAdapter("MYPAGE",mutableList,
                                {  //클릭 이벤트

                                },
                                {
                                    if (event.uistate.data.totalElements > myPageViewModel.myChallCount * 5) {
                                        Log.v("GetChallengeListWithUid", event.uistate.data.toString())
                                        index = it
                                        myPageViewModel.plusChallCount()
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            myPageViewModel.getChaalengeListWithUid(
                                                myPageViewModel.myChallCount, 5)
                                        }

                                    }
                                },{
                                    bottomSheet =  OptionBottomSheetFragment()
                                    val args = Bundle()
                                    args.putString("type", "CHALLENGE")
                                    args.putSerializable("data", it)
                                    bottomSheet.arguments = args
                                    bottomSheet.show(parentFragmentManager , bottomSheet.tag)
                                })
                            MychallengeRc.apply {
                                adapter = challengeRecyclerAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                        is UiState.Loding -> {
                            Log.v("GetChallengeListWithUid", "로딩")
                        }
                        is UiState.Error -> {
                            Log.v("GetChallengeListWithUid", event.uistate.error.toString())
                        }
                    }
                }
            }
        }
    }


}