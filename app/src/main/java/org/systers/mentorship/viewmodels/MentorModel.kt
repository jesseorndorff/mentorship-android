package org.systers.mentorship.viewmodels

data class MentorModel(val status: String, val response: ArrayList<MentorUserData>)

data class MentorUserData(val name: String,
                          val profile_photo: String,
                          val bio: String,
                          val username: String,
                          val email: String,
                          val skills: String,
                          val why_are_you_here: String,
                          val need_mentoring: Boolean,
                          val available_to_mentor: Boolean)