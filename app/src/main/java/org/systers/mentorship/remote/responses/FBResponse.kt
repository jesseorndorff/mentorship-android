package org.systers.mentorship.remote.responses

data class FBResponse(var accessToken: String,
                      var accessExpiry: Float, var message: String)