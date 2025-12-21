package com.ssti.dharmendrapractical.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.utils.NetworkUtil
import com.ssti.dharmendrapractical.utils.PermissionUtil
import com.ssti.dharmendrapractical.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
open class BaseActivity() : AppCompatActivity() {
    private lateinit var loader: Dialog
    private var resultUri: Uri? = null
    private lateinit var pickImageContract: ActivityResultLauncher<String>

    private var _imageUri = MutableLiveData<Uri>()
    var imageUri: LiveData<Uri> = _imageUri
    private lateinit var captureImageContract: ActivityResultLauncher<Uri>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        filesDir.listFiles()?.let {
            for (file in it) {
                file.delete()
            }
        }

        loader = Dialog(this).apply {
            setContentView(R.layout.item_loader)
            window?.setBackgroundDrawableResource(R.color.transparent)
            setCancelable(false)
        }

        pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri ->
            imageUri?.let {
                this._imageUri.value = it
            }
        }

        captureImageContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && resultUri != null) {
                resultUri?.let {
                    this._imageUri.value = it
                }
            }
        }
    }



    fun handleLoader(resource: Resource<Any>, showLoader: Boolean = true, swipeRefreshLayout: SwipeRefreshLayout? = null, successResponse: (Resource<Any>) -> Unit) {
        swipeRefreshLayout?.isRefreshing = false
        when (resource) {
            is Resource.Idle -> {
                Timber.Forest.v("okhttp: State Error")
                hideLoader()
            }

            is Resource.Error -> {
                Timber.Forest.v("okhttp: State Error")
                hideLoader()
            }

            is Resource.Loading -> {
                Timber.Forest.v("okhttp: State Loading")
                if (showLoader)
                    showLoader()
            }

            is Resource.NoInternet -> {
                Timber.Forest.v("okhttp: State NoInternet")
                hideLoader()
                NetworkUtil.showNetworkDialog(this) { }
            }

            is Resource.Success -> {
//                FileReadWriteUtil(this).writeFileOnInternalStorage("API_Response.txt", GeneralFunctions.prettifyJson(Gson().toJson(resource.data))!!)
                Timber.Forest.v("okhttp: State Success")
                hideLoader()
                successResponse(resource)
            }
        }
    }

    fun showImagePickerDialog() {
        if (PermissionUtil.verifyPermissions(this)) {
            try {
                val imageItems = arrayOf<CharSequence>(
                    getString(R.string.take_picture),
                    getString(R.string.choose_from_gallery),
                    getString(R.string.cancel))
                val builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.select_one))
                builder.setItems(imageItems) { dialog, item ->
                    when {
                        imageItems[item] == getString(R.string.take_picture) -> {
                            dialog.dismiss()
                            createImageURI()?.let { uri ->
                                captureImage(uri)
//                                captureImage(Uri.parse(filesDir.path))
                            }
                        }

                        imageItems[item] == getString(R.string.choose_from_gallery) -> {
                            dialog.dismiss()
                            pickImage()
                        }

                        imageItems[item] == getString(R.string.cancel) -> {
                            dialog.dismiss()
                        }
                    }
                }
                builder.show()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            }
        }
    }
    private fun createImageURI(): Uri? {

        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        else
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val imageName = "${getString(R.string.app_name)}_${System.currentTimeMillis()}"

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val finalURI = contentResolver.insert(imageCollection, contentValues)
        resultUri = finalURI
        return finalURI
    }

    private fun captureImage(uri: Uri) = captureImageContract.launch(uri)
    protected fun pickImage() = pickImageContract.launch("image/*")

    fun showLoader() {
        if (!loader.isShowing)
            loader.show()
    }

    fun hideLoader() {
        if (loader.isShowing)
            loader.dismiss()
    }
}