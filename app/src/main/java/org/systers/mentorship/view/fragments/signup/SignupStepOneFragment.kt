package org.systers.mentorship.view.fragments.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_signup_step_one.*
import org.systers.mentorship.R
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.view.activities.RegisterationContainerActivity
import org.systers.mentorship.viewmodels.RegistraionDataModel

class SignupStepOneFragment : Fragment() {

    private lateinit var registraionDataModel: RegistraionDataModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup_step_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnStepTwo.setOnClickListener { validateData() }
        btnBackLogin.setOnClickListener { (activity as RegisterationContainerActivity).onBackPressed() }
    }

    private fun validateData() {
        val name = etUsername.text.toString().trim()
        val userEmail = etUserEmail.text.toString().trim()
        val userName = etUserUserName.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val confPassword = etConfPassword.text.toString().trim()

        if (TextUtils.isEmpty(name)) {
            etUsername.error = getString(R.string.error_empty_username)
            etUsername.isFocusable = true
            return
        }

        if (TextUtils.isEmpty(userEmail)) {
            etUserEmail.error = getString(R.string.error_empty_email)
            etUserEmail.isFocusable = true
            return
        }

        if (!CommonUtils.isValidEmail(userEmail)) {
            etUserEmail.error = getString(R.string.valid_error)
            etUserEmail.isFocusable = true
            return
        }

        if (TextUtils.isEmpty(userName)) {
            etUserUserName.error = getString(R.string.error_empty_username)
            etUserUserName.isFocusable = true
            return
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.error = getString(R.string.error_empty_password)
            etPassword.isFocusable = true
            return
        }

        if (password.length < 8) {
            etPassword.error = "Password should be contain 8 characters"
            etPassword.isFocusable = true
            return
        }

        if (TextUtils.isEmpty(confPassword)) {
            etConfPassword.error = getString(R.string.error_empty_password_confirmation)
            etConfPassword.isFocusable = true
            return
        }

        if (password != confPassword) {
            etConfPassword.error = getString(R.string.password_not_match)
            etConfPassword.isFocusable = true
            return
        }

        registraionDataModel = RegistraionDataModel(name, userEmail, userName, password, "", "", "")
        (activity as RegisterationContainerActivity).replaceSignUpFragmentWithBackStack(SignupStepTwoFragment.newInstance(registraionDataModel))
    }
}