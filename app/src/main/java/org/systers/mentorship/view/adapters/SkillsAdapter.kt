package org.systers.mentorship.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.skil_layout.view.*
import org.systers.mentorship.R
import org.systers.mentorship.viewmodels.SkillsBean

class SkillsAdapter(private var skillBeanList: MutableList<SkillsBean>, val context: Context) : RecyclerView.Adapter<SkillsAdapter.SkillViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder = SkillViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.skil_layout, parent, false))

    override fun getItemCount(): Int = skillBeanList.size
    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val itemData = skillBeanList[position]
        holder.itemView.tvSkillName.text = itemData.skill

        when (itemData.selected) {
            true -> {
                holder.itemView.ivskillImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.skill_selected))
            }

            false -> {
                holder.itemView.ivskillImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.skill_png))
            }
        }

        holder.itemView.setOnClickListener {
            when (itemData.selected) {
                true -> {
                    skillBeanList[position] = SkillsBean(itemData.skill, false)
                }

                false -> {
                    skillBeanList[position] = SkillsBean(itemData.skill, true)
                }
            }
            notifyDataSetChanged()
        }
    }

    fun setData(skillBeanListData: MutableList<SkillsBean>) {
        skillBeanList = skillBeanListData
    }

    fun getSelectedSkillsData(): MutableList<SkillsBean> {
        return skillBeanList;
    }

}


