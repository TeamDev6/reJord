package com.dev6.domain.model.challenge

data class ChallengeInfoRes(
    val badgeCode : String,
    val challengeId : String,
    val contents : String,
    val footprintAmount : String,
    val imgBack : String,
    val imgFront : String,
    val textColor : String,
    val title : String
)