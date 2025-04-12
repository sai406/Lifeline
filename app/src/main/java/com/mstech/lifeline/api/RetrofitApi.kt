package com.mstech.lifeline.api

import com.mstech.lifeline.BuildConfig
import com.mstech.lifeline.activities.LoginActivity
import com.mstech.lifeline.coordinater.model.*
import com.mstech.lifeline.models.*
import com.mstech.lifeline.models.LoginResponse
import com.mstech.lifeline.resources.model.ResourceResponse
import layout.CoordinatorMemberResponse
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface RetrofitApi {

    @GET("GetDdlCountry")
    suspend fun getCountries(): Response<CountryResponse>

    @GET("GetDdlLocation")
    suspend fun getLocations(): Response<List<LocationResponse>>

    @GET("GetMemberCampaigns")
    suspend fun getCampaignList(@Query("mid") memberid: String): Response<CampaignResponse>

    @GET("GetVerificationCode")
    suspend fun getVerificationCode(
        @Query("Mobile") mobile: String,
        @Query("deviceID") did: String,
        @Query("devicetype") type: String
    ): Response<MobileResponse>

    @GET("GetMemberCoordinators")
    suspend fun getCoordinators(@Query("mid") memberid: String): Response<ContactsResponse>

    @GET("DeleteSOScontact")
    suspend fun deleteSos(@Query("contactid") sosid: String): Response<VerificationResponse>

    @GET("GetVolunteerTasks")
    suspend fun getSos(@Query("volunteerid") volunterid: String): Response<List<SOSResponse>>

    @GET("GetResourceDocuments")
    suspend fun getResources(): Response<List<ResourceResponse>>

/*    @GET("VolunteerRescueActionBtn")
    suspend fun rescueAction(
        @Query("helpid") helpid: String,
        @Query("volunteerid") volunterid: String
    ): Response<VerificationResponse>*/

   /* @POST("")
    suspend fun (
        @Query("HelpId") helpid: String,
        @Query("MemberId") volunterid: String,
        @Query("IsAccepted") status: String
    ): Response<VerificationResponse>*/
    @POST("RespondHelpByVolunteer")
    suspend fun rescueAction(@Body postdata: RequestBody): Response<RespondResponse>

    @POST("MemberLogin")
    suspend fun loginRequest(@Body postdata: RequestBody): Response<LoginActivity.LoginsResponse>

    @GET("StartCampaign")
    suspend fun sendLocation(
        @Query("mid") memberid: String,
        @Query("campid") campid: String,
        @Query("latitude") lat: String,
        @Query("longitude") lon: String
    ): Response<ResponseBody>

    @GET("MemberSignToCampaign")
    suspend fun signedCampain(
        @Query("mid") memberid: String,
        @Query("campid") campid: String,
        @Query("msg") msg: String
    ): Response<VerificationResponse>

    //    api/MemberSignToCampaign?mid=3&campid=1&msg=""
    /* @GET("api/GetSwipeGamePlaydetails")
     suspend fun getPrizebyCid(@Query("id")id :String,@Query("Cid") cid :String) : Response<PrizeResponse>
 */
    @POST("MemberRegister")
    suspend fun registerMember(@Body postdata: RequestBody): Response<VerificationResponse>

    @POST("MemberRegisterMobileApp")
    suspend fun registerCoOrdinator(@Body postdata: RequestBody): Response<RegisterResponse>

    @POST("AddMember")
    suspend fun register(@Body postdata: RequestBody): Response<Register>

    @POST("AddMemberSOS")
    suspend fun addSOS(@Body postdata: RequestBody): Response<ResponseBody>

    @POST("MemberSeekHelp")
    suspend fun sentSos(@Body postdata: RequestBody): Response<ResponseBody>

    @POST("CoordinatorSeekHelp")
    suspend fun sentCoordinaterSos(@Body postdata: RequestBody): Response<ResponseBody>

    @POST("InsertSupportMessage")
    suspend fun supportMessage(@Body postdata: RequestBody): Response<ResponseBody>

    @POST("AddMemberHelpVideos")
    suspend fun addVideo(@Body postdata: RequestBody): Response<ResponseBody>
    @GET("GetCoordinatorSOSMembers")
    suspend fun getIncidentList(@Query("mid") mid : String ): Response<SosPersonResponse>

    @GET("GetCoordinatorVolunteers")
    suspend fun getVolunteers(@Query("mid") mid : Int ): Response<List<VolunteerListResponse>>

    @GET("GetCoordinatorMembers")
    suspend fun getMembers(@Query("mid") mid : String ): Response<List<CoordinatorMemberResponse>>

    @GET("api/GetHelpSentVolunteers")
    suspend fun getSentVolunteers(@Query("helpid") helpId : Int ): Response<List<MemberResponse>>

    @GET("GetHelpSeekingMemberDetails")
    suspend fun getIncidentDetails(@Query("helpid") helpId: String): Response<IncidentDetailsResponse>

    @GET("RescueActionBtn")
    suspend fun rescueButton(@Query("helpid") helpId : String ,@Query("action") action :Int): Response<RescueResponse>

    @GET("AssignToVolunteer")
    suspend fun assignVolunteer(@Query("mid") mid : String,@Query("MemberIds") vids :String ): Response<StatusResponse>

    @GET("GetHelpSentVolunteers")
    suspend fun assignedVolunteers(@Query("helpid") helpId:String  ): Response<List<AssignedVolunteerResponse>>

    @GET("GetDdlCoordinators")
    suspend fun getCoordinaters(@Query("st") stateId:String ): Response<List<CoordinaterResponse>>

    companion object {

        operator fun invoke(): RetrofitApi {
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
                .baseUrl("http://civiccare.net/api/")
                .client(client)
                .build()
                .create(RetrofitApi::class.java)
        }

    }
}
