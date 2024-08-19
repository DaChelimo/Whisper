package com.da_chelimo.whisper.stories.repo

import android.net.Uri
import com.da_chelimo.whisper.chats.domain.getOtherUser
import com.da_chelimo.whisper.chats.repo.chats.ChatRepo
import com.da_chelimo.whisper.core.repo.user.UserRepo
import com.da_chelimo.whisper.stories.domain.Story
import com.da_chelimo.whisper.stories.domain.StoryPreview
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID

class StoryRepoImpl(
    private val userRepo: UserRepo,
    private val chatRepo: ChatRepo
) : StoryRepo {

    override suspend fun postStory(
        localImageUri: Uri,
        storyCaption: String,
        currentUserID: String
    ) {
        val storyID = UUID.randomUUID().toString()
        val currentUser = Firebase.auth.uid?.let { userRepo.getUserFromUID(it) }


        val imageUrl = Firebase.storage.getReference("${StoryRepo.STORY}/$currentUserID/$storyID")
            .putFile(localImageUri)
            .await().storage.downloadUrl
            .await().toString()

        val story = Story(
            storyID = storyID,
            authorUID = currentUserID,
            imageUrl = imageUrl,
            storyCaption = storyCaption
        )
        StoryRepo.getStoryCollection(currentUserID)
            .document(story.storyID)
            .set(story)


        val oldStoryCount =
            StoryRepo.getStoryDetailsCollection(currentUserID).get().await()
                .toObject<StoryPreview>()?.storyCount ?: 0
        val storyPreview = StoryPreview(
            authorID = currentUserID,
            authorProfilePic = currentUser?.profilePic,
            authorName = currentUser?.name ?: "--",
            storyCount = oldStoryCount + 1,
            previewImage = imageUrl,
            timeUploaded = System.currentTimeMillis()
        )
        StoryRepo.getStoryDetailsCollection(currentUserID)
            .set(storyPreview)
    }

    // Gets all the stories of the given user
    override suspend fun loadStories(authorID: String): List<Story> {
        val stories = getStoriesQuery(authorID)
            .orderBy(Story::timeUploaded.name, Query.Direction.ASCENDING)
            .get().await().toObjects(Story::class.java)
            .filterNotNull()

        Timber.d("Stories is $stories")
        return stories
    }


    private fun getStoriesQuery(authorID: String) =
        StoryRepo
        .getStoryCollection(authorID)


    override fun getMyStoryPreview() = callbackFlow {
        val storyPreviewListener = Firebase.auth.uid?.let { uid ->
            StoryRepo
                .getStoryDetailsCollection(uid)
                .addSnapshotListener { value, error ->
                    error?.printStackTrace()

                    val myStoryPreview = value?.toObject<StoryPreview>()
                    Timber.d("myStoryPreview is $myStoryPreview")

                    trySend(myStoryPreview)
                }
        }

        awaitClose {
            storyPreviewListener?.remove()
        }
    }

    override fun getStoryPreviews() = callbackFlow {
        Firebase.auth.uid?.let { uid -> // TODO: Make this more efficient
            chatRepo.getChatsForUser(uid).collectLatest { chats ->
                val storyPreviews = chats?.mapNotNull {
                    getStoryPreview(it.getOtherUser().uid)
                } ?: listOf()

                trySend(storyPreviews)
            }
        }

        awaitClose()
    }

    private suspend fun getStoryPreview(userID: String) =
        StoryRepo.getStoryDetailsCollection(userID).get().await().toObject<StoryPreview>()

    override suspend fun getStoriesForUID(userID: String) =
        getStoriesQuery(userID).get().await().toObjects<Story>()


    private suspend fun addStoryToStoryDetails(story: Story, userID: String) {
        val currentUser = userRepo.getUserFromUID(userID)
        val oldStoryPreview =
            StoryRepo.getStoryDetailsCollection(userID).get().await().toObject<StoryPreview>()

        val storyPreview =
            StoryPreview(
                authorID = Firebase.auth.uid!!,
                authorProfilePic = currentUser?.profilePic,
                authorName = currentUser?.name ?: "",
                storyCount = (oldStoryPreview?.storyCount ?: 0) + 1,
                previewImage = story.imageUrl,
                timeUploaded = story.timeUploaded
            )
        StoryRepo.getStoryDetailsCollection(userID).set(storyPreview)
    }


    private fun removeStoryFromStoryCollection(storyID: String, userID: String) =
        StoryRepo.getStoryCollection(userID).document(storyID).delete().isSuccessful

    private suspend fun removeStoryFromStoryDetails(story: Story, userID: String) {
        val oldStoryPreview =
            StoryRepo.getStoryDetailsCollection(userID).get().await().toObject<StoryPreview>()
                ?: return
        val oldStoryCount = oldStoryPreview.storyCount

        // We are removing the last story
        if (oldStoryCount == 1) {
            StoryRepo.getStoryDetailsCollection(userID).delete()
            return
        }


        val newLastStory = getLastStory(userID) ?: return
        val newLastStoryPreview =
            StoryPreview(
                authorID = Firebase.auth.uid!!,
                authorProfilePic = oldStoryPreview.authorProfilePic,
                authorName = oldStoryPreview.authorName,
                storyCount = oldStoryPreview.storyCount - 1,
                previewImage = newLastStory.imageUrl,
                timeUploaded = newLastStory.timeUploaded
            )

        StoryRepo.getStoryDetailsCollection(userID).set(newLastStoryPreview)
    }

    private suspend fun getLastStory(userID: String) =
        getStoriesQuery(userID)
            .orderBy(Story::timeUploaded.name, Query.Direction.DESCENDING)
            .limit(1)
            .get().await().toObjects<Story>().firstOrNull()


    override suspend fun deleteStory(storyID: String) {
        val storyDetailsRef = StoryRepo.getStoryDetailsCollection(Firebase.auth.uid ?: return)
        val storyDetails = storyDetailsRef.get().await().toObject<StoryPreview>()
        storyDetailsRef.update(StoryPreview::storyCount.name, storyDetails?.storyCount?.minus(1) ?: 0)

        val storyRef = StoryRepo.getStoryCollection(Firebase.auth.uid!!).document(storyID)
        Timber.d("Delete: storyRef is ${storyRef.get().await().toObject<Story>()}")
        val isSuccess = storyRef.delete()
        Timber.d("isSuccess is ${isSuccess.isSuccessful}")
//        storyRef.update(Story::active.name, false)
    }

    override suspend fun watchStory(storyAuthor: String, storyID: String, currentUserID: String) {
        TODO("Not yet implemented")
    }
}