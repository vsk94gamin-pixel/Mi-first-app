package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

object TakaFlowRepository {

    private fun getCurrentTimeString(): String {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        return sdf.format(Date())
    }

    // Default Seed User
    private val defaultUser = User(
        id = "usr_99182",
        name = "তানভীর আহমেদ (Tanvir)",
        phoneOrEmail = "01712345678",
        password = "password123",
        points = 6850, // ৳68.50
        totalWithdrawnTaka = 250,
        referralCode = "TKF-88420",
        referralCount = 7,
        dailyStreak = 4,
        lastDailyClaimDate = null,
        spinsAvailableToday = 5,
        quizzesAvailableToday = 10,
        videosAvailableToday = 8,
        scratchCardsAvailableToday = 5,
        isAdmin = false
    )

    private val _currentUser = MutableStateFlow<User?>(defaultUser)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    // Preloaded withdrawal requests (Live status testing: Pending, Processing, Approved, Rejected)
    private val initialWithdrawals = listOf(
        WithdrawalRequest(
            id = "WD-1082",
            userId = "usr_99182",
            userName = "তানভীর আহমেদ",
            userPhone = "01712345678",
            method = WithdrawalMethod.BKASH,
            pointsDeducted = 5000,
            amountTaka = 50,
            targetNumber = "01712345678",
            accountType = "Personal (পার্সোনাল)",
            status = WithdrawalStatus.APPROVED,
            requestedAt = "২৭ আগস্ট, ০৪:১৫ PM",
            processedAt = "২৭ আগস্ট, ০৫:৩০ PM",
            trxId = "BLX8921M89Q",
            adminNote = "বিকাশ পার্সোনাল নম্বরে ৫০ টাকা সফলভাবে পাঠানো হয়েছে।"
        ),
        WithdrawalRequest(
            id = "WD-1083",
            userId = "usr_99182",
            userName = "তানভীর আহমেদ",
            userPhone = "01712345678",
            method = WithdrawalMethod.NAGAD,
            pointsDeducted = 10000,
            amountTaka = 100,
            targetNumber = "01887654321",
            accountType = "Personal (পার্সোনাল)",
            status = WithdrawalStatus.APPROVED,
            requestedAt = "২৮ আগস্ট, ১১:২০ AM",
            processedAt = "২৮ আগস্ট, ০১:১০ PM",
            trxId = "NGD764420PQ",
            adminNote = "নগদ একাউন্টে ১০০ টাকা সেন্ট।"
        ),
        WithdrawalRequest(
            id = "WD-1084",
            userId = "usr_99182",
            userName = "তানভীর আহমেদ",
            userPhone = "01712345678",
            method = WithdrawalMethod.ROCKET,
            pointsDeducted = 10000,
            amountTaka = 100,
            targetNumber = "019123456789",
            accountType = "Personal (পার্সোনাল)",
            status = WithdrawalStatus.PROCESSING,
            requestedAt = "আজ, ০৯:৪৫ AM",
            processedAt = null,
            trxId = null,
            adminNote = "পেমেন্ট প্রসেসিং চলছে।"
        ),
        WithdrawalRequest(
            id = "WD-1085",
            userId = "usr_55214",
            userName = "সাকিব চৌধুরী",
            userPhone = "01600112233",
            method = WithdrawalMethod.BKASH,
            pointsDeducted = 5000,
            amountTaka = 50,
            targetNumber = "01600112233",
            accountType = "Personal (পার্সোনাল)",
            status = WithdrawalStatus.PENDING,
            requestedAt = "আজ, ১০:১৫ AM",
            processedAt = null,
            trxId = null,
            adminNote = null
        )
    )

    private val _withdrawalRequests = MutableStateFlow<List<WithdrawalRequest>>(initialWithdrawals)
    val withdrawalRequests: StateFlow<List<WithdrawalRequest>> = _withdrawalRequests.asStateFlow()

