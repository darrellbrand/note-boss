package com.djf.noteboss.di
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import android.app.Application
import androidx.room.Room
import com.djf.noteboss.data.data_source.NoteApiService
import com.djf.noteboss.data.data_source.NoteDatabase
import com.djf.noteboss.data.repository.NoteApiRepositoryImpl
import com.djf.noteboss.data.repository.NoteRepositoryImpl
import com.djf.noteboss.domain.repository.NoteApiRepository
import com.djf.noteboss.domain.repository.NoteRepository
import com.djf.noteboss.domain.usecase.CreateLinkUseCase
import com.djf.noteboss.domain.usecase.DeleteNoteUseCase
import com.djf.noteboss.domain.usecase.GetNoteUseCase
import com.djf.noteboss.domain.usecase.GetNotesUseCase
import com.djf.noteboss.domain.usecase.InsertNoteUseCase
import com.djf.noteboss.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(app, NoteDatabase::class.java, NoteDatabase.DATABASENAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }



    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://hidemymind.com/api/note/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NoteApiService {
        return retrofit.create(NoteApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRepository(service: NoteApiService): NoteApiRepository {
        return NoteApiRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideCreateLinkUseCase(repo: NoteApiRepository): CreateLinkUseCase {
        return CreateLinkUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(noteRepo: NoteRepository,noteApiRepository: NoteApiRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(noteRepo),
            getNoteUseCase = GetNoteUseCase(noteRepo),
            deleteNoteUseCase = DeleteNoteUseCase(noteRepo),
            insertNoteUseCase = InsertNoteUseCase(noteRepo),
            createLinkUseCase = CreateLinkUseCase(noteApiRepository)
        )
    }
}