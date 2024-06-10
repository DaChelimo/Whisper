package com.da_chelimo.whisper.chats.repo.contacts

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.da_chelimo.whisper.chats.repo.contacts.local.dao.LocalContactDao
import com.da_chelimo.whisper.chats.repo.contacts.local.toLocalContacts
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class ContactsRepoImpl(private val localContactDao: LocalContactDao) : ContactsRepo {

    private val firestore = Firebase.firestore

    override val contactsOnWhisper: Flow<List<User>> =
        localContactDao.getContacts()


    override suspend fun getContactFromUID(userUID: String): User? =
        localContactDao.getContact(userUID)


    /**
     * Gets all the contacts on the user's phone, breaks them into groups of 30 (maximum for Firebase queries) and
     * searches for them on Firebase Firestore
     */
    override suspend fun refreshContactsOnWhisper(context: Context) {
        val phoneContacts = getContactsOnPhone(context).values.toMutableList()
        val numOfLists = phoneContacts.count() / 30 + 1
        val shorterPhoneContacts = (0..numOfLists).map { index ->
            val lastIndex =
                if ((phoneContacts.count() - 1) < 0) 0
                else phoneContacts.count() - 1

            val fromIndex = (index * 30).coerceAtLeast(0).coerceAtMost(lastIndex)
            val toIndex =
                ((index + 1) * 30).coerceAtLeast(0).coerceAtMost(lastIndex)

            if (toIndex > fromIndex)
                phoneContacts.subList(fromIndex, toIndex)
            else
                listOf()
        }.filter { it.isNotEmpty() }

        val contactsOnWhisper = mutableListOf<User>()
        shorterPhoneContacts.forEach { listToCheck ->
            contactsOnWhisper.addAll(
                firestore
                    .collection(UserRepo.USERS_COLLECTION)
                    .whereIn(User::number.name, listToCheck)
                    .get()
                    .await()
                    .toObjects(User::class.java)
            )
        }

        localContactDao.insertData(contactsOnWhisper.toLocalContacts())
        Timber.d("contactsOnWhisper is $contactsOnWhisper")
    }


    private suspend fun getContactsOnPhone(context: Context): Map<String, String> =
        withContext(Dispatchers.IO) {
            val cursor = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                CONTACTS_PROJECTION,
                null, // SELECTION,
                null, //SELECTION_ARGS,
                Contacts.DISPLAY_NAME
            )

            val contacts = mutableMapOf<String, String>()

            cursor?.let {
                while (it.moveToNext()) {
                    val contactName =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY))
                    val contactNumber =
                        it.getString(it.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER))

                    try {
                        contacts[contactName] = contactNumber
                    } catch (_: NullPointerException) {
                    }
                }
            }

            cursor?.close()
            return@withContext contacts
        }


    companion object {
        val CONTACTS_PROJECTION = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER
        )
    }

}