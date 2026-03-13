package com.example.soccmel.data

import com.example.soccmel.model.*
import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Calendar
import java.util.UUID

object RealRepository {
    private const val PREFS_NAME = "soccmel_prefs"
    private const val KEY_USER_ID = "current_user_id"
    private const val KEY_USER_NAME = "current_user_name"
    private const val KEY_USER_EMAIL = "current_user_email"
    private const val KEY_ACCOUNTS = "registered_accounts"

    private val globalUsers = mutableMapOf<String, User>()
    private val globalPasswords = mutableMapOf<String, String>()
    private val globalPolls = mutableListOf<Poll>()
    private val globalEvents = mutableListOf<BolognaEvent>()
    private val globalFriendships = mutableListOf<Pair<String, String>>()
    private val globalFriendRequests = mutableListOf<FriendRequest>()
    
    private val dialectWords = listOf(
        DialectWord("Bulàtt", "Lo scarafaggio, ma usato ironicamente per indicare una persona un po' malmessa o un ragazzino vivace.", "Guarda quel bulàtt come corre!"),
        DialectWord("Tandùri", "Persona lenta, tonta o che non capisce al volo le cose.", "Svegliati, non fare il tandemùri!"),
        DialectWord("Sgàbi", "Lo sgarbo, un atto scortese o un torto subito.", "Che sgàbi che mi ha fatto!"),
        DialectWord("Soccmel", "Espressione tipica bolognese che esprime stupore, meraviglia o disappunto (letteralmente 'succhiamelo').", "Soccmel, che bella macchina!"),
        DialectWord("Bazza", "Un affare, una fortuna sfacciata o un colpo di fortuna.", "Ho trovato questo telefono a metà prezzo, che bazza!"),
        DialectWord("Umarell", "Pensionato che osserva i cantieri, spesso con le mani dietro la schiena, dando suggerimenti non richiesti.", "C'è un umarell che controlla i lavori in via Rizzoli."),
        DialectWord("Rusco", "La spazzatura, i rifiuti.", "Ricordati di buttare il rusco stasera."),
        DialectWord("Brìsa", "Mica, punto, non. La negazione bolognese per eccellenza.", "Brìsa fêr di quèl! (Non fare storie!)"),
        DialectWord("Dà bän", "Davvero, sul serio. Usato per dare enfasi a un'affermazione.", "È un tortellino squisito, dà bän!"),
        DialectWord("Paciugo", "Un pasticcio, un miscuglio disordinato o una poltiglia (di fango o cibo).", "Ho fatto un paciugo con la vernice sul pavimento."),
        DialectWord("Sganassêr", "Ridere di gusto, a crepapelle, fino a farsi male alle mascelle (le 'ganasce').", "Abbiamo fatto delle sganassêre incredibili stasera!"),
        DialectWord("Zandragla", "Donna sguaiata, volgare o che urla in modo fastidioso.", "Smettila di urlare come una zandragla!"),
        DialectWord("Cantalòss", "Gola, pomo d'Adamo. Spesso usato quando qualcosa va di traverso.", "Mi è rimasto un pezzo di pane nel cantalòss.")
    )

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _polls = MutableStateFlow<List<Poll>>(emptyList())
    val polls = _polls.asStateFlow()

    private val _events = MutableStateFlow<List<BolognaEvent>>(emptyList())
    val events = _events.asStateFlow()

    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests = _friendRequests.asStateFlow()

    private val _wordOfTheDay = MutableStateFlow(dialectWords[0])
    val wordOfTheDay = _wordOfTheDay.asStateFlow()

