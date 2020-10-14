package org.systers.mentorship.utils

import android.Manifest
import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Patterns
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import io.reactivex.annotations.NonNull
import org.systers.mentorship.remote.responses.CustomResponse
import retrofit2.HttpException


/**
 * Object to store utilities such as a [Gson] instance
 */
object CommonUtils {

    val gson = Gson()
    private const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12
    const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    /**
     * Extracts a CustomResponse object from a throwable
     * @param throwable from which the object is to be extracted
     * @return a CustomResponse object
     */
    fun getErrorResponse(@NonNull throwable: Throwable): CustomResponse {
        val httpException = throwable as HttpException
        val response = httpException.response().errorBody()?.string()
        return gson.fromJson(response.toString(), CustomResponse::class.java)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermission(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(R.string.yes) { dialog, which -> ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CommonUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), CommonUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermissionCamera(context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((context as Activity), Manifest.permission.CAMERA)) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("Camera permission is necessary")
                    alertBuilder.setPositiveButton(R.string.yes) { dialog, which -> ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA), CommonUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.CAMERA), CommonUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }
}
