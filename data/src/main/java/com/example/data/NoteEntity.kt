package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class so that the DB knows in which format should store the data
 *
 */
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
)
