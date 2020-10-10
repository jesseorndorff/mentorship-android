package org.systers.mentorship.remote.requests

data class FBLogin(val name: String, val username: String, val email: String, val terms_and_conditions_checked: Boolean, val need_mentoring: Boolean, val available_to_mentor: Boolean)
