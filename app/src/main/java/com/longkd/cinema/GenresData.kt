package com.longkd.cinema

import android.content.Context
import com.longkd.cinema.model.Genre
import com.longkd.cinema.utils.ENGLISH
import com.longkd.cinema.utils.TURKISH
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object GenresData {
    var genres: List<Genre> = listOf()
}

//Get genre name return empty if null
fun getGenreName(genreId: Int): String {
    return GenresData.genres.find {
        it.id == genreId
    }?.name ?: ""
}

fun getGenresList(context: Context, currentLanguage: String): List<Genre>? {
    val moshi = Moshi.Builder()
        .build()

    val listType = Types.newParameterizedType(List::class.java, Genre::class.java)
    val adapter: JsonAdapter<List<Genre>> = moshi.adapter(listType)

    val movieGenreJsonFile = when (currentLanguage) {
        ENGLISH -> {
            "movie_genres.json"
        }
        TURKISH -> {
            "movie_genres_tr.json"
        }
        else -> {
            "movie_genres.json"
        }
    }

    val myJson = context.assets.open(movieGenreJsonFile).bufferedReader().use { it.readText() }

    return adapter.fromJson(myJson)
}
