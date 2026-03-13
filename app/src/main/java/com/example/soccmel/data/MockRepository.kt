package com.example.soccmel.data

import com.example.soccmel.model.BolognaEvent
import com.example.soccmel.model.Poll
import com.example.soccmel.model.PollOption
import com.example.soccmel.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object MockRepository {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _polls = MutableStateFlow<List<Poll>>(emptyList())
    val polls = _polls.asStateFlow()

    private val _events = MutableStateFlow<List<BolognaEvent>>(emptyList())
    val events = _events.asStateFlow()

    init {
        // Initial Mock Data
        val u1 = User(name = "Zàufer", email = "zaufer@soccmel.bo")
        val u2 = User(name = "BologneseDoc", email = "bolognesedoc@soccmel.bo")
        
        val p1 = Poll(
            question = "Dove si mangiano i migliori tortellini?",
            creatorId = u1.id,
            creatorName = u1.name,
            options = listOf(
                PollOption(text = "Sfoglia Rina", votes = 12),
                PollOption(text = "Trattoria Anna Maria", votes = 8),
                PollOption(text = "Da Nonna (a casa)", votes = 25)
            ),
            comments = emptyList()
        )

        val p2 = Poll(
            question = "T-Days nel weekend: favorevole o contrario?",
            creatorId = u2.id,
            creatorName = u2.name,
            options = listOf(
                PollOption(text = "Favorevolissimo", votes = 30),
                PollOption(text = "Preferivo le auto", votes = 5)
            ),
            comments = emptyList()
        )
        
        _polls.value = listOf(p1, p2)

        _events.value = listOf(
            BolognaEvent(
                title = "Mercato Ritrovato",
                description = "Il mercato dei produttori locali alla Cineteca.",
                date = "Ogni Sabato mattina",
                location = "Piazzetta Pasolini",
                category = "Cibo"
            ),
            BolognaEvent(
                title = "Cinema Sotto le Stelle",
                description = "Proiezioni gratuite in Piazza Maggiore.",
                date = "Luglio - Agosto",
                location = "Piazza Maggiore",
                category = "Cultura"
            ),
            BolognaEvent(
                title = "Salita a San Luca",
                description = "Passeggiata di gruppo sotto il portico più lungo del mondo.",
                date = "Domenica 15 Marzo",
                location = "Arco del Meloncello",
                category = "Sport"
            )
        )
    }

    fun login(username: String) {
        _currentUser.value = User(name = username, email = "$username@soccmel.bo")
    }

    fun logout() {
        _currentUser.value = null
    }

    fun createPoll(question: String, options: List<String>) {
        val user = _currentUser.value ?: return
        val newPoll = Poll(
            question = question,
            creatorId = user.id,
            creatorName = user.name,
            options = options.map { PollOption(text = it) }
        )
        _polls.update { listOf(newPoll) + it }
    }

    fun vote(pollId: String, optionId: String) {
        val userId = _currentUser.value?.id ?: return
        
        _polls.update { currentPolls ->
            currentPolls.map { poll ->
                if (poll.id == pollId) {
                    val updatedOptions = poll.options.map { option ->
                        if (option.id == optionId) {
                            // Simple toggle logic
                            if (option.voters.contains(userId)) {
                                option.copy(votes = option.votes - 1, voters = (option.voters - userId).toMutableList())
                            } else {
                                option.copy(votes = option.votes + 1, voters = (option.voters + userId).toMutableList())
                            }
                        } else {
                            option
                        }
                    }
                    poll.copy(options = updatedOptions)
                } else {
                    poll
                }
            }
        }
    }
}
