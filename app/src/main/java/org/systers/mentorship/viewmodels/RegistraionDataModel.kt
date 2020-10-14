package org.systers.mentorship.viewmodels

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RegistraionDataModel(var name: String,
                                var email: String,
                                var username: String,
                                var password: String,
                                var skills: String,
                                var bio: String,
                                var whyAreYouHere: String) : Parcelable