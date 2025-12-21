package com.ssti.dharmendrapractical.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams
import com.ssti.dharmendrapractical.R

object DialogUtil {

/*
    fun showCommonDialog(
        context: Context,
        title: String, message: String,
        positiveButtonText: String = context.getString(R.string.yes), positiveButtonClick: () -> Unit, setCancelable: Boolean = true,
    ) {

        val mBinding = CommonDialogBinding.inflate(LayoutInflater.from(context))

        Dialog(context).apply {
            mBinding.apply {

                ivIcon.visibility = View.GONE
                vSeparator.visibility = View.GONE
                tvNeutralButton.visibility = View.GONE
                tvNegativeButton.visibility = View.GONE

                tvTitle.text = title
                tvMessage.text = message
                tvPositiveButton.text = positiveButtonText

                tvPositiveButton.setOnClickListener {
                    dismiss()
                    positiveButtonClick()
                }

                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            setContentView(mBinding.root)
            setCancelable(setCancelable)
            window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            show()
        }
    }*/
}