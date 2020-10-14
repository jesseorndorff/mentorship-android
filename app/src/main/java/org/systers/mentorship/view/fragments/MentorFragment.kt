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
import kotlinx.android.synthetic.main.fragment_mentor.*
import org.systers.mentorship.R
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.view.adapters.MentorAdapter
import org.systers.mentorship.viewmodels.MemberViewModel
import org.systers.mentorship.viewmodels.MentorUserData

class MentorFragment : Fragment() {

    private lateinit var mentorListModel: List<MentorUserData>
    private lateinit var mentorAdapter: MentorAdapter

    private val mentorViewModel by lazy {
        ViewModelProviders.of(this).get(MemberViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mentor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mentorAdapter = MentorAdapter(listOf(), activity!!)
        (activity as MainActivity).showProgressDialog("Please wait..")
        mentorViewModel.getMentorList()

        mentorViewModel.successful.observe(viewLifecycleOwner, Observer { successful ->
            (activity as MainActivity).hideProgressDialog()
            if (successful) {
                mentorListModel = mentorViewModel.mentorModel.response
                if (mentorListModel.isNotEmpty()) {
                    setMentorListAdapter()
                    mentorAdapter.setData(mentorListModel)
                }

                tvNoMentorFound.visibility = if (mentorListModel.isNotEmpty()) View.GONE else View.VISIBLE
                rvMentor.visibility = if (mentorListModel.isNotEmpty()) View.VISIBLE else View.GONE

            } else {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
            }

        })

        mentorAdapter.clickedItemListner = object : MentorAdapter.ClickedItem {
            override fun onItemClick(mentorUserObject: MentorUserData) {

            }

        }
    }

    private fun setMentorListAdapter() {
        rvMentor.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MentorAdapter(mentorListModel, activity!!)
            runLayoutAnimation(this)

            val dividerItemDecoration = DividerItemDecoration(
                    this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)

            adapter = mentorAdapter
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