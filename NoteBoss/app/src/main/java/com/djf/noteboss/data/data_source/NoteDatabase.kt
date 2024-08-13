package com.djf.noteboss.data.data_source

import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.djf.noteboss.domain.model.Note
import kotlinx.coroutines.flow.Flow

@Database(
    entities = [Note::class], version = 1
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    companion object{
        const val DATABASENAME = "note_db"
    }
}