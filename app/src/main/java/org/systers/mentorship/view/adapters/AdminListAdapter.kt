package org.systers.mentorship.view.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.admin_list_adapter.view.*
import org.systers.mentorship.R
import org.systers.mentorship.viewmodels.MentorUserData

class AdminListAdapter(var adminList: List<MentorUserData>, val context: Context) : RecyclerView.Adapter<AdminListAdapter.AdminViewHolder>() {

    lateinit var itemClickListner: OnItemClick

    class AdminViewHolder(itemsView: View) : RecyclerView.ViewHolder(itemsView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        return AdminViewHolder(LayoutInflater.from(context).inflate(R.layout.admin_list_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return adminList.size
    }

    fun setData(adminListData: List<MentorUserData>) {
        adminList = adminListData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val adminUserData = adminList[position]

        if (!TextUtils.isEmpty(adminUserData.profile_photo)) {
            Glide.with(context).load(adminUserData.profile_photo).placeholder(R.drawable.ic_person_placeholder).into(holder.itemView.cvAdminImage)
        } else {
            holder.itemView.cvAdminImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_placeholder))
        }

        holder.itemView.tvAdminName.text = adminUserData.name
        holder.itemView.tvAdminText.text = adminUserData.bio

        holder.itemView.setOnClickListener {
            itemClickListner.onClickItem(adminUserData)
        }
    }

    interface OnItemClick {
        fun onClickItem(meneteeUserData: MentorUserData);
    }
}