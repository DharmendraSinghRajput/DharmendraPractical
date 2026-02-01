package com.ssti.dharmendrapractical.utils

import android.app.AlertDialog
import android.content.Context
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.ssti.dharmendrapractical.R
import timber.log.Timber

fun AppCompatActivity.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()

fun Fragment.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this.requireContext(), message, length).show()

fun Context.showToast(message: String, length: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(this, message, length).show()

fun printErrorLog(message: String) = Timber.e("mydata: $message")

fun Context.showAlertDialog(
    title: String = "", message: String = "",
    positiveButtonText: String = "", negativeButtonText: String = "", neutralButtonText: String = "",
    positiveOnClick: () -> Unit, negativeOnClick: () -> Unit, neutralOnClick: () -> Unit
) {
    AlertDialog.Builder(this, R.style.DatePickerDialog).apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonText) { dialog, _ ->
            dialog.dismiss()
            positiveOnClick()
        }
        setNegativeButton(negativeButtonText) { dialog, _ ->
            dialog.dismiss()
            negativeOnClick()
        }
        setNeutralButton(neutralButtonText) { dialog, _ ->
            dialog.dismiss()
            neutralOnClick()
        }
        setCancelable(false)
        show()
    }
}

/*fun Context.shakeItBaby() {
    if (Build.VERSION.SDK_INT >= 26) {
        (this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        (this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(50)
    }
}*/

fun Int.toHexColor() = String.format("#%06X", 0xFFFFFF and this)

/*
fun Context.addSwipeRefreshLayout(rootView: View, setOnRefreshListener: () -> Unit): SwipeRefreshLayout {
    val swipeRefreshLayout = SwipeRefreshLayout(this).apply {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        addView(rootView)
        setOnRefreshListener {
            isRefreshing = false
            setOnRefreshListener()
        }
        setColorSchemeColors(ContextCompat.getColor(this@addSwipeRefreshLayout, R.color.cornflower_blue))
    }
    return swipeRefreshLayout
}
*/

fun String.makeFirstCharCapital() = this.replaceFirstChar { it.uppercase() }

fun RadioGroup.enableDisableRadioButtons(enabled: Boolean) = run {
    this.children.forEach {
        it.isEnabled = enabled
    }
}