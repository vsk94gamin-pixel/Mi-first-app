package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TakaFlowRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ScreenDestination {
    object Splash : ScreenDestination()
    object Auth : ScreenDestination()
    object Main : ScreenDestination()
}

enum class MainTab(val titleBn: String, val icon: String) {
    HOME("হোম", "home"),
    TASKS("টাস্ক ও আর্ন", "tasks"),
    WALLET("ওয়ালেট", "wallet"),
    HISTORY("হিস্ট্রি", "history"),
    AI_SUPPORT("এআই সাপোর্ট", "ai_bot")
}

class TakaFlowViewModel : ViewModel() {

    private val repository = TakaFlowRepository

    val currentUser: StateFlow<User?> = repository.currentUser
    val withdrawalRequests: StateFlow<List<WithdrawalRequest>> = repository.withdrawalRequests
    val taskHistory: StateFlow<List<TaskHistoryItem>> = repository.taskHistory
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessages
    val isAdminMode: StateFlow<Boolean> = repository.isAdminMode

    private val _currentScreen = MutableStateFlow<ScreenDestination>(ScreenDestination.Splash)
    val currentScreen: StateFlow<ScreenDestination> = _currentScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(MainTab.HOME)
    val currentTab: StateFlow<MainTab> = _currentTab.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    // Dialog states for tasks
    private val _showSpinWheel = MutableStateFlow(false)
    val showSpinWheel: StateFlow<Boolean> = _showSpinWheel.asStateFlow()

    private val _showQuiz = MutableStateFlow(false)
    val showQuiz: StateFlow<Boolean> = _showQuiz.asStateFlow()

    private val _showScratchCard = MutableStateFlow(false)
    val showScratchCard: StateFlow<Boolean> = _showScratchCard.asStateFlow()

    private val _showVideoWatch = MutableStateFlow(false)
    val showVideoWatch: StateFlow<Boolean> = _showVideoWatch.asStateFlow()

    private val _showTelegramModal = MutableStateFlow(false)
    val showTelegramModal: StateFlow<Boolean> = _showTelegramModal.asStateFlow()

    private val _showAdminSheet = MutableStateFlow(false)
    val showAdminSheet: StateFlow<Boolean> = _showAdminSheet.asStateFlow()

    fun navigateTo(screen: ScreenDestination) {
        _currentScreen.value = screen
    }

    fun selectTab(tab: MainTab) {
        _currentTab.value = tab
    }

    fun showToast(message: String) {
        viewModelScope.launch {
            _toastEvent.emit(message)
        }
    }

    // Auth actions
    fun login(phoneOrEmail: String, pass: String, onSuccess: () -> Unit) {
        if (phoneOrEmail.isBlank() || pass.isBlank()) {
            showToast("অনুগ্রহ করে ফোন/ইমেইল এবং পাসওয়ার্ড দিন")
            return
        }
        val success = repository.login(phoneOrEmail, pass)
        if (success) {
            _currentScreen.value = ScreenDestination.Main
            showToast("স্বাগতম! সফলভাবে লগইন হয়েছে")
            onSuccess()
        }
    }

    fun signUp(name: String, phoneOrEmail: String, pass: String, refCode: String?, onSuccess: () -> Unit) {
        if (name.isBlank() || phoneOrEmail.isBlank() || pass.isBlank()) {
            showToast("অনুগ্রহ করে সব তথ্য সঠিকভাবে পূরণ করুন")
            return
        }
        val success = repository.signUp(name, phoneOrEmail, pass, refCode)
        if (success) {
            _currentScreen.value = ScreenDestination.Main
            val bonusMsg = if (!refCode.isNullOrBlank()) "অভিনন্দন! ৭০০ পয়েন্ট ওয়েলকাম বোনাস যোগ হয়েছে" else "অভিনন্দন! ৫০০ পয়েন্ট ওয়েলকাম বোনাস যোগ হয়েছে"
            showToast(bonusMsg)
            onSuccess()
        }
    }

    fun logout() {
        repository.logout()
        _currentScreen.value = ScreenDestination.Auth
        showToast("সফলভাবে লগআউট করা হয়েছে")
    }

    fun toggleAdminMode() {
        repository.toggleAdminMode()
        val modeText = if (repository.isAdminMode.value) "এডমিন মোড চালু হয়েছে 🛠️" else "ইউজার মোডে ফিরে যাওয়া হয়েছে 👤"
        showToast(modeText)
    }

    // Modal dialog controllers
    fun openSpinWheel() { _showSpinWheel.value = true }
    fun closeSpinWheel() { _showSpinWheel.value = false }