    // Task history
    private val initialTasks = listOf(
        TaskHistoryItem("t_1", "ডেইলি বোনাস ক্লেইম", 50, "গতকাল", TaskType.DAILY_BONUS),
        TaskHistoryItem("t_2", "লাকি স্পিন হুইল জয়", 100, "গতকাল", TaskType.SPIN_WHEEL),
        TaskHistoryItem("t_3", "সফল বন্ধু রেফারেল (রাকিব)", 500, "২৮ আগস্ট", TaskType.REFERRAL),
        TaskHistoryItem("t_4", "ভিডিও ওয়াচ রিওয়ার্ড", 30, "২৭ আগস্ট", TaskType.WATCH_VIDEO),
        TaskHistoryItem("t_5", "ম্যাথ কুইজ বুস্ট", 20, "২৭ আগস্ট", TaskType.QUIZ)
    )

    private val _taskHistory = MutableStateFlow<List<TaskHistoryItem>>(initialTasks)
    val taskHistory: StateFlow<List<TaskHistoryItem>> = _taskHistory.asStateFlow()

    // AI Chat history
    private val initialAiMessages = listOf(
        ChatMessage(
            id = "msg_welcome",
            isFromUser = false,
            message = "👋 আসসালামু আলাইকুম! আমি টাকাফ্লো স্মার্ট এআই সাপোর্ট বট। অ্যাপের কাজের নিয়ম, পেমেন্ট ও উইথড্রল সম্পর্কে কোনো প্রশ্ন থাকলে আমাকে জিজ্ঞেস করুন।",
            timestamp = "এখন",
            quickActionSuggestions = listOf(
                "পেমেন্ট পাওয়ার নিয়ম ও স্ট্যাটাস কী?",
                "কীভাবে দ্রুত রেফার করে বেশি পয়েন্ট আয় করবেন?",
                "কাজের নিয়ম ও একাউন্ট খোলার নির্দেশিকা।"
            )
        )
    )

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(initialAiMessages)
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    // Top Earners Leaderboard
    val topEarners = listOf(
        TopEarner(1, "মোঃ নাঈমুল ইসলাম", 1850, 42, 0xFF06B6D4),
        TopEarner(2, "নুসরাত জাহান মীম", 1420, 31, 0xFFEC4899),
        TopEarner(3, "আরিফুল হাসান", 1290, 28, 0xFF10B981),
        TopEarner(4, "তানভীর আহমেদ (আপনি)", 250, 7, 0xFF8B5CF6),
        TopEarner(5, "মেহেদী হাসান শুভ", 980, 19, 0xFFF59E0B)
    )

    // Quiz Questions in Bengali
    val quizQuestions = listOf(
        QuizQuestion(
            id = 1,
            questionBn = "বাংলাদেশের জাতীয় মুদ্রার নাম কী?",
            options = listOf("রুপি", "টাকা", "ডলার", "দিনার"),
            correctIndex = 1,
            explanationBn = "বাংলাদেশের জাতীয় মুদ্রা হলো 'বাংলাদেশি টাকা (BDT)'।"
        ),
        QuizQuestion(
            id = 2,
            questionBn = "১০ + ২৫ × ২ এর মান কত?",
            options = listOf("৭০", "৬০", "৫০", "৩৫"),
            correctIndex = 1,
            explanationBn = "বডমাস নিয়ম অনুযায়ী: ২৫ × ২ = ৫০; তারপর ১০ + ৫০ = ৬০।"
        ),
        QuizQuestion(
            id = 3,
            questionBn = "বিকাশ ও নগদে সর্বনিম্ন কত টাকা উইথড্র করা যায়?",
            options = listOf("২০ টাকা", "৫০ টাকা", "১০০ টাকা", "২০০ টাকা"),
            correctIndex = 1,
            explanationBn = "টাকাফ্লো-তে বিকাশ ও নগদে সর্বনিম্ন উইথড্র ৫০ টাকা (৫,০০০ পয়েন্ট)।"
        ),
        QuizQuestion(
            id = 4,
            questionBn = "১০০০ টাকাফ্লো পয়েন্ট সমান কত টাকা?",
            options = listOf("৫ টাকা", "১০ টাকা", "১৫ টাকা", "২০ টাকা"),
            correctIndex = 1,
            explanationBn = "টাকাফ্লো-তে প্রতি ১০০০ পয়েন্ট = ১০ টাকা (১ পয়েন্ট = ০.০১ টাকা)।"
        ),
        QuizQuestion(
            id = 5,
            questionBn = "প্রতিটি সফল রেফারে কত পয়েন্ট বোনাস পাওয়া যায়?",
            options = listOf("১০০ পয়েন্ট", "২৫০ পয়েন্ট", "৫০০ পয়েন্ট", "১০০০ পয়েন্ট"),
            correctIndex = 2,
            explanationBn = "টাকাফ্লো-তে প্রতিটি সফল রেফারে আপনি পাবেন ৫০০ পয়েন্ট (৳৫)!"
        ),
        QuizQuestion(
            id = 6,
            questionBn = "পদ্মা সেতুর মোট দৈর্ঘ্য কত কিলোমিটার?",
            options = listOf("৫.১৫ কিমি", "৬.১৫ কিমি", "৭.১৫ কিমি", "৮.০ কিমি"),
            correctIndex = 1,
            explanationBn = "পদ্মা সেতুর দৈর্ঘ্য ৬.১৫ কিলোমিটার।"
        )
    )

