package com.dev6.network
import com.dev6.model.JoinReqDTO
import com.dev6.model.JoinResDTO
import com.dev6.model.nickname.NicknameExistCheckResDTO
import com.dev6.model.nickname.NicknameReqDTO
import com.dev6.model.nickname.NicknameUpdateResDTO
import retrofit2.Response
import retrofit2.http.*

interface JoinAPI {

    @Headers("Content-Type: application/json")
    @POST("/v1/users")
    suspend fun signUp(@Body joinReqDTO: JoinReqDTO): Response<JoinResDTO>

    @Headers("Content-Type: application/json")
    @DELETE("/v1/users")
    suspend fun signOut(@Body password: String): Response<String>

    @Headers("Content-Type: application/json")
    @PATCH("/v1/users/{uid}/nickname")
    suspend fun joinUpdate(@Body nicknameReqDTO: NicknameReqDTO , @Path("uid") uid : String): Response<NicknameUpdateResDTO>

    @Headers("Content-Type: application/json")
    @GET("/v1/users/{userId}/duplication")
    suspend fun nicknameExistCheck(@Path("userId") userId : String): Response<NicknameExistCheckResDTO>
}