    fun openQuiz() { _showQuiz.value = true }
    fun closeQuiz() { _showQuiz.value = false }

    fun openScratchCard() { _showScratchCard.value = true }
    fun closeScratchCard() { _showScratchCard.value = false }

    fun openVideoWatch() { _showVideoWatch.value = true }
    fun closeVideoWatch() { _showVideoWatch.value = false }

    fun openTelegramModal() { _showTelegramModal.value = true }
    fun closeTelegramModal() { _showTelegramModal.value = false }

    fun openAdminSheet() { _showAdminSheet.value = true }
    fun closeAdminSheet() { _showAdminSheet.value = false }

    // Task claims
    fun claimDailyBonus() {
        val bonus = repository.claimDailyBonus()
        if (bonus > 0) {
            showToast("🎉 অভিনন্দন! আপনি $bonus পয়েন্ট ডেইলি বোনাস পেয়েছেন!")
        } else {
            showToast("আজকের ডেইলি বোনাস ইতিমধ্যে ক্লেইম করা হয়েছে")
        }
    }

    fun onSpinCompleted(pointsWon: Int) {
        val ok = repository.spinWheelClaim(pointsWon)
        if (ok) {
            showToast("🎡 অসাধারণ! আপনি $pointsWon পয়েন্ট জিতেছেন!")
        } else {
            showToast("আজকের ফ্রি স্পিন লিমিট শেষ!")
        }
    }

    fun onVideoCompleted() {
        val ok = repository.watchVideoClaim(30)
        if (ok) {
            showToast("🎬 ভিডিও সফলভাবে সম্পূর্ণ হয়েছে! +৩০ পয়েন্ট যোগ হয়েছে")
        } else {
            showToast("আজকের ভিডিও লিমিট শেষ!")
        }
    }

    fun onQuizCorrect() {
        val ok = repository.quizAnswerClaim(20)
        if (ok) {
            showToast("🧠 সঠিক উত্তর! +২০ পয়েন্ট যোগ হয়েছে")
        } else {
            showToast("আজকের কুইজ লিমিট শেষ!")
        }
    }

    fun onScratchCompleted(points: Int) {
        val ok = repository.scratchCardClaim(points)
        if (ok) {
            showToast("✨ চমৎকার! স্ক্র্যাচ থেকে $points পয়েন্ট পেয়েছেন!")
        } else {
            showToast("আজকের স্ক্র্যাচ কার্ড শেষ!")
        }
    }

    fun simulateReferralShare() {
        val bonus = repository.simulateReferralBonus()
        showToast("👥 ডেমো টেস্ট: ১ জন বন্ধু জয়েন করেছে! +$bonus পয়েন্ট যোগ হয়েছে!")
    }

    // Withdrawal submission
    fun submitWithdrawal(
        method: WithdrawalMethod,
        amountTaka: Int,
        targetNumber: String,
        accountType: String,
        onSuccess: () -> Unit
    ) {
        val result = repository.requestWithdrawal(method, amountTaka, targetNumber, accountType)
        result.onSuccess { req ->
            showToast("✅ উইথড্র রিকোয়েস্ট সফল! আইডি: ${req.id} (৳${req.amountTaka})")
            _currentTab.value = MainTab.HISTORY
            onSuccess()
        }.onFailure { err ->
            showToast(err.message ?: "উইথড্র রিকোয়েস্টে সমস্যা হয়েছে")
        }
    }

    // Admin controls
    fun adminUpdateStatus(requestId: String, status: WithdrawalStatus, trxId: String? = null, note: String? = null) {
        repository.updateWithdrawalStatus(requestId, status, trxId, note)
        showToast("এডমিন আপডেট: রিকোয়েস্ট ${status.titleBn} করা হয়েছে")
    }

    fun adminDeleteRequest(requestId: String) {
        repository.deleteWithdrawal(requestId)
        showToast("উইথড্র রিকোয়েস্ট ডিলিট করা হয়েছে")
    }

    fun adminAddPoints(delta: Int) {
        repository.adminAdjustPoints(delta)
        val sign = if (delta > 0) "+$delta" else "$delta"
        showToast("এডমিন ব্যালেন্স অ্যাডজাস্ট: $sign পয়েন্ট")
    }

    // AI Chatbot
    fun sendAiPrompt(prompt: String) {
        if (prompt.isBlank()) return
        viewModelScope.launch {
            _isAiLoading.value = true
            try {
                repository.sendAiMessage(prompt)
            } finally {
                _isAiLoading.value = false
            }
        }
    }

    fun clearChat() {
        repository.clearChatHistory()
        showToast("চ্যাট হিস্ট্রি ক্লিয়ার করা হয়েছে")
    }
}
