package com.dev6.home.viewmodel
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev6.common.uistate.UiState
import com.dev6.core.enums.PostType
import com.dev6.core.enums.ScrollType
import com.dev6.core.enums.WriteType
import com.dev6.core.util.MutableEventFlow
import com.dev6.core.util.asEventFlow
import com.dev6.domain.model.challenge.ChallengeInfoRes
import com.dev6.domain.model.post.read.PostReadReq
import com.dev6.domain.model.post.read.PostReadRes
import com.dev6.domain.usecase.banner.BannerGetUseCase
import com.dev6.domain.usecase.post.PostGetListUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val postGetListUserCase: PostGetListUserCase,
    private val bannerGetUseCase: BannerGetUseCase
) : ViewModel() {

    private val _BoardeventFlow = MutableEventFlow<BoardEvent>()
    val BoardeventFlow = _BoardeventFlow.asEventFlow()

    var upScrollFlag: MutableLiveData<Boolean> = MutableLiveData()
    var boardTabTypeFlag: MutableLiveData<WriteType> = MutableLiveData()
    var scrollFlag: MutableLiveData<ScrollType> = MutableLiveData()
    var postType : MutableLiveData<String> = MutableLiveData()
    var boardCount = 0

    private fun Boardevent(event: BoardEvent) {
        viewModelScope.launch {
            try {
                _BoardeventFlow.emit(event)
            }catch (e : Exception){
                Log.e("sdfsdf" , e.message.toString())
            }
        }
    }

    fun changePostType(postType : String){
        this.postType.value = postType
    }

    fun clearBoardCount(){
        boardCount = 0
    }

    fun plusBoardCount(){
        boardCount+=1
    }

    fun upScroll(){
        upScrollFlag.value = true
    }

    fun upScrollDone(){
        upScrollFlag.value = false
    }

    fun updateScrollState(state : ScrollType){
        scrollFlag.value = state
    }

    fun checkBoardTabType(type : WriteType){
        boardTabTypeFlag.value = type
    }


    suspend fun getPostList(postReadReq: PostReadReq) {
        viewModelScope.launch {
            postGetListUserCase(postReadReq).catch {}.collect { uiState ->
                Boardevent(BoardEvent.GetPostUiEvent(uiState))
            }
        }
    }

    suspend fun getBannerData(){
        viewModelScope.launch {
            bannerGetUseCase.getBannerInfo().collect{ uiState ->
                Boardevent(BoardEvent.BannerEvent(uiState))
            }
        }
    }


    sealed class BoardEvent {
        data class GetPostUiEvent(val uiState: UiState<PostReadRes>) : BoardEvent()
        data class BannerEvent(val uiState : UiState<ChallengeInfoRes>) : BoardEvent()
        data class upScrollEvent(val flag: Boolean) : BoardEvent()
    }
}