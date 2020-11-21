package com.example.partyplaylist.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://accounts.spotify.com/"
private const val CLIENT_ID = "c2f0e5c97c01405285c671c65fa074d6"
private const val REDIRECT_URL = "com.example.partyplaylist://callback"

private val retrofit = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

interface SpotifyApiService {
    @GET("authorize?response_type=code&client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URL&code_challenge_method=S256")
    fun authenticateUser(@Query("scope") scope: String,
                         @Query("state") state: String,
                         @Query("code_challenge") challenge: String) : Call<String>

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object SpotifyApi {
    val retrofitService : SpotifyApiService by lazy { retrofit.create(SpotifyApiService::class.java) }
}