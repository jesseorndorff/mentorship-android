package org.systers.mentorship.view.activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import org.systers.mentorship.R
import org.systers.mentorship.view.fragments.signup.SignupStepOneFragment


class RegisterationContainerActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registraion_container_layout)
        addFragment(SignupStepOneFragment())
    }

    private fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(R.id.singUpFrame, fragment).commit()
    }

    fun replaceSignUpFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.singUpFrame, fragment).addToBackStack(fragment::getActivity.name).commit()
    }

    fun gotoPreviousFragment() {
        supportFragmentManager.popBackStackImmediate()
    }
}
