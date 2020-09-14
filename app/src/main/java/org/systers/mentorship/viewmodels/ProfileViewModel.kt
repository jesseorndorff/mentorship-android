package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.models.User
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

/**
 * This class represents the [ViewModel] used for ProfileFragment
 */
class ProfileViewModel : ViewModel() {

    var tag = ProfileViewModel::class.java.simpleName!!

    private val userDataManager: UserDataManager = UserDataManager()

    val successfulGet: MutableLiveData<Boolean> = MutableLiveData()
    val successfulUpdate: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var user: User
    lateinit var message: String

    /**
     * Fetches the current users full profile
     */
    @SuppressLint("CheckResult")
    fun getProfile() {
        userDataManager.getUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<User>() {
                    override fun onNext(userprofile: User) {
                        user = userprofile
                        successfulGet.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        when (throwable) {
                            is IOException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message = CommonUtils.getErrorResponse(throwable).message
                            }
                            else -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        successfulGet.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }

    /**
     * Updates the current user profile with data changed by the user
     */
    @SuppressLint("CheckResult")
    fun updateProfileWithFile(body: MultipartBody.Part, id: RequestBody,  name: RequestBody,
                              bio: RequestBody, location: RequestBody, occupation: RequestBody, organization: RequestBody, interests: RequestBody,
                              skills: RequestBody, needMentoring: RequestBody, availableToMentor: RequestBody) {
        userDataManager.updateUserWithFile(body, id,  name,  bio, location, occupation, organization, interests, skills, needMentoring, availableToMentor)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(response: CustomResponse) {
                        successfulUpdate.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        when (throwable) {
                            is IOException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message = CommonUtils.getErrorResponse(throwable).message
                            }
                            else -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        successfulUpdate.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun updateProfile( id: Int?,  name: String?,
                       bio: String?, location: String?, occupation: String?, organization: String?, interests: String?,
                       skills: String?, needMentoring: String, availableToMentor: String) {
        userDataManager.updateUser(id,  name,  bio, location, occupation, organization, interests, skills, needMentoring, availableToMentor)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(response: CustomResponse) {
                        successfulUpdate.value = true
                    }

                    override fun onError(throwable: Throwable) {
                        when (throwable) {
                            is IOException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_please_check_internet)
                            }
                            is TimeoutException -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_request_timed_out)
                            }
                            is HttpException -> {
                                message = CommonUtils.getErrorResponse(throwable).message
                            }
                            else -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        successfulUpdate.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }
}

