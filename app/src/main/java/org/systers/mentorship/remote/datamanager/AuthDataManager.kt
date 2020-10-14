package org.systers.mentorship.remote.datamanager

import io.reactivex.Observable
import org.systers.mentorship.remote.ApiManager
import org.systers.mentorship.remote.requests.FBLogin
import org.systers.mentorship.remote.requests.Login
import org.systers.mentorship.remote.requests.Register
import org.systers.mentorship.remote.responses.AuthToken
import org.systers.mentorship.remote.responses.CustomResponse
import org.systers.mentorship.remote.responses.FBResponse
import org.systers.mentorship.viewmodels.SingUpDataModel

/**
 * This class represents the data manager related to Authentication API
 */
class AuthDataManager {

    private val apiManager = ApiManager.instance

    fun login(login: Login): Observable<AuthToken> {
        return apiManager.authService.login(login)
    }

    fun checkFbUser(fbLogin: FBLogin): Observable<FBResponse> {
        return apiManager.authService.fBLoginCheck(fbLogin)

    }

    fun registerFbUser(fbLogin: FBLogin): Observable<FBResponse> {
        return apiManager.authService.fBLoginRegister(fbLogin)
    }

    fun register(register: Register): Observable<CustomResponse> {
        return apiManager.authService.register(register)
    }

    fun singupModel(singUpModel: SingUpDataModel): Observable<CustomResponse> {
        return apiManager.authService.singUpModel(singUpModel)
    }
}
