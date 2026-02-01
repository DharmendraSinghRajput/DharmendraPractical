package com.ssti.dharmendrapractical.ui.common

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ssti.dharmendrapractical.utils.Resource
import com.ssti.dharmendrapractical.utils.showToast

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    protected val baseActivity: BaseActivity get() = requireActivity() as BaseActivity

    fun showLoader() = baseActivity.showLoader()

    fun hideLoader() = baseActivity.hideLoader()

    fun <T> handleLoader(
        resource: Resource<T>,
        showLoader: Boolean = true,
        swipeRefreshLayout: SwipeRefreshLayout? = null,
        successResponse: (T) -> Unit = {}
    ) {
        when (resource) {
            is Resource.Loading -> {
                if (showLoader) showLoader()
                swipeRefreshLayout?.isRefreshing = true
            }

            is Resource.Success -> {
                hideLoader()
                swipeRefreshLayout?.isRefreshing = false
            }

            is Resource.Error -> {
                hideLoader()
                swipeRefreshLayout?.isRefreshing = false
                showToast(resource.message ?: "Something went wrong")
            }

            is Resource.NoInternet -> {
                hideLoader()
                swipeRefreshLayout?.isRefreshing = false
            }

            else -> {}
        }
    }

    fun showImagePickerDialog() {
        baseActivity.showImagePickerDialog()
    }

    fun observeSelectedImage(): LiveData<Uri> {
        return baseActivity.imageUri
    }

    fun getPrefUtil() = baseActivity.prefUtil

    // Optional: convenience method if many fragments need keyboard hiding
    fun hideKeyboard() {
        baseActivity.hideKeyboard()
    }
}