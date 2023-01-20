package com.alvaro.notes_compose.notedetails.domain.usecase

import com.alvaro.notes_compose.common.domain.Note
import javax.inject.Inject

class GetIsValidNote @Inject constructor() {

    operator fun invoke(note: Note) : Boolean {
        return note.title.isNotBlank() && note.content.isNotBlank()
    }
}