    // Announcements
    val announcements = listOf(
        Announcement("a1", "🎉 মেগা বোনাস অফার", "আজ বিকাশ ও নগদে ৫০ টাকা হলেই ইনস্ট্যান্ট উইথড্র রিকোয়েস্ট দিতে পারবেন!", "আজ", true),
        Announcement("a2", "👥 রেফারেল কন্টেস্ট", "এই সপ্তাহে সর্বোচ্চ রেফারকারী পাবেন অতিরিক্ত ১,০০০ টাকা স্পেশাল গিফট!", "গতকাল", false)
    )

    // ==========================================
    // AUTHENTICATION & PROFILE ACTIONS
    // ==========================================

    fun login(phoneOrEmail: String, pass: String): Boolean {
        val current = _currentUser.value
        if (current != null && current.phoneOrEmail == phoneOrEmail && current.password == pass) {
            return true
        }
        // Fallback demo login
        _currentUser.value = User(
            id = "usr_" + (10000..99999).random(),
            name = if (phoneOrEmail.contains("@")) phoneOrEmail.substringBefore("@") else "ইউজার (${phoneOrEmail.takeLast(4)})",
            phoneOrEmail = phoneOrEmail,
            password = pass,
            points = 2500,
            totalWithdrawnTaka = 0,
            referralCode = "TKF-" + (10000..99999).random(),
            referralCount = 0
        )
        return true
    }

    fun signUp(name: String, phoneOrEmail: String, pass: String, refCode: String?): Boolean {
        val welcomeBonus = if (!refCode.isNullOrBlank()) 700 else 500 // extra 200 pts for referral code
        val newUser = User(
            id = "usr_" + (10000..99999).random(),
            name = name,
            phoneOrEmail = phoneOrEmail,
            password = pass,
            points = welcomeBonus,
            totalWithdrawnTaka = 0,
            referralCode = "TKF-" + (10000..99999).random(),
            referredBy = refCode?.takeIf { it.isNotBlank() },
            referralCount = 0,
            dailyStreak = 1
        )
        _currentUser.value = newUser

        val taskDesc = if (!refCode.isNullOrBlank()) "ওয়েলকাম + রেফার কোড বোনাস" else "নতুন সাইন আপ ওয়েলকাম বোনাস"
        addHistoryItem(taskDesc, welcomeBonus, TaskType.DAILY_BONUS)
        return true
    }

    fun logout() {
        _currentUser.value = null
    }

    fun toggleAdminMode() {
        _isAdminMode.value = !_isAdminMode.value
    }

    // ==========================================
    // TASK & EARNING ACTIONS
    // ==========================================

    fun claimDailyBonus(): Int {
        val user = _currentUser.value ?: return 0
        val bonus = 50 + (user.dailyStreak * 10)
        _currentUser.value = user.copy(
            points = user.points + bonus,
            dailyStreak = (user.dailyStreak + 1),
            lastDailyClaimDate = getCurrentTimeString()
        )
        addHistoryItem("ডেইলি বোনাস (দিন ${user.dailyStreak + 1})", bonus, TaskType.DAILY_BONUS)
        return bonus
    }

