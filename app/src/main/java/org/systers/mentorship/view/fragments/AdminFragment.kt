package org.systers.mentorship.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_admin.*
import org.systers.mentorship.R
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.view.adapters.AdminListAdapter
import org.systers.mentorship.view.adapters.MentorAdapter
import org.systers.mentorship.viewmodels.MemberViewModel
import org.systers.mentorship.viewmodels.MentorUserData

class AdminFragment : Fragment() {

    private lateinit var adminDataList: List<MentorUserData>
    lateinit var adminListAdapter: AdminListAdapter

    private val adminViewModel by lazy {
        ViewModelProviders.of(this).get(MemberViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adminListAdapter = AdminListAdapter(listOf(), activity!!)
        adminViewModel.successful.observe(viewLifecycleOwner, Observer { successful ->
            (activity as MainActivity).hideProgressDialog()
            if (successful) {
                adminDataList = adminViewModel.adminModel.response

                if (adminDataList.isNotEmpty()) {
                    setAdminListAdaper()
                    adminListAdapter.setData(adminDataList)
                }

                tvNoAdminFound.visibility = if (adminDataList.isNotEmpty()) View.GONE else View.VISIBLE
                rvAdmin.visibility = if (adminDataList.isNotEmpty()) View.VISIBLE else View.GONE
            } else {
                Toast.makeText(activity, getString(R.string.error_something_went_wrong), Toast.LENGTH_SHORT).show()
            }

        })

        (activity as MainActivity).showProgressDialog(getString(R.string.please_wait))
        adminViewModel.getAdminApiList()

        itemListner();
    }

    private fun itemListner() {
        adminListAdapter.itemClickListner = object : AdminListAdapter.OnItemClick {
            override fun onClickItem(meneteeUserData: MentorUserData) {

            }

        }
    }

    private fun setAdminListAdaper() {
        rvAdmin.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MentorAdapter(adminDataList, activity!!)
            runLayoutAnimation(this)

            val dividerItemDecoration = DividerItemDecoration(
                    this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)

            adapter = adminListAdapter
        }
    }


    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        recyclerView.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,
                R.anim.layout_fall_down)
        recyclerView.adapter?.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

}