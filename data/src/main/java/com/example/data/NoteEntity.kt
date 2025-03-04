package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

//нужен для описания сущности базы данных
@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val text: String,
)
