package com.ssti.dharmendrapractical.utils

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import com.ssti.dharmendrapractical.R

object NetworkUtil {

    fun showNetworkDialog(
        context: Context, title: String = context.getString(R.string.no_internet_connection),
        message: String = context.getString(R.string.turn_on_internet_then_click_on_retry_to_continue), positiveButtonClick: () -> Unit
    ) {
        AlertDialog.Builder(context, R.style.DatePickerDialog).apply {
            setTitle(title)
            setMessage(message)
            setIcon(android.R.drawable.ic_dialog_alert)
            setPositiveButton(context.getString(R.string.retry)) { dialogInterface, which ->
                positiveButtonClick()
                dialogInterface.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

}