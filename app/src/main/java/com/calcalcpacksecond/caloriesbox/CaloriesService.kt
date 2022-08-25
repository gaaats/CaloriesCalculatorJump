package com.calcalcpacksecond.caloriesbox

import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CaloriesService {

    companion object {
        const val BASE_URL = "https://api.api-ninjas.com/v1/"
        const val API_KEY = BuildConfig.API_KEY
        const val ACTIVITY = "box"
    }

    @GET("caloriesburned")
    suspend fun getCaloriesBurned(
        @Query("activity") activity: String = ACTIVITY,
    ): Response<CaloriesListResponse>


}

class MyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Api-Key", CaloriesService.API_KEY)
            .build()
        return chain.proceed(request)
    }
}