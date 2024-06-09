package com.da_chelimo.whisper.chats.repo.contacts.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.da_chelimo.whisper.core.domain.User

@Entity
data class LocalContact(
    @ColumnInfo("contact")
    var contact: User,

    @PrimaryKey
    val id: String = contact.uid,
)


