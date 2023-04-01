package com.longkd.cinema.data.remote.model

data class ConfigurationResponse(
    val change_keys: List<String>,
    val images: ImagesConfigurationsData
)
