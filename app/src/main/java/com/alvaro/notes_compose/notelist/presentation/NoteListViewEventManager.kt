package com.alvaro.notes_compose.notelist.presentation

class NoteListViewEventManager {

    private val events = mutableMapOf<String, NoteListEvents>()

    fun addEvent(event: NoteListEvents){
        events[event::class.java.name] = event
    }

    fun removeEvent(event: NoteListEvents){
        events.remove(event::class.java.name)
    }

    fun isEventActive(event: NoteListEvents): Boolean {
        return events[event::class.java.name] != null
    }

}