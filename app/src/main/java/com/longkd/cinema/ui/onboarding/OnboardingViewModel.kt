package com.longkd.cinema.ui.onboarding

import android.content.SharedPreferences
import com.longkd.cinema.core.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    fun setOnBoardingShown() {
        sharedPreferences.edit().putBoolean(ONBOARDING_KEY, false).apply()
    }


    companion object {
        const val ONBOARDING_KEY = "shouldShowOnboarding"
    }
}