package com.ssti.dharmendrapractical.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ssti.dharmendrapractical.R
import com.ssti.dharmendrapractical.utils.NetworkUtil
import com.ssti.dharmendrapractical.utils.PermissionUtil
import com.ssti.dharmendrapractical.utils.PrefUtil
import com.ssti.dharmendrapractical.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    private lateinit var loader: Dialog

    @Inject
    lateinit var prefUtil: PrefUtil

    private val _imageUri = MutableLiveData<Uri>()
    val imageUri: LiveData<Uri> get() = _imageUri

    private var cameraImageUri: Uri? = null

    private lateinit var pickImageContract: ActivityResultLauncher<String>
    private lateinit var captureImageContract: ActivityResultLauncher<Uri>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // IMPORTANT: Removed dangerous filesDir deletion
        // filesDir.listFiles()?.forEach { it.delete() }

        loader = Dialog(this).apply {
            setContentView(R.layout.item_loader)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }

        pickImageContract = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { _imageUri.value = it }
        }

        captureImageContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success && cameraImageUri != null) {
                _imageUri.value = cameraImageUri
            }
            // Always clear after callback
            cameraImageUri = null
        }
    }

    // ────────────────────────────────────────────────
    //               Loader Management
    // ────────────────────────────────────────────────

    fun showLoader() {
        if (!loader.isShowing) {
            loader.show()
        }
    }

    fun hideLoader() {
        if (loader.isShowing) {
            loader.dismiss()
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

    // ────────────────────────────────────────────────
    //               Image Picker / Camera
    // ────────────────────────────────────────────────

    fun showImagePickerDialog() {
        if (!PermissionUtil.verifyPermissions(this)) {
            // You should request permissions here if verifyPermissions only checks
            // For simplicity assuming PermissionUtil handles request if needed
            return
        }

        val options = arrayOf(
            getString(R.string.take_picture),
            getString(R.string.choose_from_gallery),
            getString(R.string.cancel)
        )

        AlertDialog.Builder(this)
            .setTitle(R.string.select_one)
            .setItems(options) { dialog, index ->
                when (options[index]) {
                    getString(R.string.take_picture) -> {
                        launchCamera()
                    }
                    getString(R.string.choose_from_gallery) -> {
                        pickImageContract.launch("image/*")
                    }
                    else -> { /* cancel */ }
                }
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun launchCamera() {
        val uri = createImageCaptureUri() ?: return

        cameraImageUri = uri
        captureImageContract.launch(uri)
    }

    private fun createImageCaptureUri(): Uri? {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val name = "${getString(R.string.app_name)}_${System.currentTimeMillis()}.jpg"

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            // Optional: add more metadata if needed
            // put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp/")
        }

        return try {
            contentResolver.insert(collection, values)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // ────────────────────────────────────────────────
    //               Utility Methods
    // ────────────────────────────────────────────────

    fun openActivity(target: Class<*>) {
        startActivity(Intent(this, target))
    }

    fun openActivityAndFinish(target: Class<*>) {
        startActivity(Intent(this, target))
        finish()
    }

    fun hideKeyboard() {
        val view = currentFocus ?: View(this)
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}