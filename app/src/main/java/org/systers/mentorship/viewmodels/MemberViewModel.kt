package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import org.systers.mentorship.MentorshipApplication
import org.systers.mentorship.R
import org.systers.mentorship.remote.datamanager.UserDataManager
import org.systers.mentorship.utils.CommonUtils
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

class MemberViewModel : ViewModel() {
    private var tag = MemberViewModel::class.java.simpleName
    private val userDataManager by lazy {
        UserDataManager()
    }
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String
    lateinit var mentorModel: MentorModel
    lateinit var meneteeModel: MentorModel
    lateinit var adminModel: MentorModel

    @SuppressLint("CheckResult")
    fun getMentorList() {
        userDataManager.getMentorList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MentorModel>() {
                    override fun onComplete() {
                    }

                    override fun onNext(result: MentorModel) {
                        mentorModel = result
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

                })
    }

    @SuppressLint("CheckResult")
    fun getMeneteeApiList() {
        userDataManager.getMeneteeList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MentorModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(result: MentorModel) {
                        meneteeModel = result
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

                })
    }

    @SuppressLint("CheckResult")
    fun getAdminApiList() {
        userDataManager.getAdminList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MentorModel>() {
                    override fun onComplete() {

                    }

                    override fun onNext(result: MentorModel) {
                        adminModel = result
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

                })
    }
}