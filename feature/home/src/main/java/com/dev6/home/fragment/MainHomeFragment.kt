package com.dev6.home.fragment

import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.dev6.common.uistate.UiState
import com.dev6.core.BindingFragment
import com.dev6.core.enums.ScrollType
import com.dev6.core.enums.WriteType
import com.dev6.core.util.extension.repeatOnStarted
import com.dev6.domain.model.challenge.ChallengeInfoRes
import com.dev6.domain.model.challenge.ChallengeReadReq
import com.dev6.domain.model.post.read.PostReadReq
import com.dev6.domain.model.post.write.ChallengeWriteRes
import com.dev6.domain.model.post.write.PostWriteRes
import com.dev6.home.adapter.HomeContentPagerAdapter
import com.dev6.home.R
import com.dev6.home.databinding.FragmentHomeMainBinding
import com.dev6.home.viewmodel.BoardViewModel
import com.dev6.home.viewmodel.ChallengeViewModel
import com.dev6.home.viewmodel.MainViewModel
import com.dev6.home.viewmodel.MyPageViewModel
import com.dev6.write.WriteFragment
import com.dev6.write.viewmodel.WriteViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.time.LocalDateTime

@AndroidEntryPoint
class MainHomeFragment : BindingFragment<FragmentHomeMainBinding>(R.layout.fragment_home_main) {
    private val boardViewModel: BoardViewModel by activityViewModels()
    private val challengeViewModel: ChallengeViewModel by activityViewModels()
    val writeViewModel: WriteViewModel by activityViewModels()
    val mainViewModel: MainViewModel by activityViewModels()

    lateinit var bottomSheet: BottomSheetDialogFragment
    lateinit var challengeInfoRes: ChallengeInfoRes
    var writeType = ""
    lateinit var job: Job

    override fun initView() {
        super.initView()

        binding.challengeBanner.setOnClickListener {
            binding.challengeBanner.changeBanner(
                {
                    writeViewModel.changeWriteType("CHALLENGE")
                    bottomSheet = WriteFragment()
                    bottomSheet.show(parentFragmentManager, "")
                },
                challengeInfoRes
            )
        }

        boardViewModel.boardTabTypeFlag.observe(viewLifecycleOwner) {
            writeType = it.toString()
        }


        binding.pagerContent.adapter = HomeContentPagerAdapter(this@MainHomeFragment)
        binding.pagerContent.isSaveEnabled = false
        binding.tableLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        writeViewModel.updateCateGoryValue(WriteType.CHALLENGE)
                        boardViewModel.checkBoardTabType(WriteType.CHALLENGE)
                    }
                    1 -> {
                        writeViewModel.updateCateGoryValue(WriteType.SHARE)
                        boardViewModel.checkBoardTabType(WriteType.SHARE)
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        TabLayoutMediator(binding.tableLayout, binding.pagerContent) { tab, position ->
            if (position == 0) {
                tab.text = "챌린지 후기"
                writeViewModel.changeWriteType("CHALLENGE")
            } else {
                tab.text = "게시판"
                writeViewModel.changeWriteType("BOARD")
            }
        }.attach()

        repeatOnStartedFragment {
            mainViewModel.getBannerData()
        }

    }

    override fun initViewModel() {
        super.initViewModel()
        boardViewModel.scrollFlag.observe(viewLifecycleOwner) {
            when (it) {
                ScrollType.TOP -> {
                    binding.upFab.alpha = 1.0f
                }
                ScrollType.SCROLLUP -> {
                    binding.upFab.alpha = 1.0f
                }
                ScrollType.SCROLLDONW -> {
                    binding.upFab.alpha = 0.2f
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.upFab.apply {
            setOnClickListener {
                boardViewModel.upScroll()
                challengeViewModel.upScroll()
                binding.upFab.alpha = 1.0f
                binding.appbarLayout.setExpanded(true)
            }
        }
    }

    override fun afterViewCreated() {
        super.afterViewCreated()
        lifecycleScope.launch {
            mainViewModel.bannereventFlow.collect {
                eventHandler(it)
            }
        }

        repeatOnStartedFragment {
            writeViewModel.eventFlow.collect {
                writeEventHandler(it)
            }
        }
    }

    private fun eventHandler(event: MainViewModel.HomeEvent) {
        when (event) {
            is MainViewModel.HomeEvent.BannerEvent -> {
                when (event.uiState) {
                    is UiState.Success -> {
                        challengeInfoRes = event.uiState.data
                        initBanner(challengeInfoRes)
                    }
                    is UiState.Loding -> {

                    }
                    is UiState.Error -> {

                    }

                }
            }
        }
    }

    private fun writeEventHandler(event: WriteViewModel.Event) {
        when (event) {
            is WriteViewModel.Event.postWriteEvent -> {
                when (event.uiState) {
                    is UiState.Success -> {
                            lifecycleScope.launch(Dispatchers.IO) {
                                delay(1000)
                                launch {
                                    boardViewModel.postRefreshFlag(true)
                                }.join()
                                launch {
                                    boardViewModel.getPostList(
                                        PostReadReq(
                                            0,
                                            (event.uiState as UiState.Success<PostWriteRes>).data.postType,
                                            LocalDateTime.now().toString(),
                                            5
                                        )
                                    )
                                }.cancelAndJoin()
                        }
                    }
                    is UiState.Loding -> {

                    }
                    is UiState.Error -> {

                    }

                }
            }
            is WriteViewModel.Event.postChallegeEvent -> {
                when (event.uiState) {
                    is UiState.Success -> {
                            lifecycleScope.launch(Dispatchers.IO)  {
                                delay(1000)
                                Log.v("작성1","ㅇㅇ")
                                launch {
                                    Log.v("작성2","ㅇㅇ")
                                    challengeViewModel.postRefreshFlag(true)
                                }.join()
                                launch {
                                    Log.v("작성3","ㅇㅇ")
                                    challengeViewModel.getChallengeList(
                                        ChallengeReadReq(0, LocalDateTime.now().toString(), 5)
                                    )
                                }.cancelAndJoin()
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


    private fun initBanner(challengeInfoRes: ChallengeInfoRes) {
        //챌린지 글쓰기할때 챌린지 id
        writeViewModel.changeChallengeIdData(
            challengeInfoRes.challengeId
        )
        binding.challengeBanner.setBannerLayout(challengeInfoRes)
    }

    override fun onResume() {
        super.onResume()
    }
}