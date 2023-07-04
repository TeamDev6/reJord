package com.dev6.home.fragment

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dev6.common.uistate.UiState
import com.dev6.core.BindingFragment
import com.dev6.core.util.DevicePrefs
import com.dev6.domain.model.join.nickName.NicknameReq
import com.dev6.home.R
import com.dev6.home.bottomsheet.WithdrawalSheetFragment
import com.dev6.home.databinding.FragmentUserInfoBinding
import com.dev6.home.viewmodel.MyPageViewModel
import com.dev6.home.viewmodel.UserInfoViewModel
import com.dev6.write.fragment.WriteBottomSheetFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class UserInfoFragment : BindingFragment<FragmentUserInfoBinding>(R.layout.fragment_user_info) {
    lateinit var bottomSheet : BottomSheetDialogFragment
    private val myPageViewModel: MyPageViewModel by activityViewModels()
    private val userInfoViewModel: UserInfoViewModel by activityViewModels()
    override fun initView() {
        super.initView()
    }

    override fun initViewModel() {
        super.initViewModel()
        binding.userNicknameTv.setText(myPageViewModel.userNickName)
        repeatOnStartedFragment {
            userInfoViewModel.eventFlow.collect { event -> handleEvent(event) }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.WithdrawalBt.setOnClickListener {
            bottomSheet =  WithdrawalSheetFragment()
            bottomSheet.show(parentFragmentManager , bottomSheet.tag)
        }

        binding.userEditBt.setOnClickListener {
            userInfoViewModel.userDataUpdate(
                NicknameReq(binding.userNicknameTv.text.toString()),
                DevicePrefs(requireContext()).getUserUID()
            )
        }
        //중복확인
        //애초에 닉네임 중복 확인이라는게 있나?
        binding.textView5.setOnClickListener {
           // Log.v("sgsdgsdg" , binding.userNicknameTv.text.toString())
            //userInfoViewModel.userUserIdExistCheck(DevicePrefs(requireContext()).getUserUID())
        }
        binding.closeIv.setOnClickListener {
            myPageViewModel.postmypageBackEvent(true)
            findNavController().popBackStack()
        }

        binding.WithdrawalBt.setOnClickListener {
            userInfoViewModel.signOut("")
        }
    }

    override fun afterViewCreated() {
        super.afterViewCreated()
    }

    private fun handleEvent(event: UserInfoViewModel.Event) = when (event) {
        is UserInfoViewModel.Event.userDataUpdateUiEvent -> {
            when (event.uiState) {
                is UiState.Loding -> {

                }
                is UiState.Success -> {
                    myPageViewModel.postmypageBackEvent(true)
                    findNavController().popBackStack()
                }
                is UiState.Error -> {
                    Log.v("유저인포",event.uiState.error.toString())
                }
            }
        }
        is UserInfoViewModel.Event.userUserIdExistUiEvent -> {
            when (event.uiState) {
                is UiState.Loding -> {

                }
                is UiState.Success -> {
                    Log.v("유저인포","중복확인ㅇㅋ")
                }
                is UiState.Error -> {
                    Log.v("유저인포",event.uiState.error.toString())
                }
            }
        }
        is UserInfoViewModel.Event.signOut -> {
            when (event.uiState) {
                is UiState.Loding -> {

                }
                is UiState.Success -> {
                    Log.v("회원탈퇴","탈퇴성공")
                }
                is UiState.Error -> {
                    Log.v("탈퇴에러",event.uiState.error.toString())
                }
            }
        }
    }
}