package com.mstech.lifeline.organisation.model

import com.mstech.lifeine.organisation.model.OrganisationResponse
import com.mstech.lifeline.BuildConfig

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetroApi {

    @GET("organisation/GetOrganisationList")
    suspend fun getOrganisationList(): Response<List<OrganisationResponse>>

    @GET("articles/getarticlestitleslist")
    suspend fun getArticleList(@Query("orgid")orgid :String): Response<List<ArticleResponse>>

    @GET("articles/getarticledetails")
    suspend fun getDocuments(@Query("articleid")articleId :String): Response<DocumentResponse>



    companion object {

        operator fun invoke(): RetroApi {
            val interceptor = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
            } else {
                interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.NONE }
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS).build();

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.solutionsempo.com/api/")
                .client(client)
                .build()
                .create(RetroApi::class.java)
        }

    }
}