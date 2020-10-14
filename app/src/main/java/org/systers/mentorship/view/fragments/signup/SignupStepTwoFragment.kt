package org.systers.mentorship.view.fragments.signup

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_signup_step_two.*
import org.systers.mentorship.R
import org.systers.mentorship.view.activities.RegisterationContainerActivity
import org.systers.mentorship.view.adapters.SkillsAdapter
import org.systers.mentorship.viewmodels.RegistraionDataModel
import org.systers.mentorship.viewmodels.SignUpModel
import org.systers.mentorship.viewmodels.SkillsBean


class SignupStepTwoFragment() : Fragment() {

    private lateinit var skAdapter: SkillsAdapter;
    private lateinit var registraionDataModel: RegistraionDataModel
    private lateinit var skillBeanList: MutableList<SkillsBean>

    private val skillsViewModel by lazy {
        ViewModelProviders.of(this).get(SignUpModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_step_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skAdapter = SkillsAdapter(arrayListOf(), activity!!)
        skillBeanList = arrayListOf()
        skillsViewModel.successful.observe(viewLifecycleOwner, Observer { successful ->
            (activity as RegisterationContainerActivity).hideProgressDialog()
            if (successful != null) {
                val skillsModel: List<String> = skillsViewModel.skillsModel!!.skills.split(",")
                skillBeanList.clear()
                for (i in skillsModel.indices) {
                    skillBeanList.add(SkillsBean(skillsModel[i], false))
                }
                skAdapter.setData(skillBeanList)
                rvSkills.apply {
                    layoutManager = GridLayoutManager(context, 2)
                    adapter = SkillsAdapter(skillBeanList, context)
                    runLayoutAnimation(this)
                    adapter = skAdapter
                }
            }
        })

        getSkillsData()

        btnOne.setOnClickListener {
            (activity as RegisterationContainerActivity).gotoPreviousFragment()
        }

        btnNextThree.setOnClickListener {

            val selectedSkillsData = skAdapter.getSelectedSkillsData()
            var skills: String = ""

            for (i in selectedSkillsData.indices) {
                if (selectedSkillsData[i].selected) {
                    if (TextUtils.isEmpty(skills)) {
                        skills = selectedSkillsData[i].skill
                    } else {
                        skills += "," + selectedSkillsData[i].skill
                    }
                }
            }

            if (TextUtils.isEmpty(skills)) {
                Toast.makeText(activity, getString(R.string.please_select_skills), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val split = skills.split(",")
            if (split.size < 5) {
                Toast.makeText(activity, getString(R.string.plaese_select_atleast_five_skills), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registraionDataModelData.skills = skills

            (activity as RegisterationContainerActivity).replaceSignUpFragmentWithBackStack(SignupStepThreeFragment.newInstance(registraionDataModelData))

        }
    }

    private fun getSkillsData() {
        (activity as RegisterationContainerActivity).showProgressDialog(getString(R.string.please_wait))
        skillsViewModel.skills()
    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,
                R.anim.layout_fall_down)
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    companion object {
        lateinit var registraionDataModelData: RegistraionDataModel

        @JvmStatic
        fun newInstance(registraionDataModel: RegistraionDataModel) =
                SignupStepTwoFragment().apply {
                    registraionDataModelData = registraionDataModel
                }
    }

}