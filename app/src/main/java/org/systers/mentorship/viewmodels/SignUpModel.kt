package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.remote.datamanager.AuthDataManager
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class SignUpModel: ViewModel(){
    var tag = SignUpModel::class.java.simpleName!!
    private val userDataManager: UserDataManager = UserDataManager()

    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String
    var skillsModel: SkillsModel?=null
    private val authDataManager: AuthDataManager = AuthDataManager()


    @SuppressLint("CheckResult")
    fun skills() {
        userDataManager.getSkills()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<SkillsModel>() {
                    override fun onNext(skillsModelResult: SkillsModel) {
                        skillsModel =SkillsModel(skillsModelResult.skills)
                        successful.value = true
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
                                message = CommonUtils.getErrorResponse(throwable).message.toString()
                            }
                            else -> {
                                message = MentorshipApplication.getContext()
                                        .getString(R.string.error_something_went_wrong)
                                Log.e(tag, throwable.localizedMessage)
                            }
                        }
                        successful.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }

    @SuppressLint("CheckResult")
    fun signUp(@NonNull singUpModel: SingUpDataModel) {
        authDataManager.singupModel(singUpModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CustomResponse>() {
                    override fun onNext(customResponse: CustomResponse) {
                        message = customResponse.message
                        successful.value = true
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
                        successful.value = false
                    }

                    override fun onComplete() {
                    }
                })
    }


}