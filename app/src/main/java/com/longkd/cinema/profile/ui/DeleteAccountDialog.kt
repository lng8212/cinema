package com.longkd.cinema.profile.ui

import android.app.Activity
import android.view.LayoutInflater
import com.longkd.cinema.core.BaseDialog
import com.longkd.cinema.databinding.DialogDeleteAccountBinding

class DeleteAccountDialog(
    activity: Activity,
    private val listener: DeleteAccountListener
) : BaseDialog(
    activity = activity,
    binding = DialogDeleteAccountBinding.inflate(LayoutInflater.from(activity))
) {

    init {
        binding as DialogDeleteAccountBinding
        binding.cancelButton.setOnClickListener {
            dismissDialog()
        }
        binding.deleteAccountButton.setOnClickListener {
            listener.deleteAccount()
            dismissDialog()
        }
    }

    fun interface DeleteAccountListener {
        fun deleteAccount()
    }

}