package org.systers.mentorship.remote.services

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.systers.mentorship.models.HomeStatistics
import org.systers.mentorship.models.User
import org.systers.mentorship.remote.requests.ChangePassword
import org.systers.mentorship.remote.responses.CustomResponse
import retrofit2.http.*

/**
 * This interface describes the methods related to Users REST API
 */
interface UserService {

    /**
     * This function returns all users of the system
     * @return an observable instance of a list of [User]s
     */
    @GET("users")
    fun getUsers(): Observable<List<User>>

    /**
     * This function returns all users, with email verified, of the system
     * @return an observable instance of a list of [User]s
     */
    @GET("users/verified")
    fun getVerifiedUsers(): Observable<List<User>>

    @GET("users/verified/{lat}/{long}")
    fun getFilteredUsers(@Path("lat") userLat : String , @Path("long") userLong : String): Observable<List<User>>

    /**
     * This function returns a user's public profile of the system
     * @return an observable instance of the [User]
     */
    @GET("users/{userId}")
    fun getUser(@Path("userId") userId: Int): Observable<User>

    /**
     * This function returns the current user profile
     * @return an observable instance of the [User]
     */
    @GET("user")
    fun getUser(): Observable<User>

    /**
     * This function updates the current user's profile
     * @return an observable instance of the [CustomResponse]
     */
    @Multipart
    @PUT("user")
    fun updateUserWithFile(@Part image: MultipartBody.Part, @Part("id") id: RequestBody, @Part("name") name: RequestBody,
                   @Part("bio") bio: RequestBody, @Part("location") location: RequestBody, @Part("occupation") occupation: RequestBody, @Part("organization") organization: RequestBody, @Part("interests") interests: RequestBody,
                   @Part("skills") skills: RequestBody, @Part("needMentoring") needMentoring: RequestBody, @Part("availableToMentor") availableToMentor: RequestBody): Observable<CustomResponse>

    @PUT("user")
    @FormUrlEncoded
    fun updateUser(@Field("id") id: Int?, @Field("name") name: String?,
                   @Field("bio") bio: String?, @Field("location") location: String?, @Field("occupation") occupation: String?, @Field("organization") organization: String?, @Field("interests") interests: String?,
                   @Field("skills") skills: String?, @Field("needMentoring") needMentoring: String, @Field("availableToMentor") availableToMentor: String): Observable<CustomResponse>
    /**
     * This function updates the current user password
     * @return an observable instance of the [CustomResponse]
     */
    @PUT("user/change_password")
    fun updatePassword(@Body changePassword: ChangePassword): Observable<CustomResponse>

    /**
     * This function gets the current user's home screen statistics
     * @return an observable instance of [HomeStatistics]
     */
    @GET("home")
    fun getHomeStats(): Observable<HomeStatistics>

    /**
     * This function deletes current user
     * @return an observable instance of a [CustomResponse]
     */
    @DELETE("user")
    fun deleteUser(): Observable<CustomResponse>
}
