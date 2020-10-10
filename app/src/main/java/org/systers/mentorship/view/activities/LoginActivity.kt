package org.systers.mentorship.view.activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Base64
import android.util.Log
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.btnSignUp
import kotlinx.android.synthetic.main.activity_login.tiPassword
import kotlinx.android.synthetic.main.activity_login.tiUsername
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.cbBoth
import kotlinx.android.synthetic.main.activity_sign_up.cbMentee
import kotlinx.android.synthetic.main.activity_sign_up.cbMentor
import kotlinx.android.synthetic.main.activity_sign_up.tvTC
import kotlinx.android.synthetic.main.facebook_dialog.*
import org.json.JSONException
import org.systers.mentorship.R
import org.systers.mentorship.remote.requests.FBLogin
import org.systers.mentorship.remote.requests.Login
import org.systers.mentorship.utils.Constants
import org.systers.mentorship.viewmodels.LoginViewModel
import java.net.MalformedURLException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/**
 * This activity will let the user to login using username/email and password.
 */
class LoginActivity : BaseActivity() {

    var fbId: String? = null
    var fbname: String? = null

    var email = ""
    var getEmail = false

    private var isAvailableToMentor: Boolean = false
    private var needsMentoring: Boolean = false
    private var isAvailableForBoth: Boolean = false
    private lateinit var callbackManager: CallbackManager

    private val loginViewModel by lazy {
        ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }
    private lateinit var username: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        printHashKey(this)
        btnSignUpFaceBook.setOnClickListener {
            loginButton.performClick()
        }

        callbackManager = CallbackManager.Factory.create();
        val permissionNeeds: List<String> = Arrays.asList("email", "public_profile", "AccessToken")

        loginButton.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        println("onSuccess")
                        val accessToken = loginResult.accessToken
                                .token
                        Log.i("accessToken", accessToken)
                        val request = GraphRequest.newMeRequest(
                                loginResult.accessToken
                        ) { `object`, response ->
                            Log.i("LoginActivity",
                                    response.toString())
                            try {
                                fbId = `object`.getString("id")
                                try {
                                    //   val profile_pic = URL("http://graph.facebook.com/" + id.toString() + "/picture?type=large")
                                    //   Log.i("profile_pic", profile_pic.toString() + "")
                                } catch (e: MalformedURLException) {
                                    e.printStackTrace()
                                }

                                fbname = if (`object`.has("name")) {
                                    `object`.getString("name")

                                } else {
                                    ""
                                }

                                if (`object`.has("email")) {
                                    email = `object`.getString("email")
                                } else {
                                    email = ""
                                }

                                hitFbCheckApi()
                                //    openDialog()

                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }
                        val parameters = Bundle()
                        parameters.putString("fields",
                                "id,name,email")
                        request.parameters = parameters
                        request.executeAsync()
                    }

                    override fun onCancel() {
                        println("onCancel")
                    }

                    override fun onError(exception: FacebookException) {
                        println("onError")
                        Log.v("LoginActivity", exception.cause.toString())
                    }
                })

        loginViewModel.successful.observe(this, Observer { successful ->
            hideProgressDialog()
            if (successful != null) {
                if (successful) {
                    Toast.makeText(this, R.string.logging_successful, Toast.LENGTH_LONG).show()
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Snackbar.make(getRootView(), loginViewModel.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        loginViewModel.fbSuccessful.observe(this, Observer {
            hideProgressDialog()
            if (it != null) {
                if (it) {
                    if (loginViewModel.message == "New User") {
                        openDialog()
                    } else {
                        intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        overridePendingTransition(R.anim.slide_inn, R.anim.slide_outt)
                        finish()
                    }
                } else {
                    Snackbar.make(getRootView(), loginViewModel.message, Snackbar.LENGTH_LONG)
                            .show()
                }
            }
        })

        btnLogin.setOnClickListener {
            login()
        }

        btnSignUp.setOnClickListener {
            intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_inn, R.anim.slide_outt)
        }

        tiPassword.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            false
        }

        try {
            val tokenExpiredVal = intent.extras!!.getInt(Constants.TOKEN_EXPIRED_EXTRA)
            if (tokenExpiredVal == 0)
                Snackbar.make(getRootView(), "Session token expired, please login again", Snackbar.LENGTH_LONG).show()
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun hitFbCheckApi() {
        loginViewModel.fbLoginCheck(FBLogin(fbname!!, fbId!!, email, false, need_mentoring = false, available_to_mentor = false), this)
        showProgressDialog(getString(R.string.logging_in))
    }

    private fun openDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.facebook_dialog)
        dialog.show()

        dialog.tvTC.movementMethod = LinkMovementMethod.getInstance()
        dialog.btnLogin.setOnClickListener {

            needsMentoring = dialog.cbMentee.isChecked //old name but works
            isAvailableToMentor = dialog.cbMentor.isChecked //old name but works
            isAvailableForBoth = dialog.cbBoth.isChecked

            if (isAvailableForBoth) {
                isAvailableToMentor = true
                needsMentoring = true
            }
            hitFBRegisterApi()
        }

        dialog.cbTCf.setOnCheckedChangeListener { _, b ->
            dialog.btnLogin.isEnabled = b
        }
    }

    private fun hitFBRegisterApi() {
        loginViewModel.fbLoginRegister(FBLogin(fbname!!, fbId!!, email, true, needsMentoring, isAvailableToMentor))
        showProgressDialog(getString(R.string.logging_in))
    }

    fun printHashKey(pContext: Context) {
        try {
            val info = packageManager.getPackageInfo(
                    "org.systers.mentorship",
                    PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }

    private fun validateCredentials(): Boolean {

        var validCredentials = true
        if (username.isBlank()) {
            tiUsername.error = getString(R.string.error_empty_username)
            validCredentials = false
        } else {
            tiUsername.error = null
        }

        if (password.isBlank()) {
            tiPassword.error = getString(R.string.error_empty_password)
            validCredentials = false
        } else {
            tiPassword.error = null
        }
        return validCredentials
    }

    private fun login() {
        username = tiUsername.editText?.text.toString().trim()
        password = tiPassword.editText?.text.toString().trim()
        if (validateCredentials()) {
            loginViewModel.login(Login(username, password))
            showProgressDialog(getString(R.string.logging_in))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.successful.removeObservers(this)
        loginViewModel.successful.value = null
    }

    override fun onActivityResult(requestCode: Int, responseCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, responseCode, data)
        callbackManager.onActivityResult(requestCode, responseCode, data)
    }
}

