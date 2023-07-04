package com.dev6.domain.repository
import com.dev6.domain.model.join.JoinReq
import com.dev6.domain.model.join.JoinRes
import com.dev6.domain.model.join.nickName.NicknameExistCheckRes
import com.dev6.domain.model.join.nickName.NicknameReq
import com.dev6.domain.model.join.nickName.NicknameUpdateRes

interface JoinRepository {
    suspend fun signUp(joinReqDTO: JoinReq) : JoinRes
    suspend fun joinUpdate(nicknameReqPair: Pair<NicknameReq,String>) : NicknameUpdateRes
    suspend fun nicknameExistCheck(userId : String) : NicknameExistCheckRes
    suspend fun signOut(passWord : String) : String
}