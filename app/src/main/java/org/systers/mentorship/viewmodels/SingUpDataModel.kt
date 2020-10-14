package org.systers.mentorship.viewmodels

data class SingUpDataModel(val name: String,
                           val username: String,
                           val password: String,
                           val email: String,
                           val skills: String,
                           val bio: String,
                           val why_are_you_here: String,
                           val terms_and_conditions_checked: Boolean,
                           val need_mentoring: Boolean,
                           val available_to_mentor: Boolean)