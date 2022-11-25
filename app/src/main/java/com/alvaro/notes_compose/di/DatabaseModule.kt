package com.alvaro.notes_compose.di

import android.content.Context
import androidx.room.Room
import com.alvaro.notes_compose.common.data.NoteMapper
import com.alvaro.notes_compose.common.data.NoteRepositoryImpl
import com.alvaro.notes_compose.common.data.local.NotesDao
import com.alvaro.notes_compose.common.data.local.NotesDatabase
import com.alvaro.notes_compose.common.domain.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NotesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            NotesDatabase::class.java,
            NotesDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteDao(noteDatabase: NotesDatabase): NotesDao {
        return noteDatabase.getDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDatabase: NotesDatabase, noteMapper: NoteMapper): NoteRepository {
        return NoteRepositoryImpl(noteDatabase , noteMapper)
    }

    @Provides
    @Singleton
    fun provideNoteMapper(): NoteMapper {
        return NoteMapper()
    }

}