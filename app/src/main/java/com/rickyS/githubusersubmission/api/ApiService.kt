package com.rickyS.githubusersubmission.api

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailGitHubResponse>

    @GET("search/users")
    fun getUser(
        @Query("q") login: String
    ): Call<GitHubResponse>

    @GET("users/{username}/followers")
    fun getFollowerUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowingUser(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

}