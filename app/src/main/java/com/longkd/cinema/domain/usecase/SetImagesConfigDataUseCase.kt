package com.longkd.cinema.domain.usecase

import com.longkd.cinema.domain.repository.MoviesRepository

class SetImagesConfigDataUseCase(
    private val moviesRepository: MoviesRepository
) {
    suspend operator fun invoke() = moviesRepository.getSetConfigurationData()
}
