package org.systers.mentorship.view.fragments


import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.systers.mentorship.R
import org.systers.mentorship.databinding.FragmentEditProfileBinding
import org.systers.mentorship.models.User
import org.systers.mentorship.utils.CommonUtils
import org.systers.mentorship.utils.EditProfileFragmentErrorStates
import org.systers.mentorship.utils.ImageFilePath
import org.systers.mentorship.view.activities.MainActivity
import org.systers.mentorship.viewmodels.ProfileViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

/**
 * The fragment is responsible for editing the User's profile
 */
class EditProfileFragment : DialogFragment() {

    companion object {
        private lateinit var tempUser: User

        /**
         * Creates an instance of EditProfileFragment
         */
        fun newInstance(user: User): EditProfileFragment {
            tempUser = user.copy(id = null, username = null, email = null)
            return EditProfileFragment()
        }
    }

    private val profileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }
    private lateinit var editProfileBinding: FragmentEditProfileBinding
    private lateinit var onDismissListener: DialogInterface.OnDismissListener
    private lateinit var currentUser: User
    var userChoosenTask: String? = null
    var imageFilePath: String? = null
    val REQUEST_CAMERA = 5
    val SELECT_FILE = 6
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    var itemFile: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        profileViewModel.successfulUpdate.observe(this, Observer { successful ->
            (activity as MainActivity).hideProgressDialog()
            editProfileBinding.rlProgressBar.visibility = View.GONE
            if (successful != null) {
                try {
                    rlProgressBar.visibility = View.GONE
                } catch (e: Exception) {
                }

                if (successful) {
                    Toast.makeText(context, getText(R.string.update_successful), Toast.LENGTH_LONG).show()
                    profileViewModel.getProfile()
                    dismiss()
                } else {
                    Toast.makeText(activity, profileViewModel.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        isCancelable = false
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        editProfileBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.fragment_edit_profile, null, false)

        editProfileBinding.user = tempUser.copy()
        currentUser = tempUser.copy()

        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder.setView(editProfileBinding.root)
        dialogBuilder.setTitle(R.string.fragment_title_edit_profile)
        dialogBuilder.setPositiveButton(getString(R.string.save), null)
        dialogBuilder.setNegativeButton(getString(R.string.cancel)) { _, _ -> }

        editProfileBinding.imgUserAvatar.setOnClickListener {
            OnClicktvUploadImage()
        }

        if (!TextUtils.isEmpty(editProfileBinding.user!!.profile_photo)) {
            try {
                Glide.with(this)
                        .load(editProfileBinding.user!!.profile_photo)
                        .placeholder(R.drawable.bg_profile)
                        .listener(object : RequestListener<Drawable> {
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                editProfileBinding.progressBar.visibility = View.GONE
                                return false
                            }

                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                editProfileBinding.progressBar.visibility = View.GONE
                                return false
                            }
                        })
                        .into(editProfileBinding.imgUserAvatar)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {

            editProfileBinding.progressBar.visibility = View.GONE

            editProfileBinding.imgUserAvatar.setImageResource(R.drawable.profile_icon)
        }

        return dialogBuilder.create()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (userChoosenTask.equals("Take Photo")) cameraIntent() else if (userChoosenTask.equals("Choose from Library")) galleryIntent()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data)
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult()
            }
        }
    }

    private fun onSelectFromGalleryResult(data: Intent?) {
        val selectedImageUri = data!!.data
        val picturePath: String = ImageFilePath.getPath(activity!!.applicationContext, selectedImageUri)
        itemFile = File(picturePath)
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, data.data)
                editProfileBinding.imgUserAvatar.setImageBitmap(bm)
                // editProfileBinding.imgUserAvatar.rotation = 90F
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun onCaptureImageResult() {
        val thumbnail = BitmapFactory.decodeFile(imageFilePath) as Bitmap
        itemFile = File(imageFilePath)
        try {
            editProfileBinding.imgUserAvatar.setImageBitmap(thumbnail)
            //   editProfileBinding.imgUserAvatar.rotation = 90F
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()

        val editProfileDialog = dialog as AlertDialog

        imgUserAvatar.setOnClickListener(View.OnClickListener {
            OnClicktvUploadImage()
        })

        editProfileDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val errors = validateProfileInput(editProfileBinding.user?.name?.trim())

            with(editProfileBinding.tiName) {
                this.error = when (errors.firstOrNull()) {
                    is EditProfileFragmentErrorStates.EmptyNameError ->
                        context.getString(R.string.error_empty_name)
                    is EditProfileFragmentErrorStates.NameTooShortError -> {
                        val minLength = resources.getInteger(R.integer.min_name_length)
                        context.getString(R.string.error_name_too_short, minLength)
                    }
                    is EditProfileFragmentErrorStates.NameTooLongError -> {
                        val maxLength = resources.getInteger(R.integer.max_name_length)
                        context.getString(R.string.error_name_too_long, maxLength)
                    }
                    else -> {
                        this.isErrorEnabled = false
                        null
                    }
                }
            }
            //   if (currentUser != editProfileBinding.user && errors.isEmpty()) {
            //editProfileBinding.user?.profile_photo = itemFile!!
            // profileViewModel.updateProfile(editProfileBinding.user!!)

            var body: MultipartBody.Part? = null
            if (itemFile != null) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), itemFile!!)
                body = MultipartBody.Part.createFormData("profile_photo", itemFile!!.name, requestFile)
            }

            val id: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), editProfileBinding.user?.id.toString())
            //   val username: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), editProfileBinding.user!!.username.toString())
            val name: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.name)) editProfileBinding.user?.name.toString() else "")
            //  val email: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), editProfileBinding.user!!.email.toString())
            val bio: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.bio)) editProfileBinding.user?.bio.toString() else "")
            val location: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.location)) editProfileBinding.user?.location.toString() else "")
            val occupation: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.occupation)) editProfileBinding.user?.occupation.toString() else "")
            val organization: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.organization)) editProfileBinding.user?.organization.toString() else "")
            val interests: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.interests)) editProfileBinding.user?.interests.toString() else "")
            val skills: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), if (!TextUtils.isEmpty(editProfileBinding.user?.skills)) editProfileBinding.user?.skills.toString() else "")
            val needMentoring: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), editProfileBinding.user?.needMentoring.toString())
            val availableToMentor: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), editProfileBinding.user?.availableToMentor.toString())

            try {
                editProfileBinding.rlProgressBar.visibility = View.VISIBLE
            } catch (e: Exception) {

            }

            if (body != null) {
                profileViewModel.updateProfileWithFile(body, id, name, bio, location, occupation, organization, interests, skills, needMentoring, availableToMentor)
            } else {
                profileViewModel.updateProfile(editProfileBinding.user?.id, if (!TextUtils.isEmpty(editProfileBinding.user?.name)) editProfileBinding.user?.name else "", if (!TextUtils.isEmpty(editProfileBinding.user?.bio)) editProfileBinding.user?.bio else "", if (!TextUtils.isEmpty(editProfileBinding.user?.location)) editProfileBinding.user?.location else "", if (!TextUtils.isEmpty(editProfileBinding.user?.occupation)) editProfileBinding.user?.occupation else "", if (!TextUtils.isEmpty(editProfileBinding.user?.organization)) editProfileBinding.user?.organization else "", if (!TextUtils.isEmpty(editProfileBinding.user?.interests)) editProfileBinding.user?.interests else "", if (!TextUtils.isEmpty(editProfileBinding.user?.skills)) editProfileBinding.user?.skills else "", editProfileBinding.user?.needMentoring.toString(), editProfileBinding.user?.availableToMentor.toString())
            }
        }
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun OnClicktvUploadImage() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose from Library",
                "Cancel")
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Upload Image")
        builder.setItems(items) { dialog: DialogInterface, item: Int ->
            if (items[item] == "Take Photo") {
                userChoosenTask = "Take Photo"
                cameraIntent()
            } else if (items[item] == "Choose from Library") {
                userChoosenTask = "Choose from Library"
                galleryIntent()
            } else if (items[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()

        profileViewModel.successfulUpdate.removeObservers(activity!!)
        profileViewModel.successfulUpdate.value = null
    }

    private fun validateProfileInput(name: String?): Array<EditProfileFragmentErrorStates> {

        var errors = arrayOf<EditProfileFragmentErrorStates>()

        if (name.isNullOrEmpty()) errors += EditProfileFragmentErrorStates.EmptyNameError

        if (name?.length ?: 0 < resources.getInteger(R.integer.min_name_length)) {
            errors += EditProfileFragmentErrorStates.NameTooShortError
        }

        if (name?.length ?: 0 > resources.getInteger(R.integer.max_name_length)) {
            errors += EditProfileFragmentErrorStates.NameTooLongError
        }
        return errors
    }

    fun setOnDismissListener(onDismissListener: DialogInterface.OnDismissListener?) {
        this.onDismissListener = onDismissListener!!
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog)
        }
    }

    fun galleryIntent() {
        if (CommonUtils.checkPermission(activity!!)) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT //
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
        }
    }

    fun cameraIntent() {
        if (CommonUtils.checkPermissionCamera(activity!!)) {
            val pictureIntent = Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE)
            if (pictureIntent.resolveActivity(activity!!.packageManager) != null) { //Create a itemFile to store the image
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) { // Error occurred while creating the File
                }
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(activity!!, "org.systers.mentorship.provider", photoFile)
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            photoURI)
                    startActivityForResult(pictureIntent,
                            REQUEST_CAMERA)
                }
            }
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        imageFilePath = image.absolutePath
        return image
    }
}
