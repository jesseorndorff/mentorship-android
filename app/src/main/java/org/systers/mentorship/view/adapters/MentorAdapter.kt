package org.systers.mentorship.view.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.mentor_list_adapter.view.*
import org.systers.mentorship.R
import org.systers.mentorship.viewmodels.MentorUserData

class MentorAdapter(var mentorListModel: List<MentorUserData>, val context: Context) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    lateinit var clickedItemListner: ClickedItem

    class MentorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        return MentorViewHolder(LayoutInflater.from(context).inflate(R.layout.mentor_list_adapter, parent, false))
    }

    override fun getItemCount(): Int {
        return mentorListModel.size
    }

    fun setData(mentorListModelData: List<MentorUserData>) {
        mentorListModel = mentorListModelData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentorUserData = mentorListModel[position]

        if (!TextUtils.isEmpty(mentorUserData.profile_photo)) {
            Glide.with(context).load(mentorUserData.profile_photo).placeholder(R.drawable.ic_person_placeholder).into(holder.itemView.cvMentorImage)
        } else {
            holder.itemView.cvMentorImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_placeholder))
        }

        holder.itemView.tvMentorName.text = mentorUserData.name
        holder.itemView.tvMentorText.text = mentorUserData.bio

        holder.itemView.setOnClickListener {
            clickedItemListner.onItemClick(mentorUserData)
        }
    }

    interface ClickedItem {
        fun onItemClick(mentorUserObject: MentorUserData)
    }
}