package com.amanullah.chromaticsaiassessment.domain.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import com.amanullah.chromaticsaiassessment.data.local.entity.Contact
import javax.inject.Inject

class FetchContactsUseCase @Inject constructor(
    private val contentResolver: ContentResolver
) {
    operator fun invoke(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.PHOTO_URI
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            null
        )

        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoUriIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: "Unknown"
                val number = it.getString(numberIndex) ?: "Unknown"
                val photoUri = it.getString(photoUriIndex)

                contactsList.add(Contact(phoneNumber = number, name = name, photoUri = photoUri))
            }
        }

        return contactsList
    }
}