package com.example.model

enum class WithdrawalMethod(
    val titleBn: String,
    val iconName: String,
    val minPoints: Int,
    val minTaka: Int,
    val colorHex: Long
) {
    BKASH("বিকাশ (bKash)", "bkash", 5000, 50, 0xFFE2136E),
    NAGAD("নগদ (Nagad)", "nagad", 5000, 50, 0xFFF7941D),
    ROCKET("রকেট (Rocket)", "rocket", 10000, 100, 0xFF8C3494)
}

enum class WithdrawalStatus(
    val titleBn: String,
    val badgeSymbol: String,
    val colorHex: Long,
    val bgHex: Long
) {
    PENDING("পেন্ডিং", "⏳", 0xFFFBBF24, 0x28F59E0B),
    PROCESSING("প্রসেসিং", "🔄", 0xFF38BDF8, 0x2806B6D4),
    APPROVED("পেমেন্ট এপ্রুভড", "✅", 0xFF34D399, 0x2810B981),
    REJECTED("রিজেক্টেড / রিফান্ডেড", "❌", 0xFFFB7185, 0x28F43F5E)
}

data class WithdrawalRequest(
    val id: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val method: WithdrawalMethod,
    val pointsDeducted: Int,
    val amountTaka: Int,
    val targetNumber: String,
    val accountType: String = "Personal (পার্সোনাল)",
    val status: WithdrawalStatus,
    val requestedAt: String,
    val processedAt: String? = null,
    val trxId: String? = null,
    val adminNote: String? = null
)

data class User(
    val id: String,
    val name: String,
    val phoneOrEmail: String,
    val password: String,
    val points: Int = 2500,
    val totalWithdrawnTaka: Int = 150,
    val referralCode: String,
    val referredBy: String? = null,
    val referralCount: Int = 4,
    val dailyStreak: Int = 3,
    val lastDailyClaimDate: String? = null,
    val spinsAvailableToday: Int = 5,
    val quizzesAvailableToday: Int = 10,
    val videosAvailableToday: Int = 8,
    val scratchCardsAvailableToday: Int = 5,
    val isAdmin: Boolean = false,
    val joinedDate: String = "১ জুলাই ২০২৬"
) {
    val takaEquivalent: Double
        get() = points / 100.0 // 100 points = 1 Taka (1000 pts = ৳10)
}

enum class TaskType(val titleBn: String, val icon: String, val rewardInfoBn: String) {
    DAILY_BONUS("ডেইলি বোনাস", "🎁", "প্রতিদিন ৫০-১০০ পয়েন্ট"),
    SPIN_WHEEL("লাকি স্পিন হুইল", "🎡", "১০-৫০০ পয়েন্ট পর্যন্ত"),
    WATCH_VIDEO("ভিডিও ওয়াচ টাস্ক", "🎬", "প্রতি ভিডিওতে ৩০ পয়েন্ট"),
    QUIZ("ম্যাথ ও জিকে কুইজ", "🧠", "প্রতি সঠিক উত্তরে ২০ পয়েন্ট"),
    SCRATCH_CARD("গোল্ডেন স্ক্র্যাচ কার্ড", "✨", "১৫-৩০০ পয়েন্ট পর্যন্ত"),
    REFERRAL("রেফার অ্যান্ড আর্ন", "👥", "প্রতি সফল রেফারে ৫০০ পয়েন্ট")
}

data class TaskHistoryItem(
    val id: String,
    val title: String,
    val points: Int,
    val timestamp: String,
    val taskType: TaskType
)

data class ChatMessage(
    val id: String,
    val isFromUser: Boolean,
    val message: String,
    val timestamp: String,
    val quickActionSuggestions: List<String> = emptyList()
)

data class QuizQuestion(
    val id: Int,
    val questionBn: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanationBn: String
)

data class TopEarner(
    val rank: Int,
    val name: String,
    val totalEarnedTaka: Int,
    val referralCount: Int,
    val avatarBgColor: Long
)

data class Announcement(
    val id: String,
    val title: String,
    val text: String,
    val date: String,
    val isImportant: Boolean = false
)
