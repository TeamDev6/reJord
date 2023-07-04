package com.dev6.home.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev6.common.uistate.UiState
import com.dev6.core.util.MutableEventFlow
import com.dev6.core.util.asEventFlow
import com.dev6.domain.model.join.JoinReq
import com.dev6.domain.model.join.JoinRes
import com.dev6.domain.model.join.nickName.NicknameExistCheckRes
import com.dev6.domain.model.join.nickName.NicknameReq
import com.dev6.domain.model.join.nickName.NicknameUpdateRes
import com.dev6.domain.usecase.join.JoinUpdateUseCase
import com.dev6.domain.usecase.join.JoinUseCase
import com.dev6.domain.usecase.join.NicknameExistCheckUseCase
import com.dev6.domain.usecase.join.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserInfoViewModel @Inject constructor(
    private val userDataUpdateUseCase: JoinUpdateUseCase,
    private val userNicknameExistCheckUseCase: NicknameExistCheckUseCase,
    private val signOutUseCase : SignOutUseCase
) : ViewModel() {

    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun userDataUpdate(updateReq: NicknameReq , userUid: String) {
        viewModelScope.launch{
            userDataUpdateUseCase(Pair(updateReq , userUid)).catch {}.collect{ uiState ->
                event(Event.userDataUpdateUiEvent(uiState))
            }
        }
    }

    fun signOut(passWord : String){
        viewModelScope.launch{
            signOutUseCase(passWord).collect{ uiState ->
                event(Event.signOut(uiState))
            }
        }
    }


    fun userUserIdExistCheck(nickname: String) {
        viewModelScope.launch{
            userNicknameExistCheckUseCase(nickname).catch {}.collect{ uiState ->
                event(Event.userUserIdExistUiEvent(uiState))
            }
        }
    }

    sealed class Event {
        data class userDataUpdateUiEvent(val uiState: UiState<NicknameUpdateRes>) : Event()
        data class userUserIdExistUiEvent(val uiState: UiState<NicknameExistCheckRes>) : Event()
        data class signOut(val uiState : UiState<String>): Event()
    }
}

