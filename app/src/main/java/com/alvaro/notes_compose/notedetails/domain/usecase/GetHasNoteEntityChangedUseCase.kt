package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.notes_compose.common.domain.Note
import javax.inject.Inject

class GetHasNoteEntityChangedUseCase @Inject constructor(
    private val getNoteFromCache: GetNoteFromCache
) {

    operator fun invoke(currentNote: Note) : Boolean {
        //if no cachedNote found is a new note so no need for further checks
        val cachedNote = getNoteFromCache(currentNote.id) ?: return false

        return !(currentNote.title == cachedNote.title &&
                currentNote.content == cachedNote.content &&
                currentNote.noteType == cachedNote.noteType &&
                currentNote.priority == cachedNote.priority)
    }
}