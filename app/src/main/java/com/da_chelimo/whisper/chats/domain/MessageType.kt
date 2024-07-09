package com.da_chelimo.whisper.chats.domain

import com.da_chelimo.whisper.chats.domain.MessageType.Audio
import com.da_chelimo.whisper.chats.domain.MessageType.Image
import com.da_chelimo.whisper.chats.domain.MessageType.Text
import com.da_chelimo.whisper.chats.domain.MessageType.Types
import com.da_chelimo.whisper.chats.domain.MessageType.Video
import com.da_chelimo.whisper.core.utils.formatDurationInMillis
import kotlinx.serialization.Serializable

@Serializable
sealed class MessageType(open var message: String, open var type: Types) {

    enum class Types { Text, Image, Video, Audio }

    constructor() : this("", Types.Text)

    data class Text(override var message: String, override var type: Types = Types.Text) :
        MessageType(message, type)

    data class Image(
        override var message: String,
        val imageUrl: String,
        override var type: Types = Types.Image
    ) : MessageType(message, type)

    data class Video(
        override var message: String,
        val videoUrl: String,
        override var type: Types = Types.Video
    ) : MessageType(message, type)

    data class Audio(
        val duration: Long, // Time in millis
        val audioUrl: String,
        override var type: Types = Types.Audio
    ) : MessageType(duration.formatDurationInMillis(), type)


    fun toFirebaseMap(): Map<String, Any> {
        val messageKey = MessageType::message.name
        val typeKey = MessageType::type.name

        val imageUrlKey = Image::imageUrl.name
        val videoUrlKey = Video::videoUrl.name

        val durationKey = Audio::duration.name
        val audioUrlKey = Audio::audioUrl.name

        return when (this) {
            is Text -> mapOf(
                messageKey to message,
                typeKey to type
            )

            is Image -> mapOf(
                messageKey to message,
                typeKey to type,
                imageUrlKey to imageUrl
            )

            is Video -> mapOf(
                messageKey to message,
                typeKey to type,
                videoUrlKey to videoUrl
            )

            is Audio -> mapOf(
                durationKey to duration,
                typeKey to type,
                audioUrlKey to (audioUrl ?: "")
            )
        }
    }
}


fun Map<String, Any?>.toMessageType(): MessageType {
    val mapOf = this

    if (mapOf.isEmpty()) // Default implementation
        return Text("")

    val typeAsString = mapOf.getOrElse(MessageType::type.name) { Types.Text }
    val allTypes = Types.entries.map { it.name }

    return when (val type = Types.valueOf(allTypes.first { it == typeAsString })) {
        Types.Text -> Text(mapOf[MessageType::message.name] as String, type)
        Types.Image -> Image(
            message = mapOf[MessageType::message.name] as String,
            imageUrl = mapOf[Image::imageUrl.name] as String,
            type = type
        )

        Types.Audio -> Audio(
            mapOf[Audio::duration.name] as Long,
            audioUrl = mapOf[Audio::audioUrl.name] as String
        )

        Types.Video -> Video(
            message = mapOf[MessageType::message.name] as String,
            videoUrl = mapOf[Video::videoUrl.name] as String,
            type = type
        )
    }
}