package com.da_chelimo.whisper.stories.repo

import android.net.Uri
import com.da_chelimo.whisper.stories.domain.Story
import com.da_chelimo.whisper.stories.domain.StoryPreview
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow

interface StoryRepo {

    companion object {
        const val STORY = "story"

        private const val STORY_DETAILS_COLLECTION_REF = "details"
        private const val STORY_CONTENT_COLLECTION_REF = "content"
        private const val STORY_VIEWERS_COLLECTION_REF = "viewers"

        fun getStoryDetailsCollection(userID: String) =
            Firebase.firestore.collection(STORY_DETAILS_COLLECTION_REF).document(userID)

        fun getStoryCollection(userID: String) =
            Firebase.firestore.collection(STORY)
                .document(STORY_CONTENT_COLLECTION_REF)
                .collection(userID)


        fun getStoryViewersCollection(userID: String) =
            Firebase.firestore.collection(STORY)
                .document(STORY_VIEWERS_COLLECTION_REF)
                .collection(userID)

    }

    // Gets all the stories of the given user
    suspend fun loadStories(authorID: String): List<Story>


    fun getMyStoryPreview(): Flow<StoryPreview?>
    fun getStoryPreviews(): Flow<List<StoryPreview>?>

    /**
     * Fetches the stories for a given userID
     *
     * @param userID - The uid of the use to fetch from
     */
    suspend fun getStoriesForUID(userID: String): List<Story>

    /**
     * Posts an image to the user's story
     */
    suspend fun postStory(localImageUri: Uri, storyCaption: String, userID: String)

    /**
     * Watch the story of a user... Updates the user's viewerCount
     */
    suspend fun watchStory(storyAuthor: String, storyID: String, currentUserID: String)



}