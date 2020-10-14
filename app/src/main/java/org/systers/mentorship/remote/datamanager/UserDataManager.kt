package org.systers.mentorship.remote.datamanager

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.systers.mentorship.models.HomeStatistics
import org.systers.mentorship.viewmodels.SkillsModel
import org.systers.mentorship.models.User
import org.systers.mentorship.remote.ApiManager
import org.systers.mentorship.remote.requests.ChangePassword
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.viewmodels.MentorModel

/**
 * This class represents the data manager related to Users API
 */
class UserDataManager {

    private val apiManager = ApiManager.instance

    /**
     * This will call the getVerifiedUsers method of UserService interface
     * @return an Observable of a list of [User]
     */
    fun getUsers(): Observable<List<User>> {
        return apiManager.userService.getVerifiedUsers()
    }

    fun getSkills():Observable<SkillsModel>{
        return apiManager.userService.getSkills()
    }

    fun getMentorList():Observable<MentorModel>{
        return apiManager.userService.getMentors()
    }

    fun getMeneteeList():Observable<MentorModel>{
        return apiManager.userService.getMenetee()
    }

    fun getAdminList():Observable<MentorModel>{
        return apiManager.userService.getAdminList()
    }

    /** this will get verified users with lat long filter **/


    fun getLocationUsers(lat : String , long : String): Observable<List<User>> {
        return apiManager.userService.getFilteredUsers(lat , long)
    }

    /**
     * This will call the getUser method of UserService interface
     * @return an Observable of [User]
     */

    fun getUser(userId: Int): Observable<User> {
        return apiManager.userService.getUser(userId)
    }

    /**
     * This will call the getUser method of UserService interface
     * @return an Observable of [User]
     */
    fun getUser(): Observable<User> {
        return apiManager.userService.getUser()
    }

    /**
     * This will call the updateUser method of UserService interface
     * @return an Observable of [CustomResponse]
     */
    fun updateUser(id: Int?, name: String?,
                   bio: String?, location: String?, occupation: String?, organization: String?, interests: String?,
                   skills: String?, needMentoring: String, availableToMentor: String): Observable<CustomResponse> {
        return apiManager.userService.updateUser(id,  name,  bio, location, occupation, organization, interests, skills, needMentoring, availableToMentor)
    }

    fun updateUserWithFile(body: MultipartBody.Part, id: RequestBody, name: RequestBody,
                           bio: RequestBody, location: RequestBody, occupation: RequestBody, organization: RequestBody, interests: RequestBody,
                           skills: RequestBody, needMentoring: RequestBody, availableToMentor: RequestBody): Observable<CustomResponse> {
        return apiManager.userService.updateUserWithFile(body, id,  name,  bio, location, occupation, organization, interests, skills, needMentoring, availableToMentor)
    }

    /**
     * This will call the updatePassword method of UserService interface
     * @return an Observable of [CustomResponse]
     */
    fun updatePassword(changePassword: ChangePassword): Observable<CustomResponse> {
        return apiManager.userService.updatePassword(changePassword)
    }

    /**
     * This function fetches user statistics
     * @return an observable of [HomeStatistics]
     */
    fun getHomeStats(): Observable<HomeStatistics> {
        return apiManager.userService.getHomeStats()
    }

    /**
     * This will call the deleteUser method of UserService interface
     * @return an Observable of [CustomResponse]
     */
    fun deleteUser(): Observable<CustomResponse> {
        return apiManager.userService.deleteUser()
    }
}
