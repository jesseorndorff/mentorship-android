package org.systers.mentorship.view.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.menetee_list_adapter.view.*
import org.systers.mentorship.R
import org.systers.mentorship.viewmodels.MentorUserData

class MeneteeListAdapter(var meneteeList: List<MentorUserData>, val context: Context) : RecyclerView.Adapter<MeneteeListAdapter.MeneteeViewHolder>() {


    class MeneteeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    lateinit var itemClickListner: OnItemClick

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeneteeViewHolder {
        return MeneteeViewHolder(LayoutInflater.from(context).inflate(R.layout.menetee_list_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return meneteeList.size
    }

    fun setData(meneteeListData: List<MentorUserData>) {
        meneteeList = meneteeListData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MeneteeViewHolder, position: Int) {
        val meneteeUserData = meneteeList[position]

        if (!TextUtils.isEmpty(meneteeUserData.profile_photo)) {
            Glide.with(context).load(meneteeUserData.profile_photo).placeholder(R.drawable.ic_person_placeholder).into(holder.itemView.cvMeneteeImage)
        } else {
            holder.itemView.cvMeneteeImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_placeholder))
        }

        holder.itemView.tvMeneteeName.text = meneteeUserData.name
        holder.itemView.tvMeneteeText.text = meneteeUserData.bio

        holder.itemView.setOnClickListener {
            itemClickListner.onClickItem(meneteeUserData)
        }

    }


    interface OnItemClick {
        fun onClickItem(meneteeUserData: MentorUserData);
    }
}