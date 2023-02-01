package com.dev6.home.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev6.common.uistate.UiState
import com.dev6.core.util.MutableEventFlow
import com.dev6.core.util.asEventFlow
import com.dev6.domain.model.post.read.PostReadReq
import com.dev6.domain.model.post.read.PostReadRes
import com.dev6.domain.usecase.post.PostGetListUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeViewModel @Inject constructor(
    private val postGetListUserCase: PostGetListUserCase
) : ViewModel() {

    private val _ChallengeEventFlow = MutableEventFlow<ChallengeEvent>(10)
    val ChallengeEventFlow = _ChallengeEventFlow.asEventFlow()

    private fun Challengeevent(event: ChallengeEvent) {
        viewModelScope.launch {
            _ChallengeEventFlow.emit(event)
        }
    }

    fun getChallengeList(postReadReq: PostReadReq) {
        viewModelScope.launch {

        }
    }

    sealed class ChallengeEvent {
        data class GetChallengeUiEvent(val uiState: UiState<PostReadRes>) : ChallengeEvent()
    }
}