package com.longkd.cinema.profile.ui

import android.content.Context
import android.content.SharedPreferences
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.notification.daily.ReminderManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val sharedPreferences: SharedPreferences) : BaseViewModel() {

    fun setSwitchNotification(value: Boolean) {
        sharedPreferences.edit().putBoolean(SWITCH_NOTIFICATION, value).apply()
    }

    fun getSwitchNotification(): Boolean {
        return sharedPreferences.getBoolean(SWITCH_NOTIFICATION, true)
    }

    fun settingReminder(value: Boolean, context: Context) {
        if (value) ReminderManager.startReminder(context)
        else ReminderManager.stopReminder(context)

    }

    companion object {
        const val SWITCH_NOTIFICATION = "SWITCH_NOTIFICATION"
    }
}