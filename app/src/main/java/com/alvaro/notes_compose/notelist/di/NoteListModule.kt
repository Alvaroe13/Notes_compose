package com.alvaro.notes_compose.notelist.di

import com.alvaro.core.util.Logger
import com.alvaro.notes_compose.common.domain.NoteRepository
import com.alvaro.notes_compose.notelist.domain.usecase.DeleteNote
import com.alvaro.notes_compose.notelist.domain.usecase.GetNotes
import com.alvaro.notes_compose.notelist.domain.usecase.RemoveNoteFromCacheUseCase
import com.alvaro.notes_compose.notelist.presentation.NoteListViewEventManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NoteListModule {

    @Provides
    @Singleton
    @Named("NoteListView")
    fun provideLogger(): Logger {
        return Logger(
            tag = "NoteListView",
            isDebug = true
        )
    }

    @Provides
    @Singleton
    fun provideGetNotesInteractor(noteRepository: NoteRepository) : GetNotes {
        return GetNotes(noteRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteNoteInteractor(noteRepository: NoteRepository): DeleteNote {
        return DeleteNote(noteRepository)
    }

    @Provides
    @Singleton
    fun provideRestoreNotesInteractor(noteRepository: NoteRepository): RemoveNoteFromCacheUseCase {
        return RemoveNoteFromCacheUseCase(noteRepository)
    }

    @Provides
    @Singleton
    fun provideNoteListViewEventManager(): NoteListViewEventManager {
        return NoteListViewEventManager()
    }
}