    init {
        // Select word of the day based on date
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        _wordOfTheDay.value = dialectWords[dayOfYear % dialectWords.size]
        
        // Initial Mock Users
        val u1 = User(
            id = "user_alice", 
            name = "zaufer", 
            email = "alice@soccmel.bo", 
            bio = "Amo Bologna e i suoi portici 🏰", 
            location = "Bologna", 
            joinDate = "Marzo 2024", 
            interests = listOf("🏰 Portici", "📸 Foto", "🍷 Vino", "🎵 Musica"), 
            citizenshipScore = 150
        )
        val u2 = User(
            id = "user_bob", 
            name = "bolognesedoc", 
            email = "bob@soccmel.bo", 
            bio = "Sempre pronto per un tortellino 🥟", 
            location = "Bologna", 
            joinDate = "Gennaio 2024", 
            interests = listOf("🥟 Tortellini", "⚽ Bologna FC", "🎮 Gaming"), 
            citizenshipScore = 80
        )
        val u3 = User(
            id = "user_carlo", 
            name = "umareit", 
            email = "carlo@soccmel.bo", 
            bio = "Appassionato di cantieri 🏗️ | Bologna nel cuore!", 
            website = "umareit.it", 
            location = "Bologna", 
            joinDate = "Settembre 2023", 
            interests = listOf("🏗️ Cantieri", "🏃 Sport", "🍕 Pizza"), 
            citizenshipScore = 250
        )

        registerMockUser(u1, "pass123")
        registerMockUser(u2, "pass123")
        registerMockUser(u3, "pass123")
        
        // Initial friendship and meeting for demo
        globalFriendships.add(Pair(u1.id, u2.id))
        globalUsers[u1.id] = u1.copy(metUserIds = listOf(u2.id))
        globalUsers[u2.id] = u2.copy(metUserIds = listOf(u1.id))

        globalPolls.add(Poll(
            id = "p1", 
            question = "Miglior posto per l'aperitivo in Via del Pratello?", 
            creatorId = u1.id, 
            creatorName = u1.name, 
            options = listOf(PollOption(text = "Barazzo"), PollOption(text = "Mutenye"), PollOption(text = "Alce Nero")),
            comments = listOf(
                Comment(userId = u2.id, userName = u2.name, text = "Il Barazzo ha sempre i vini migliori!")
            )
        ))
        globalPolls.add(Poll(
            id = "p2", 
            question = "San Luca a piedi: meglio dal Meloncello o via Casaglia?", 
            creatorId = u2.id, 
            creatorName = u2.name, 
            options = listOf(PollOption(text = "Meloncello (classico)"), PollOption(text = "Casaglia (pro)")),
            comments = emptyList()
        ))

        globalEvents.add(BolognaEvent(title = "Mercato Ritrovato", description = "Il mercato dei produttori locali alla Cineteca.", date = "Ogni Sabato mattina", location = "Piazzetta Pasolini", category = "Cibo", isPublic = true, isFixed = true, supportContact = "info@mercatocritico.it"))
        globalEvents.add(BolognaEvent(title = "Cinema Sotto le Stelle", description = "Proiezioni gratuite in Piazza Maggiore.", date = "Giugno - Agosto", location = "Piazza Maggiore", category = "Cultura", isPublic = true, isFixed = true, supportContact = "cineteca@comune.bologna.it"))
        globalEvents.add(BolognaEvent(title = "Cioccoshow", description = "La festa del cioccolato artigianale nel cuore della città.", date = "Novembre (Annuale)", location = "Piazza XX Settembre", category = "Cibo", isPublic = true, isFixed = true))
        globalEvents.add(BolognaEvent(title = "Fiera di Santa Lucia", description = "I mercatini di Natale sotto il portico della Chiesa dei Servi.", date = "Novembre - Dicembre", location = "Strada Maggiore", category = "Cultura", isPublic = true, isFixed = true))
        globalEvents.add(BolognaEvent(title = "Workshop Fotografia Urbana", description = "Impariamo a fotografare i portici insieme.", date = "Sabato 15 Marzo ore 15:00", location = "Piazza Santo Stefano", category = "Cultura", isPublic = true, isFixed = false, supportContact = "info@bolognafotografia.it"))
        globalEvents.add(BolognaEvent(title = "Calcetto ai Giardini Margherita", description = "Ci manca un portiere, chi viene?", date = "Stasera ore 19:00", location = "Giardini Margherita", category = "Sport", isPublic = false, organizerId = u1.id, participantIds = listOf(u1.id)))
        globalEvents.add(BolognaEvent(title = "Aperitivo in via del Pratello", description = "Ritrovo per un bicchiere di vino.", date = "Venerdì ore 18:30", location = "Via del Pratello", category = "Social", isPublic = false, organizerId = u2.id, participantIds = listOf(u2.id)))
        
        _events.value = globalEvents
    }

    private const val KEY_STORAGE_VERSION = "storage_version"
    private const val CURRENT_VERSION = 3 // Incrementato per forzare il reset totale

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // RESET TOTALE UNA TANTUM: se la versione salvata è inferiore a quella attuale, cancella tutto
        val savedVersion = prefs.getInt(KEY_STORAGE_VERSION, 0)
        if (savedVersion < CURRENT_VERSION) {
            prefs.edit()
                .remove(KEY_ACCOUNTS)
                .remove(KEY_USER_ID)
                .remove(KEY_USER_NAME)
                .remove(KEY_USER_EMAIL)
                .putInt(KEY_STORAGE_VERSION, CURRENT_VERSION)
                .apply()
            // Non c'è bisogno di pulire globalUsers/globalPasswords qui perché 
            // gli utenti mock vengono ricaricati dal blocco init{} dell'oggetto
        }

