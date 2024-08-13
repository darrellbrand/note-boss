package com.djf.noteboss.di

import android.app.Application
import androidx.room.Insert
import androidx.room.Room
import androidx.room.RoomDatabase
import com.djf.noteboss.data.data_source.NoteDatabase
import com.djf.noteboss.data.repository.NoteRepositoryImpl
import com.djf.noteboss.domain.repository.NoteRepository
import com.djf.noteboss.domain.usecase.DeleteNoteUseCase
import com.djf.noteboss.domain.usecase.GetNoteUseCase
import com.djf.noteboss.domain.usecase.GetNotesUseCase
import com.djf.noteboss.domain.usecase.InsertNoteUseCase
import com.djf.noteboss.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASENAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(noteRepo: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(noteRepo),
            getNoteUseCase = GetNoteUseCase(noteRepo),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepo),
            insertNoteUseCase = InsertNoteUseCase(noteRepo)
        )
    }
}