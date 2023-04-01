package com.longkd.cinema.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.longkd.cinema.data.local.model.MovieEntity
import com.longkd.cinema.data.local.model.ShowEntity

@Database(
    entities = [MovieEntity::class, ShowEntity::class],
    version = 2,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract val dao: MovieDao
}
