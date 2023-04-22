package com.longkd.cinema

import android.content.SharedPreferences
import androidx.lifecycle.viewModelScope
import com.longkd.cinema.core.BaseViewModel
import com.longkd.cinema.domain.usecase.SetImagesConfigDataUseCase
import com.longkd.cinema.profile.ui.ProfileViewModel
import com.longkd.cinema.ui.onboarding.OnboardingViewModel.Companion.ONBOARDING_KEY
import com.longkd.cinema.utils.ENGLISH
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setImagesConfigDataUseCase: SetImagesConfigDataUseCase,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {

    fun getSetImagesConfigData() {
        viewModelScope.launch {
            setImagesConfigDataUseCase()
        }
    }

    fun getSwitchNotification(): Boolean {
        return sharedPreferences.getBoolean(ProfileViewModel.SWITCH_NOTIFICATION, true)
    }

    fun getShouldShowOnBoarding(): Boolean {
        return sharedPreferences.getBoolean(ONBOARDING_KEY, true)
    }

    fun getCurrentLocale(): String {
        return sharedPreferences.getString("locale", ENGLISH) ?: ENGLISH
    }

}
