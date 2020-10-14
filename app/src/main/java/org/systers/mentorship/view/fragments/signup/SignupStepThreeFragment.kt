package org.systers.mentorship.view.fragments.signup

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_signup_step_one.*
import kotlinx.android.synthetic.main.fragment_signup_step_three.*
import kotlinx.android.synthetic.main.fragment_signup_step_two.*
import org.systers.mentorship.R
import org.systers.mentorship.view.activities.LoginActivity
import org.systers.mentorship.view.activities.RegisterationContainerActivity
import org.systers.mentorship.viewmodels.RegistraionDataModel
import org.systers.mentorship.viewmodels.SignUpModel
import org.systers.mentorship.viewmodels.SingUpDataModel


class SignupStepThreeFragment : Fragment() {


    private lateinit var registraionDataModel: RegistraionDataModel
    private var isMentor: Boolean = true
    private var isMentee: Boolean = false

    private val signUpModel by lazy {
        ViewModelProviders.of(this).get(SignUpModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_step_three, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpModel.successful.observe(viewLifecycleOwner, Observer { successful ->

            (activity as RegisterationContainerActivity).hideProgressDialog()

            if (successful) {
                Toast.makeText(activity, signUpModel.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(activity, LoginActivity::class.java))
                activity!!.overridePendingTransition(R.anim.slide_inn, R.anim.slide_outt)
            } else {
                Toast.makeText(activity, signUpModel.message, Toast.LENGTH_SHORT).show()
            }

        })

        btnTwo.setOnClickListener {
            (activity as RegisterationContainerActivity).gotoPreviousFragment()
        }


        btnRegister.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        val userBio = etUserBio.text.toString().trim()
        val userWhyAreyouHere = etUserWhyAreYouHere.text.toString().trim()

        if (TextUtils.isEmpty(userBio)) {
            etUserBio.error = getString(R.string.please_enter_bio)
            etUserBio.isFocusable = true
            return
        }

        if (TextUtils.isEmpty(userWhyAreyouHere)) {
            etUserWhyAreYouHere.error = getString(R.string.please_fill_this_field)
            etUserWhyAreYouHere.isFocusable = true
            return
        }

        rbMentor.setOnCheckedChangeListener { view, isChecked ->
            isMentor = isChecked
            when (isChecked) {
                true -> {
                    isMentee = false;
                }
            }
        }

        rbMenetee.setOnCheckedChangeListener { view, isChecked ->
            isMentee = isChecked
            when (isChecked) {
                true -> {
                    isMentor = false;
                }
            }
        }

        registraionDataModel.bio = userBio
        registraionDataModel.whyAreYouHere = userWhyAreyouHere

        (activity as RegisterationContainerActivity).showProgressDialog("Processing your request..")

        signUpModel.signUp(SingUpDataModel(registraionDataModel.name, registraionDataModel.username, registraionDataModel.password, registraionDataModel.email, registraionDataModel.skills, registraionDataModel.bio, registraionDataModel.whyAreYouHere, true, isMentee, isMentor))

    }

    companion object {
        @JvmStatic
        fun newInstance(registraionDataModelData: RegistraionDataModel) =
                SignupStepThreeFragment().apply {
                    registraionDataModel = registraionDataModelData
                }
    }
}