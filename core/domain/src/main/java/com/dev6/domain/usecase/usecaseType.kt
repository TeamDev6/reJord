package com.dev6.domain.usecase
import com.dev6.common.uistate.UiState
import com.dev6.domain.model.challenge.ChallengeEditReq
import com.dev6.domain.model.challenge.ChallengeEditRes
import com.dev6.domain.model.challenge.ChallengeReadReq
import com.dev6.domain.model.challenge.ChallengeRes
import com.dev6.domain.model.join.JoinReq
import com.dev6.domain.model.join.JoinRes
import com.dev6.domain.model.join.login.LoginReq
import com.dev6.domain.model.join.login.LoginRes
import com.dev6.domain.model.join.nickName.NicknameExistCheckRes
import com.dev6.domain.model.join.nickName.NicknameReq
import com.dev6.domain.model.join.nickName.NicknameUpdateRes
import com.dev6.domain.model.post.delete.PostEditReq
import com.dev6.domain.model.post.delete.PostEditRes
import com.dev6.domain.model.post.read.PostReadReq
import com.dev6.domain.model.post.read.PostReadRes
import com.dev6.domain.model.post.write.ChallengeWriteReq
import com.dev6.domain.model.post.write.ChallengeWriteRes
import com.dev6.domain.model.post.write.PostWriteReq
import com.dev6.domain.model.post.write.PostWriteRes
import kotlinx.coroutines.flow.Flow


typealias LoginReposBaseUseCase = BaseUseCase<LoginReq, Flow<UiState<LoginRes>>>
typealias JoinReposBaseUseCase = BaseUseCase<JoinReq, Flow<UiState<JoinRes>>>
typealias SignOutBaseUseCase = BaseUseCase<String, Flow<UiState<String>>>
typealias NicknameExistCheckBaseUseCase = BaseUseCase<String, Flow<UiState<NicknameExistCheckRes>>>
typealias JoinUpdateReposBaseUseCase = BaseUseCase<Pair<NicknameReq,String>, Flow<UiState<NicknameUpdateRes>>>
typealias PostGetListReposBaseUseCase = BaseUseCase<PostReadReq, Flow<UiState<PostReadRes>>>
typealias ChallengeGetListReposBaseUseCase = BaseUseCase<ChallengeReadReq, Flow<UiState<ChallengeRes>>>
typealias PostWriteBaseUseCase = BaseUseCase<PostWriteReq, Flow<UiState<PostWriteRes>>>
typealias ChallengeWriteBaseUseCase = BaseUseCase<ChallengeWriteReq , Flow<UiState<ChallengeWriteRes>>>
typealias PostDeleteBaseUseCase = BaseUseCase<String , Flow<UiState<String>>>
typealias ChallengeDeleteBaseUseCase = BaseUseCase<String , Flow<UiState<String>>>
typealias PostEditBaseUseCase = BaseUseCase<PostEditReq, Flow<UiState<PostEditRes>>>
typealias ChallengeEditBaseUseCase = BaseUseCase<ChallengeEditReq, Flow<UiState<ChallengeEditRes>>>
