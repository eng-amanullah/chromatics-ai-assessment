package com.amanullah.chromaticsaiassessment.data.local.entity

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "blocked_contacts")
data class BlockedContact(
    @PrimaryKey(autoGenerate = false) val phoneNumber: String,
    val name: String,
    val photoUri: String? = null,
    var isBlocked: Boolean = false
)