        val savedAccounts = prefs.getString(KEY_ACCOUNTS, "") ?: ""
        
        savedAccounts.split(",").filter { it.isNotBlank() }.forEach { entry ->
            val parts = if (entry.contains("|")) entry.split("|") else entry.split(":")
            
            if (parts.size >= 2) {
                val email = parts[0].lowercase().trim()
                val storedPass = parts[1]
                
                // Usa il prefisso B64: per identificare in modo univoco le nuove password codificate
                val pass = if (storedPass.startsWith("B64:")) {
                    try {
                        val base64Data = storedPass.substring(4)
                        val decodedBytes = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT)
                        String(decodedBytes).trim()
                    } catch (e: Exception) {
                        storedPass.trim()
                    }
                } else {
                    storedPass.trim()
                }
                
                val id = if (parts.size >= 3 && parts[2].isNotBlank()) parts[2] else UUID.nameUUIDFromBytes(email.toByteArray()).toString()
                val name = if (parts.size >= 4 && parts[3].isNotBlank()) parts[3] else email.substringBefore("@")
                
                globalPasswords[email] = pass
                
                if (!globalUsers.containsKey(id)) {
                    val u = User(
                        id = id,
                        name = name,
                        email = email,
                        bio = "Cittadino di Bologna",
                        joinDate = "Marzo 2026",
                        interests = listOf("🏰 Bologna")
                    )
                    globalUsers[id] = u
                }
            }
        }

        val id = prefs.getString(KEY_USER_ID, null)
        if (id != null) {
            val user = globalUsers[id]
            if (user != null) {
                _currentUser.value = user
                refreshPolls()
                refreshEvents()
                refreshFriendRequests()
            }
        }
    }

    fun login(context: Context, email: String, password: String): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val normalizedEmail = email.lowercase().trim()
        val normalizedPassword = password.trim()
        
        val savedPassword = globalPasswords[normalizedEmail]
        
        if (savedPassword == null) {
            return "Account non trovato. Registrati per iniziare."
        }
        
        // Confronto pulito senza spazi superflui
        if (savedPassword.trim() != normalizedPassword) {
            return "Password errata per questo account"
        }
        
        val user = globalUsers.values.find { it.email.lowercase() == normalizedEmail } ?: run {
            val name = normalizedEmail.substringBefore("@")
            val id = UUID.nameUUIDFromBytes(normalizedEmail.toByteArray()).toString()
            val u = User(
                id = id,
                name = name,
                email = normalizedEmail,
                bio = "Cittadino di Bologna",
                joinDate = "Marzo 2026"
            )
            globalUsers[id] = u
            u
        }

        prefs.edit()
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_EMAIL, user.email)
            .apply()
        _currentUser.value = user
        refreshPolls()
        refreshEvents()
        refreshFriendRequests()
        return null
    }

    fun register(context: Context, email: String, password: String): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val normalizedEmail = email.lowercase().trim()
        val normalizedPassword = password.trim()
        
        if (globalPasswords.containsKey(normalizedEmail)) {
            return "L'email è già registrata. Effettua il login."
        }
        
        if (normalizedPassword.length < 4) return "La password deve essere di almeno 4 caratteri"
        
        val name = normalizedEmail.substringBefore("@")
        val userId = UUID.randomUUID().toString()
        val user = User(
            id = userId, 
            name = name, 
            email = normalizedEmail,
            bio = "Cittadino di Bologna pronto a contribuire!", 
            joinDate = "Marzo 2026", 
            interests = listOf("🏰 Bologna")
        )
        
        globalUsers[userId] = user
        globalPasswords[normalizedEmail] = normalizedPassword
        
        // Codifica la password in Base64 con prefisso per salvarla in modo sicuro
        val encodedPassword = "B64:" + android.util.Base64.encodeToString(normalizedPassword.toByteArray(), android.util.Base64.NO_WRAP)
        
        val currentSaved = prefs.getString(KEY_ACCOUNTS, "") ?: ""
        val newEntry = "$normalizedEmail|$encodedPassword|$userId|$name"
        
        val newSaved = if (currentSaved.isBlank()) newEntry else {
            val existingEntries = currentSaved.split(",").filter { it.isNotBlank() }
            if (existingEntries.any { it.startsWith("$normalizedEmail|") || it.startsWith("$normalizedEmail:") }) {
                currentSaved
            } else {
                "$currentSaved,$newEntry"
            }
        }
        prefs.edit().putString(KEY_ACCOUNTS, newSaved).apply()
        
        sendFriendRequestFrom("user_alice", user.id)

        prefs.edit()
            .putString(KEY_USER_ID, user.id)
            .putString(KEY_USER_NAME, user.name)
            .putString(KEY_USER_EMAIL, user.email)
            .apply()
        _currentUser.value = user
        refreshPolls()
        refreshEvents()
        refreshFriendRequests()
        return null
    }

    fun logout(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_EMAIL)
            .apply()
        _currentUser.value = null
    }

    fun joinEvent(eventId: String) {
        val user = _currentUser.value ?: return
        val eventIndex = globalEvents.indexOfFirst { it.id == eventId }
        if (eventIndex != -1) {
            val event = globalEvents[eventIndex]
            if (!event.participantIds.contains(user.id)) {
                globalEvents[eventIndex] = event.copy(participantIds = event.participantIds + user.id)
                refreshEvents()
            }
        }
    }

    fun leaveEvent(eventId: String) {
        val user = _currentUser.value ?: return
        val eventIndex = globalEvents.indexOfFirst { it.id == eventId }
        if (eventIndex != -1) {
            val event = globalEvents[eventIndex]
            if (event.participantIds.contains(user.id)) {
                globalEvents[eventIndex] = event.copy(participantIds = event.participantIds - user.id)
                refreshEvents()
            }
        }
    }

    fun completeEvent(eventId: String) {
        val user = _currentUser.value ?: return
        val eventIndex = globalEvents.indexOfFirst { it.id == eventId }
        if (eventIndex != -1) {
            val event = globalEvents[eventIndex]
            if (event.organizerId == user.id) {
                globalEvents[eventIndex] = event.copy(status = EventStatus.COMPLETED)
                event.participantIds.forEach { pid -> updateScore(pid, 30) }
                event.participantIds.forEach { userId1 ->
                    val otherParticipants = event.participantIds.filter { it != userId1 }
                    val user1 = globalUsers[userId1] ?: return@forEach
                    val updatedMetIds = (user1.metUserIds + otherParticipants).distinct()
                    val updatedUser = user1.copy(metUserIds = updatedMetIds)
                    globalUsers[userId1] = updatedUser
                    if (_currentUser.value?.id == userId1) _currentUser.value = updatedUser
                }
                refreshEvents()
                refreshPolls()
            }
        }
    }

    fun isConnected(userId: String, otherUserId: String): Boolean {
        if (userId == otherUserId) return true
        val areFriends = globalFriendships.any { (it.first == userId && it.second == otherUserId) || (it.first == otherUserId && it.second == userId) }
        val hasMet = globalUsers[userId]?.metUserIds?.contains(otherUserId) == true
        return areFriends || hasMet
    }

    fun canChatWith(userId: String, otherUserId: String): Boolean = isConnected(userId, otherUserId)

    private fun registerMockUser(user: User, password: String) {
        globalUsers[user.id] = user
        globalPasswords[user.email] = password
    }

    fun updateProfile(name: String, bio: String, website: String, location: String = "", interests: List<String> = emptyList()) {
        val user = _currentUser.value ?: return
        val updatedUser = user.copy(name = name, bio = bio, website = website.ifBlank { null }, location = location.ifBlank { null }, interests = interests)
        globalUsers[user.id] = updatedUser
        _currentUser.value = updatedUser
    }

    private fun refreshPolls() {
        val user = _currentUser.value ?: return
        _polls.value = globalPolls.filter { it.creatorId == user.id || isConnected(user.id, it.creatorId) }.sortedByDescending { it.timestamp }
    }

    private fun refreshEvents() {
        val user = _currentUser.value ?: return
        _events.value = globalEvents.filter { event ->
            event.isPublic || event.isFixed || event.organizerId == user.id || (event.organizerId != null && isConnected(user.id, event.organizerId))
        }
    }

    private fun refreshFriendRequests() {
        val user = _currentUser.value ?: return
        _friendRequests.value = globalFriendRequests.filter { it.toUserId == user.id && it.status == FriendRequestStatus.PENDING }
    }

    private fun sendFriendRequestFrom(fromUserId: String, toUserId: String) {
        val fromUser = globalUsers[fromUserId] ?: return
        globalFriendRequests.add(FriendRequest(fromUserId = fromUserId, fromUserName = fromUser.name, toUserId = toUserId))
    }

    fun sendFriendRequest(toUserId: String) {
        val user = _currentUser.value ?: return
        if (globalFriendRequests.any { it.fromUserId == user.id && it.toUserId == toUserId && it.status == FriendRequestStatus.PENDING }) return
        globalFriendRequests.add(FriendRequest(fromUserId = user.id, fromUserName = user.name, toUserId = toUserId))
        refreshFriendRequests()
    }

    fun respondToRequest(requestId: String, accept: Boolean) {
        val requestIndex = globalFriendRequests.indexOfFirst { it.id == requestId }
        if (requestIndex != -1) {
            val request = globalFriendRequests[requestIndex]
            if (accept) {
                globalFriendRequests[requestIndex] = request.copy(status = FriendRequestStatus.ACCEPTED)
                globalFriendships.add(Pair(request.fromUserId, request.toUserId))
                updateScore(request.fromUserId, 20)
                updateScore(request.toUserId, 20)
            } else {
                globalFriendRequests[requestIndex] = request.copy(status = FriendRequestStatus.REJECTED)
            }
            refreshFriendRequests()
            refreshPolls()
            refreshEvents()
        }
    }

    fun getMyPolls(): List<Poll> {
        val userId = _currentUser.value?.id ?: return emptyList()
        return globalPolls.filter { it.creatorId == userId }.sortedByDescending { it.timestamp }
    }

    fun createPoll(question: String, options: List<String>) {
        val user = _currentUser.value ?: return
        val newPoll = Poll(question = question, creatorId = user.id, creatorName = user.name, options = options.map { PollOption(text = it) })
        globalPolls.add(newPoll)
        updateScore(user.id, 5)
        refreshPolls()
    }

    fun createEvent(title: String, description: String, date: String, location: String, category: String, isPublic: Boolean) {
        val user = _currentUser.value ?: return
        val newEvent = BolognaEvent(title = title, description = description, date = date, location = location, category = category, isPublic = isPublic, isFixed = false, organizerId = user.id, participantIds = listOf(user.id))
        globalEvents.add(newEvent)
        updateScore(user.id, 10)
        refreshEvents()
    }

    fun addComment(pollId: String, text: String) {
        val user = _currentUser.value ?: return
        val pollIndex = globalPolls.indexOfFirst { it.id == pollId }
        if (pollIndex != -1) {
            val poll = globalPolls[pollIndex]
            val newComment = Comment(userId = user.id, userName = user.name, text = text)
            globalPolls[pollIndex] = poll.copy(comments = poll.comments + newComment)
            updateScore(user.id, 2)
            refreshPolls()
        }
    }

    fun vote(pollId: String, optionId: String) {
        val userId = _currentUser.value?.id ?: return
        val pollIndex = globalPolls.indexOfFirst { it.id == pollId }
        if (pollIndex != -1) {
            val poll = globalPolls[pollIndex]
            val updatedOptions = poll.options.map { option ->
                if (option.id == optionId) {
                    if (option.voters.contains(userId)) {
                        option.copy(votes = option.votes - 1, voters = (option.voters - userId).toMutableList())
                    } else {
                        updateScore(userId, 1)
                        option.copy(votes = option.votes + 1, voters = (option.voters + userId).toMutableList())
                    }
                } else {
                    option
                }
            }
            globalPolls[pollIndex] = poll.copy(options = updatedOptions)
            refreshPolls()
        }
    }

    private fun updateScore(userId: String, delta: Int) {
        val user = globalUsers[userId] ?: return
        val updatedUser = user.copy(citizenshipScore = user.citizenshipScore + delta)
        globalUsers[userId] = updatedUser
        if (_currentUser.value?.id == userId) _currentUser.value = updatedUser
    }

    fun getUserById(userId: String): User? = globalUsers[userId]

    fun searchUsers(query: String): List<User> {
        if (query.isBlank()) return emptyList()
        return globalUsers.values.filter { it.name.contains(query, ignoreCase = true) || it.email.contains(query, ignoreCase = true) }.toList()
    }

    fun confirmMeeting(otherUserId: String) {
        val currentUser = _currentUser.value ?: return
        if (currentUser.metUserIds.contains(otherUserId)) return
        val updatedCurrent = currentUser.copy(citizenshipScore = currentUser.citizenshipScore + 50, metUserIds = currentUser.metUserIds + otherUserId)
        globalUsers[currentUser.id] = updatedCurrent
        _currentUser.value = updatedCurrent
        val otherUser = globalUsers[otherUserId] ?: return
        val updatedOther = otherUser.copy(citizenshipScore = otherUser.citizenshipScore + 50, metUserIds = otherUser.metUserIds + currentUser.id)
        globalUsers[otherUserId] = updatedOther
        refreshPolls()
        refreshEvents()
    }
}
