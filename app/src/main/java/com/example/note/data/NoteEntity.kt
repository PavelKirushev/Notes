package com.example.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat

//нужен для описания сущности базы данных
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
)
