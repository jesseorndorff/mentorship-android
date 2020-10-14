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
import kotlinx.android.synthetic.main.fragment_manetee.*
import org.systers.mentorship.R
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.view.adapters.MeneteeListAdapter
import org.systers.mentorship.view.adapters.MentorAdapter
import org.systers.mentorship.viewmodels.MemberViewModel
import org.systers.mentorship.viewmodels.MentorUserData


class ManeteeFragment : Fragment() {

    private lateinit var meneteeList: List<MentorUserData>
    lateinit var meneteeLisAdapter: MeneteeListAdapter;

    private val meneteeViewModel by lazy {
        ViewModelProviders.of(this).get(MemberViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manetee, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        meneteeLisAdapter = MeneteeListAdapter(listOf(), activity!!)

        meneteeViewModel.successful.observe(viewLifecycleOwner, Observer { successful ->

            (activity as MainActivity).hideProgressDialog()

            if (successful) {
                meneteeList = meneteeViewModel.meneteeModel.response

                if (meneteeList.isNotEmpty()) {
                    setMeneteeListAdaper()
                    meneteeLisAdapter.setData(meneteeList)
                }

                tvNoMeneteeFound.visibility = if (meneteeList.isNotEmpty()) View.GONE else View.VISIBLE
                rvMenetee.visibility = if (meneteeList.isNotEmpty()) View.VISIBLE else View.GONE

            } else {
                Toast.makeText(activity, getString(R.string.error_something_went_wrong), Toast.LENGTH_SHORT).show()
            }

        })

        (activity as MainActivity).showProgressDialog(getString(R.string.please_wait))
        meneteeViewModel.getMeneteeApiList()

        itemClickListner()
    }

    private fun itemClickListner() {
        meneteeLisAdapter.itemClickListner = object : MeneteeListAdapter.OnItemClick {
            override fun onClickItem(meneteeUserData: MentorUserData) {

            }
        }
    }

    private fun setMeneteeListAdaper() {
        rvMenetee.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MentorAdapter(meneteeList, activity!!)
            runLayoutAnimation(this)

            val dividerItemDecoration = DividerItemDecoration(
                    this.context, DividerItemDecoration.VERTICAL)
            addItemDecoration(dividerItemDecoration)

            adapter = meneteeLisAdapter
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