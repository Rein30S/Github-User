package com.rickyS.githubusersubmission.api

import com.google.gson.annotations.SerializedName

data class DetailGitHubResponse(

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("login")
    val login: String?,

    @field:SerializedName("followers")
    val followers: Int? = null,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String? = null,

    @field:SerializedName("following")
    val following: Int? = null,

    @field:SerializedName("followers_url")
    val followersUrl: String? = null
)