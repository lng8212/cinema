package com.longkd.cinema.profile.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.longkd.cinema.R
import com.longkd.cinema.databinding.DialogLogoutBinding
import com.longkd.cinema.utils.clearBlurEffect
import com.longkd.cinema.utils.showBlurEffect

@SuppressLint("NewApi")
class LogoutDialog(
    private val activity: Activity,
    private val listener: LogOutRequestListener
) {
    var dialog: AlertDialog? = null

    fun startDialog() {
        val binding = DialogLogoutBinding.inflate(LayoutInflater.from(activity))
        val mBuilder = AlertDialog.Builder(activity).setView(binding.root)

        dialog = mBuilder.show()
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.bg_transparent_view))
        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialog?.setOnCancelListener {
            clearBlurEffect(activity)
        }
        showBlurEffect(activity)

        with(binding) {
            cancelButton.setOnClickListener {
                dismissDialog()
            }

            logoutButton.setOnClickListener {
                listener.logOut()
                dismissDialog()
            }
        }
    }

    private fun dismissDialog() {
        if (dialog != null) {
            dialog?.dismiss()
            clearBlurEffect(activity)
        }
    }

    fun interface LogOutRequestListener {
        fun logOut()
    }
}
