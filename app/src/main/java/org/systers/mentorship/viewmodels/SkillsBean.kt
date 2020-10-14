package org.systers.mentorship.viewmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SkillsBean(val skill:String,val selected:Boolean):Parcelable