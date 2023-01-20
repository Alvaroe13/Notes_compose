package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.notes_compose.common.domain.Note
import com.alvaro.notes_compose.common.domain.NoteRepository
import javax.inject.Inject

class GetNoteFromCache @Inject constructor(private val noteRepository: NoteRepository) {

    operator fun invoke(currentNoteId: String?) : Note? {
        //if no cachedNote found is a new note so no need for further checks
        return noteRepository.getCacheNotes().find { it.id == currentNoteId }
    }
}