    fun spinWheelClaim(wonPoints: Int): Boolean {
        val user = _currentUser.value ?: return false
        if (user.spinsAvailableToday <= 0) return false

        _currentUser.value = user.copy(
            points = user.points + wonPoints,
            spinsAvailableToday = user.spinsAvailableToday - 1
        )
        addHistoryItem("লাকি স্পিন হুইল রিওয়ার্ড", wonPoints, TaskType.SPIN_WHEEL)
        return true
    }

    fun watchVideoClaim(pointsEarned: Int = 30): Boolean {
        val user = _currentUser.value ?: return false
        if (user.videosAvailableToday <= 0) return false

        _currentUser.value = user.copy(
            points = user.points + pointsEarned,
            videosAvailableToday = user.videosAvailableToday - 1
        )
        addHistoryItem("ভিডিও ওয়াচ টাস্ক কমপ্লিট", pointsEarned, TaskType.WATCH_VIDEO)
        return true
    }

    fun quizAnswerClaim(pointsEarned: Int = 20): Boolean {
        val user = _currentUser.value ?: return false
        if (user.quizzesAvailableToday <= 0) return false

        _currentUser.value = user.copy(
            points = user.points + pointsEarned,
            quizzesAvailableToday = user.quizzesAvailableToday - 1
        )
        addHistoryItem("কুইজ সঠিক উত্তর বোনাস", pointsEarned, TaskType.QUIZ)
        return true
    }

    fun scratchCardClaim(pointsEarned: Int): Boolean {
        val user = _currentUser.value ?: return false
        if (user.scratchCardsAvailableToday <= 0) return false

        _currentUser.value = user.copy(
            points = user.points + pointsEarned,
            scratchCardsAvailableToday = user.scratchCardsAvailableToday - 1
        )
        addHistoryItem("গোল্ডেন স্ক্র্যাচ কার্ড জয়", pointsEarned, TaskType.SCRATCH_CARD)
        return true
    }

    fun simulateReferralBonus(): Int {
        val user = _currentUser.value ?: return 0
        val bonus = 500
        _currentUser.value = user.copy(
            points = user.points + bonus,
            referralCount = user.referralCount + 1
        )
        addHistoryItem("সফল বন্ধু রেফারেল বোনাস (+১ বন্ধু)", bonus, TaskType.REFERRAL)
        return bonus
    }

    private fun addHistoryItem(title: String, points: Int, type: TaskType) {
        val newItem = TaskHistoryItem(
            id = "t_" + System.currentTimeMillis(),
            title = title,
            points = points,
            timestamp = "আজ, " + SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            taskType = type
        )
        _taskHistory.value = listOf(newItem) + _taskHistory.value
    }

    // ==========================================
    // WITHDRAWAL ACTIONS
    // ==========================================

    fun requestWithdrawal(
        method: WithdrawalMethod,
        amountTaka: Int,
        targetNumber: String,
        accountType: String
    ): Result<WithdrawalRequest> {
        val user = _currentUser.value ?: return Result.failure(Exception("ইউজার লগইন করা নেই"))
        val pointsRequired = amountTaka * 100

        if (amountTaka < method.minTaka) {
            return Result.failure(Exception("${method.titleBn}-এ সর্বনিম্ন উইথড্র ৳${method.minTaka}"))
        }

        if (user.points < pointsRequired) {
            return Result.failure(Exception("আপনার পর্যাপ্ত পয়েন্ট নেই। প্রয়োজন: $pointsRequired পয়েন্ট (আছে: ${user.points} পয়েন্ট)"))
        }

        if (targetNumber.length < 11) {
            return Result.failure(Exception("সঠিক ১১ ডিজিটের ফোন নম্বর লিখুন"))
        }

        // Deduct points
        _currentUser.value = user.copy(points = user.points - pointsRequired)

        val newRequest = WithdrawalRequest(
            id = "WD-" + (1086..9999).random(),
            userId = user.id,
            userName = user.name,
            userPhone = user.phoneOrEmail,
            method = method,
            pointsDeducted = pointsRequired,
            amountTaka = amountTaka,
            targetNumber = targetNumber,
            accountType = accountType,
            status = WithdrawalStatus.PENDING,
            requestedAt = getCurrentTimeString(),
            adminNote = "অপেক্ষমাণ আছে (অটো ভেরিফিকেশন চলছে)"
        )

        _withdrawalRequests.value = listOf(newRequest) + _withdrawalRequests.value
        return Result.success(newRequest)
    }

