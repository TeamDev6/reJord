package com.dev6.domain.usecase.join
import android.util.Log
import com.dev6.common.uistate.UiState
import com.dev6.domain.model.join.JoinReq
import com.dev6.domain.repository.JoinRepository
import com.dev6.domain.usecase.JoinReposBaseUseCase
import com.dev6.domain.usecase.SignOutBaseUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class SignOutUseCase @Inject constructor(
    private val joinRepository: JoinRepository
): SignOutBaseUseCase {
    override suspend fun invoke(params: String) = flow {
        emit(UiState.Loding)
        runCatching {
            joinRepository.signOut(params)
        }.onSuccess { result ->
            emit(UiState.Success(result))
        }.onFailure {
            emit(UiState.Error(it))
        }
    }
}