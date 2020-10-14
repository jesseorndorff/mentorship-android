package org.systers.mentorship.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.systers.mentorship.remote.datamanager.UserDataManager

class MentorshipViewModel : ViewModel() {

    var tag=MentorshipViewModel::class.java.simpleName
    private val userDataManager: UserDataManager = UserDataManager()
    val successful: MutableLiveData<Boolean> = MutableLiveData()
    lateinit var message: String

    @SuppressLint("CheckResult")
    fun getMontorList(){

    }

}