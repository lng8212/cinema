package com.longkd.cinema.auth.domain.usecase

import com.longkd.cinema.R
import com.longkd.cinema.auth.domain.AuthRepository
import com.longkd.cinema.utils.Resource
import com.longkd.cinema.utils.Strings
import com.longkd.cinema.utils.isValidEmail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class LoginWithEmailUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<Unit>> {
        return when {
            email.isValidEmail() -> authRepository.loginWithEmailPassword(email, password)
            email.isValidEmail().not() -> flowOf(Resource.Error(message = Strings.get(R.string.invalid_email_format)))
            else -> flowOf(Resource.Error(message = "Unknown error"))
        }
    }

}
