package com.dev6.home.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dev6.common.uistate.UiState
import com.dev6.core.BindingFragment
import com.dev6.core.util.extension.repeatOnStarted
import com.dev6.domain.model.challenge.ChallengeReviewResult
import com.dev6.domain.model.post.read.Content
import com.dev6.home.R
import com.dev6.home.adapter.BoardRecyclerAdapter
import com.dev6.home.adapter.ChallengeRecyclerAdapter
import com.dev6.home.bottomsheet.OptionBottomSheetFragment
import com.dev6.home.databinding.FragmentMyPageBoardBinding
import com.dev6.home.viewmodel.MyPageViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageBoardFragment :
    BindingFragment<FragmentMyPageBoardBinding>(R.layout.fragment_my_page_board) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private lateinit var MyBoardRc: RecyclerView
    private lateinit var boardRecyclerAdapter: BoardRecyclerAdapter
    private var mutableList: MutableList<Content> = arrayListOf()
    private var count = 0
    private var index = 0
    lateinit var bottomSheet : BottomSheetDialogFragment
    override fun initView() {
        super.initView()
        //recyclerView init
        myPageViewModel.clearBoardCount()
        MyBoardRc = binding.MyBoardRc

        //잘되는지 확인필요
        MyBoardRc.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
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
            myPageViewModel.getPostListWithUid(0, 5)
        }

        myPageViewModel.postRefreshFlag.observe(viewLifecycleOwner){
            if(it){
                Log.v("어어어","에")
                index =0
                mutableList.clear()
                myPageViewModel.clearBoardCount()
                boardRecyclerAdapter.notifyDataSetChanged()
                myPageViewModel.postRefreshFlag(false)
            }
        }
    }

    override fun initListener() {
        super.initListener()
    }

    override fun afterViewCreated() {
        super.afterViewCreated()
        repeatOnStarted {
            myPageViewModel.myPageBoardFlow.collect{ event->
                if(event is MyPageViewModel.BoardEvent.GetPostListWithUid ){
                    when (event.uistate) {
                        is UiState.Success -> {
                            Log.v("GetPostListWithUid test", event.uistate.data.toString())
                            mutableList.addAll(index, event.uistate.data.content)
                            boardRecyclerAdapter = BoardRecyclerAdapter("MYBOARD",mutableList, {

                            },
                                {
                                    if (event.uistate.data.totalElements > myPageViewModel.myBoardCount * 5) {
                                        Log.v("GetPostListWithUid22", event.uistate.data.toString())
                                        index = it
                                        myPageViewModel.plusBoardCount()
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            myPageViewModel.getPostListWithUid(
                                                myPageViewModel.myBoardCount, 5)
                                        }

                                    }
                                }
                                ,{
                                    bottomSheet =  OptionBottomSheetFragment()
                                    val args = Bundle()
                                    args.putString("type", "POST")
                                    args.putSerializable("data", it)
                                    bottomSheet.arguments = args
                                    bottomSheet.show(parentFragmentManager , bottomSheet.tag)
                                })
                            MyBoardRc.apply {
                                adapter = boardRecyclerAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }
                        is UiState.Loding -> {

                        }
                        is UiState.Error -> {

                        }
                    }
                }
            }
        }
    }
}