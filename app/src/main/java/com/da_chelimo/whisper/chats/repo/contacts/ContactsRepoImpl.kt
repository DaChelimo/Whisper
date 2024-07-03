package com.da_chelimo.whisper.chats.repo.contacts

import android.content.Context
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import com.da_chelimo.whisper.chats.domain.Chat
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.chats.repo.contacts.local.dao.LocalContactDao
import com.da_chelimo.whisper.chats.repo.contacts.local.toLocalContacts
import com.da_chelimo.whisper.core.domain.User
import com.da_chelimo.whisper.core.domain.toMiniUser
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

class ContactsRepoImpl(
    private val localContactDao: LocalContactDao,
    private val userRepo: UserRepo
) : ContactsRepo {

    private val firestore = Firebase.firestore

    override val contactsOnWhisper: Flow<List<User>?> =
        localContactDao.getContacts().map { list -> list?.filterNot { it.uid == Firebase.auth.uid } }


    override suspend fun getContactFromUID(userUID: String): User? =
        localContactDao.getContact(userUID)


    override suspend fun checkForPreExistingChat(contactToCheck: User): String? {
        val otherContact = contactToCheck.toMiniUser()
        val myContact = userRepo.getUserFromUID(Firebase.auth.uid!!)?.toMiniUser()


        val firstUserFilter = Filter.and(Filter.equalTo(Chat::firstMiniUser.name, myContact), Filter.equalTo(Chat::secondMiniUser.name, otherContact))
        val secondUserFilter = Filter.and(Filter.equalTo(Chat::secondMiniUser.name, myContact), Filter.equalTo(Chat::firstMiniUser.name, otherContact))

        /**
         * In the chat details, the current user can be the first user and the otherContact be the second user
         * OR vice-versa
         *
         * The two methods below check for both
         */
        val chatExists = Firebase.firestore.collection(ChatRepo.CHAT_DETAILS)
            .where(Filter.or(firstUserFilter, secondUserFilter))
            .get()
            .await()
            .toObjects(Chat::class.java)
            .firstOrNull()

        Timber.d("Chat exists: $chatExists")

        return chatExists?.chatID
//        val chatWithOtherUserAsFirstUser = Firebase.firestore.collection(ChatRepo.CHAT_DETAILS)
//            .whereEqualTo(Chat::firstMiniUser.name, otherContact)
//            .whereEqualTo(Chat::secondMiniUser.name, myContact)
//            .get()
//            .await()
//            .toObjects(Chat::class.java)
//            .firstOrNull()
//
//        val chatWithCurrentUserAsFirstUser = Firebase.firestore.collection(ChatRepo.CHAT_DETAILS)
//            .whereEqualTo(Chat::firstMiniUser.name, myContact)
//            .whereEqualTo(Chat::secondMiniUser.name, otherContact)
//            .get()
//            .await()
//            .toObjects(Chat::class.java)
//            .firstOrNull()
//
//
//
//        return chatWithCurrentUserAsFirstUser?.chatID ?: chatWithOtherUserAsFirstUser?.chatID
    }

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

        localContactDao.refreshContacts(contactsOnWhisper.toLocalContacts())

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