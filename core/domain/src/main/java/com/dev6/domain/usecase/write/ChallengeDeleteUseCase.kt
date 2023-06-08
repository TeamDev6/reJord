package com.dev6.domain.usecase.write

import com.dev6.common.uistate.UiState
import com.dev6.domain.repository.PostRepository
import com.dev6.domain.usecase.ChallengeDeleteBaseUseCase
import com.dev6.domain.usecase.PostDeleteBaseUseCase
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChallengeDeleteUseCase @Inject constructor(
    private val postRepository: PostRepository
) : ChallengeDeleteBaseUseCase {
    override suspend fun invoke(params: String) = flow {
        emit(UiState.Loding)
        runCatching {
            postRepository.challengeDelete(params)
        }.onSuccess { result ->
            emit(UiState.Success(result))
        }.onFailure {
            emit(UiState.Error(it))
        }
    }
}