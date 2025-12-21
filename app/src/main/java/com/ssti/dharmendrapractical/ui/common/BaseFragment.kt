package com.ssti.dharmendrapractical.ui.common

import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ssti.dharmendrapractical.utils.Resource

open class BaseFragment(layoutId: Int) : Fragment(layoutId) {
    // Calling Base Activity's handleLoader
    fun handleLoader(resource: Resource<Any>, showLoader: Boolean = true, swipeRefreshLayout: SwipeRefreshLayout? = null, successResponse: (Resource<Any>) -> Unit) {
        (this.activity as BaseActivity).handleLoader(resource, showLoader, swipeRefreshLayout = swipeRefreshLayout) { response ->
            successResponse(response)
        }
    }

/*    fun showImagePickerDialog() {
        (this.activity as BaseActivity).showImagePickerDialog()
    }*/

   // fun observeImageURI() = (this.activity as BaseActivity).imageUri

    //fun getPrefUtil() = (this.activity as BaseActivity).prefUtil

    fun showLoader() = (this.activity as BaseActivity).showLoader()

    fun hideLoader() = (this.activity as BaseActivity).hideLoader()
}