    // ==========================================
    // ADMIN PANEL ACTIONS (FOR TESTING & DEMO)
    // ==========================================

    fun updateWithdrawalStatus(
        requestId: String,
        newStatus: WithdrawalStatus,
        trxId: String? = null,
        adminNote: String? = null
    ) {
        val list = _withdrawalRequests.value.toMutableList()
        val index = list.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val old = list[index]
            val processedTime = if (newStatus == WithdrawalStatus.APPROVED || newStatus == WithdrawalStatus.PROCESSING) {
                getCurrentTimeString()
            } else old.processedAt

            val generatedTrx = if (newStatus == WithdrawalStatus.APPROVED && trxId.isNullOrBlank()) {
                "TRX" + (10000000..99999999).random() + "TK"
            } else trxId

            val updated = old.copy(
                status = newStatus,
                processedAt = processedTime,
                trxId = generatedTrx ?: old.trxId,
                adminNote = adminNote ?: when (newStatus) {
                    WithdrawalStatus.APPROVED -> "পেমেন্ট সফলভাবে এপ্রুভ ও পাঠানো হয়েছে।"
                    WithdrawalStatus.PROCESSING -> "পেমেন্ট প্রসেসিং চলছে।"
                    WithdrawalStatus.PENDING -> "রিকোয়েস্ট পেন্ডিং রাখা হয়েছে।"
                    WithdrawalStatus.REJECTED -> "তথ্যগত কারণে রিজেক্ট ও পয়েন্ট রিফান্ড করা হয়েছে।"
                }
            )
            list[index] = updated
            _withdrawalRequests.value = list

            // If approved, update totalWithdrawnTaka of current user
            val current = _currentUser.value
            if (current != null && current.id == old.userId) {
                if (newStatus == WithdrawalStatus.APPROVED && old.status != WithdrawalStatus.APPROVED) {
                    _currentUser.value = current.copy(
                        totalWithdrawnTaka = current.totalWithdrawnTaka + old.amountTaka
                    )
                } else if (newStatus == WithdrawalStatus.REJECTED && old.status != WithdrawalStatus.REJECTED) {
                    // Refund points
                    _currentUser.value = current.copy(
                        points = current.points + old.pointsDeducted
                    )
                    addHistoryItem("উইথড্র রিফান্ড (${old.method.titleBn})", old.pointsDeducted, TaskType.DAILY_BONUS)
                }
            }
        }
    }

    fun deleteWithdrawal(requestId: String) {
        _withdrawalRequests.value = _withdrawalRequests.value.filter { it.id != requestId }
    }

    fun adminAdjustPoints(deltaPoints: Int) {
        val user = _currentUser.value ?: return
        val newPoints = (user.points + deltaPoints).coerceAtLeast(0)
        _currentUser.value = user.copy(points = newPoints)
    }

    // ==========================================
    // AI CHATBOT ACTIONS
    // ==========================================

    suspend fun sendAiMessage(userPrompt: String) {
        val userMsg = ChatMessage(
            id = "msg_" + System.currentTimeMillis(),
            isFromUser = true,
            message = userPrompt,
            timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        )
        _chatMessages.value = _chatMessages.value + userMsg

        // Generate smart response from Gemini or Bengali Engine
        val replyText = GeminiAiService.askTakaFlowAi(userPrompt)

        val aiMsg = ChatMessage(
            id = "msg_" + (System.currentTimeMillis() + 1),
            isFromUser = false,
            message = replyText,
            timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date()),
            quickActionSuggestions = listOf(
                "পেমেন্ট পাওয়ার নিয়ম ও স্ট্যাটাস কী?",
                "কীভাবে দ্রুত রেফার করে বেশি পয়েন্ট আয় করবেন?",
                "কাজের নিয়ম ও একাউন্ট খোলার নির্দেশিকা।"
            )
        )
        _chatMessages.value = _chatMessages.value + aiMsg
    }

    fun clearChatHistory() {
        _chatMessages.value = initialAiMessages
    }
}
