package com.dev6.join
import androidx.fragment.app.viewModels
import com.dev6.common.uistate.UiState
import com.dev6.core.base.BindingFragment
import com.dev6.domain.model.join.JoinReq
import com.dev6.join.R
import com.dev6.join.databinding.FragmentJoinBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinFragment : BindingFragment<FragmentJoinBinding>(R.layout.fragment_join) {
    private val joinViewModel: JoinViewModel by viewModels()

    override fun initView() {
        super.initView()
    }

    override fun initViewModel() {
        super.initViewModel()
        var join = JoinReq("이름")
        joinViewModel.userJoin(join)

        repeatOnStartedFragment {
            joinViewModel.eventFlow.collect{ event-> handleEvent(event)}
        }
    }

    override fun initListener() {
        super.initListener()
    }

    override fun afterViewCreated() {
        super.afterViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun handleEvent(event: Event) = when (event) {
        is Event.JoinEvent ->{
            when(event.uiState){
                is UiState.Loding->      {

                }
                is UiState.Success->{

                }
                is UiState.Error->{

                }
            }
        }
    }
}