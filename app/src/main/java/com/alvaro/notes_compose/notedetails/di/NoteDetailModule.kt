package com.alvaro.notes_compose.notedetails.di

import com.alvaro.core.util.Logger
import com.alvaro.core.util.TimeStampGenerator
import com.alvaro.notes_compose.common.domain.NoteRepository
import com.alvaro.notes_compose.notedetails.domain.usecase.GetNoteById
import com.alvaro.notes_compose.notedetails.domain.usecase.InsertNote
import com.alvaro.notes_compose.notedetails.domain.usecase.UpdateNote
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteDetailModule {

    @Provides
    @Singleton
    @Named("NoteDetailView")
    fun provideLogger(): Logger {
        return Logger(
            tag = "NoteDetailView",
            isDebug = true
        )
    }

    @Provides
    @Singleton
    fun provideInsertNoteInteractor(
        noteRepository: NoteRepository,
        timeStampGenerator: TimeStampGenerator
    ) : InsertNote {
        return InsertNote(
            noteRepository,
            timeStampGenerator
        )
    }

    @Provides
    @Singleton
    fun provideGetNoteByIdInteractor(noteRepository: NoteRepository) : GetNoteById {
        return GetNoteById(noteRepository)
    }

    @Provides
    @Singleton
    fun provideTimeStampGenerator() : TimeStampGenerator {
        return TimeStampGenerator()
    }

    @Provides
    @Singleton
    fun provideUpdateNoteInteractor(noteRepository: NoteRepository) : UpdateNote {
        return UpdateNote(noteRepository)
    }

}