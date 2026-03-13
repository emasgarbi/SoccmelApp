package com.example.soccmel.model

import java.util.UUID

data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String, // Required for real registration
    val avatarUrl: String? = null,
    val bio: String? = null,
    val website: String? = null,
    val location: String? = null,
    val joinDate: String? = null,
    val interests: List<String> = emptyList(),
    val citizenshipScore: Int = 0, // Replaces generic 'score' for healthy sociality
    val metUserIds: List<String> = emptyList() // List of User IDs met in person
)

data class Comment(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val userName: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class Poll(
    val id: String = UUID.randomUUID().toString(),
    val question: String,
    val creatorId: String,
    val creatorName: String,
    val options: List<PollOption>,
    val comments: List<Comment> = emptyList(), // Encourage dialogue
    val timestamp: Long = System.currentTimeMillis()
)

data class PollOption(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    var votes: Int = 0,
    val voters: MutableList<String> = mutableListOf() // List of User IDs who voted
)

data class BolognaEvent(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String, // e.g., "Cultura", "Cibo", "Mobilità"
    val imageUrl: String? = null,
    val isPublic: Boolean = true, // Public city event vs Private friend gathering
    val isFixed: Boolean = false, // Recurring city events (no email contact) vs user/public specific events
    val supportContact: String? = null, // Email, phone or link for public events
    val organizerId: String? = null, // For private events, who organized it
    val participantIds: List<String> = emptyList(), // List of User IDs who joined the event
    val status: EventStatus = EventStatus.PLANNED // Current status of the event
)

enum class EventStatus {
    PLANNED, COMPLETED, CANCELLED
}

data class DialectWord(
    val word: String,
    val meaning: String,
    val example: String? = null
)

data class FriendRequest(
    val id: String = UUID.randomUUID().toString(),
    val fromUserId: String,
    val fromUserName: String,
    val toUserId: String,
    val status: FriendRequestStatus = FriendRequestStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis()
)

enum class FriendRequestStatus {
    PENDING, ACCEPTED, REJECTED
}
