package com.dev6.remote
import com.dev6.model.JoinReqDTO
import com.dev6.model.JoinResDTO
import com.dev6.model.executeNetworkHandling
import com.dev6.model.nickname.NicknameExistCheckResDTO
import com.dev6.model.nickname.NicknameReqDTO
import com.dev6.model.nickname.NicknameUpdateResDTO
import com.dev6.network.JoinAPI
import javax.inject.Inject


interface JoinRemoteDataSource {
    suspend fun signUp(joinReqDTO: JoinReqDTO): JoinResDTO
    suspend fun joinUpdate(nicknameReqDTO: NicknameReqDTO , userUid: String) : NicknameUpdateResDTO
    suspend fun nicknameExistCheck(userId : String) : NicknameExistCheckResDTO
    suspend fun signOut(passWord : String) : String
}

class JoinRemoteDataSourceImpl @Inject constructor(
    private val joinService: JoinAPI
) : JoinRemoteDataSource {

    override suspend fun signUp(joinEntitiy: JoinReqDTO): JoinResDTO {
        return joinService.signUp(joinEntitiy).executeNetworkHandling()
    }

    override suspend fun joinUpdate(nicknameReqDTO: NicknameReqDTO , userUid: String): NicknameUpdateResDTO {
        return joinService.joinUpdate(nicknameReqDTO , userUid).executeNetworkHandling()
    }

    override suspend fun nicknameExistCheck(userId: String): NicknameExistCheckResDTO {
        return joinService.nicknameExistCheck(userId).executeNetworkHandling()
    }

    override suspend fun signOut(passWord: String): String {
        return joinService.signOut(passWord).executeNetworkHandling()
    }
}