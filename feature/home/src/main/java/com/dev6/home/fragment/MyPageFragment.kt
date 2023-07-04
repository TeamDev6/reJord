package com.dev6.home.fragment
import android.util.Log
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dev6.common.uistate.UiState
import com.dev6.core.BindingFragment
import com.dev6.core.enums.WriteType
import com.dev6.core.util.extension.repeatOnStarted
import com.dev6.domain.model.mypage.MyData
import com.dev6.home.AppBarStateChangeListener
import com.dev6.home.R
import com.dev6.home.adapter.MyPageContentPagerAdapter
import com.dev6.home.databinding.FragmentMyPageBinding
import com.dev6.home.viewmodel.BoardViewModel
import com.dev6.home.viewmodel.ChallengeViewModel
import com.dev6.home.viewmodel.MyPageViewModel
import com.dev6.write.viewmodel.WriteViewModel
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment() : BindingFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {
    private val myPageViewModel: MyPageViewModel by activityViewModels()

    override fun initView() {
        super.initView()
    }

    override fun initViewModel() {
        super.initViewModel()
        repeatOnStarted {
            myPageViewModel.getMyData()
        }

    }

    override fun initListener() {
        super.initListener()
        myInfoEdit()
        myBadgeData()
        myFootPrintData()
        binding.mypageContent.adapter = MyPageContentPagerAdapter(this)
        binding.mypageContent.isSaveEnabled = false
        TabLayoutMediator(binding.tableLayout, binding.mypageContent){ tab, position ->
            if (position == 0) {
                tab.text = "챌린지 후기"
            } else {
                tab.text = "게시판"
            }
        }.attach()

        binding.appbarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if(state == State.COLLAPSED){
                    binding.pinNameTv.visibility = View.VISIBLE
                }else{
                    binding.pinNameTv.visibility = View.INVISIBLE
                }
            }
        })

        binding.myInfoEdit.setOnClickListener {
            findNavController().navigate(R.id.action_global_userInfoFragment)
        }
    }

    override fun afterViewCreated() {
        super.afterViewCreated()
        repeatOnStarted {
            myPageViewModel.myPageFlow.collect{event->
                    if(event is MyPageViewModel.MyPageEvent.GetMyData){
                        when(event.uiState){
                            is UiState.Success -> {
                                myPageViewModel.userNickName = event.uiState.data.nickname
                                initMyData(event.uiState.data)
                                this.cancel()
                            }
                            is UiState.Loding -> {

                            }
                            is UiState.Error -> {
                                Log.v("MyPage Error", event.toString())
                            }
                        }
                    }

            }
        }

        repeatOnStarted {
            myPageViewModel.myEditFlow.collect{event->
                when(event){
                    is MyPageViewModel.MyEditEvent.deletePostEvent->{
                        when (event.uiState) {
                            is UiState.Success -> {
                                Log.v("asdfasdfs", "agwegegegegegeg")
                                lifecycleScope.launch(Dispatchers.IO) {
                                    launch {
                                        myPageViewModel.postRefreshFlag(true)
                                    }.join()
                                    launch {
                                        myPageViewModel.getPostListWithUid(0, 5)
                                    }.join()
                                }
                            }
                            is UiState.Loding -> {

                            }
                            is UiState.Error -> {

                            }

                        }
                    }
                    is MyPageViewModel.MyEditEvent.deleteChallengeEvent ->{
                    when (event.uiState) {
                        is UiState.Success -> {
                            lifecycleScope.launch(Dispatchers.IO) {
                                launch {
                                    myPageViewModel.challengeRefreshFlag(true)
                                }.join()
                                launch {
                                    myPageViewModel.getChaalengeListWithUid(0, 5)
                                }.join()
                            }
                        }
                        is UiState.Loding -> {

                        }
                        is UiState.Error -> {

                        }

                    }
                }
                    is MyPageViewModel.MyEditEvent.editPostEvent ->{
                        when (event.uiState) {
                            is UiState.Success -> {
                                lifecycleScope.launch {
                                    lifecycleScope.launch(Dispatchers.IO) {
                                        launch {
                                            myPageViewModel.postRefreshFlag(true)
                                        }.join()
                                        launch {
                                            myPageViewModel.getPostListWithUid(0, 5)
                                        }.join()
                                    }
                                }
                            }
                            is UiState.Loding -> {
                                Log.v("에딧포스트","로딩")
                            }
                            is UiState.Error -> {
                                Log.v("에딧포스트", event.uiState.error.toString())
                            }
                        }
                    }
                     is MyPageViewModel.MyEditEvent.editChallengeEvent->{
                        when (event.uiState) {
                            is UiState.Success -> {
                                Log.v("에딧캘린지","ㅇㅇ")
                                lifecycleScope.launch(Dispatchers.IO) {
                                    launch {
                                        myPageViewModel.challengeRefreshFlag(true)
                                    }.join()
                                    launch {
                                        myPageViewModel.getChaalengeListWithUid(0, 5)
                                    }.join()
                                }
                            }
                            is UiState.Loding -> {

                            }
                            is UiState.Error -> {
                                Log.v("에딧캘린지", event.uiState.error.toString())
                            }

                        }
                    }
                }
            }
        }
    }

    private fun initMyData(data: MyData){
        binding.apply {
            myPageNickName.text =  data.nickname
            myPageSubText.text = "리욜드와 함께 지구를 지킨지\n${data.dday}일 되었어요:)"
            badgeCount.text = "${data.badgeAmount}개"
            footPrintCount.text = "${data.totalFootprintAmount}개"
            pinNameTv.text = data.nickname
        }
    }

    private fun myInfoEdit(){
        binding.myInfoEdit.setOnClickListener {

        }
    }
    private fun myBadgeData(){
        binding.badgeCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_myPageBadgeFragment)
        }
    }
    private fun myFootPrintData(){
        binding.footPrintCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_footPrintFragment)
        }
    }
}