package com.example.data

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAiService {
    private const val TAG = "GeminiAiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val SYSTEM_INSTRUCTION = """
        তুমি হলে 'টাকাফ্লো (TakaFlow)' ডিজিটাল আর্নিং অ্যাপের অফিশিয়াল এবং বিনয়ী বাংলা স্মার্ট এআই অ্যাসিস্ট্যান্ট।
        তোমার দায়িত্ব হলো ব্যবহারকারীদের টাকাফ্লো অ্যাপের সমস্ত নিয়মাবলি, পয়েন্ট অর্জন পদ্ধতি, উইথড্রল নিয়ম, বিকাশ/নগদ/রকেট পেমেন্ট সময়সূচি এবং রেফারেল বোনাস সম্পর্কে স্পষ্ট, বন্ধুত্বপূর্ণ ও সুন্দর ভাষায় দিকনির্দেশনা দেওয়া।
        
        অ্যাপের গুরুত্বপূর্ণ নিয়মসমূহ:
        ১. পয়েন্ট রূপান্তর: ১০০০ পয়েন্ট = ১০ টাকা (১ পয়েন্ট = ০.০১ টাকা)।
        ২. উইথড্র মাধ্যম ও সর্বনিম্ন লিমিট: 
           - বিকাশ (bKash): সর্বনিম্ন ৫০ টাকা (৫,০০০ পয়েন্ট)
           - নগদ (Nagad): সর্বনিম্ন ৫০ টাকা (৫,০০০ পয়েন্ট)
           - রকেট (Rocket): সর্বনিম্ন ১০০ টাকা (১০,০০০ পয়েন্ট)
        ৩. পেমেন্ট সময়: রিকোয়েস্ট করার ১ থেকে সর্বোচ্চ ২৪ ঘণ্টার মধ্যে একাউন্টে টাকা পাঠানো হয়। শুক্র ও সরকারি ছুটির দিনে প্রসেসিং কিছুটা সময় নিতে পারে।
        ৪. রেফার বোনাস: প্রতিটি সফল রেফারে আপনি পাবেন ৫০০ পয়েন্ট (৳৫) এবং আপনার বন্ধু পাবে ২০০ পয়েন্ট ওয়েলকাম বোনাস!
        ৫. টাস্কসমূহ: ডেইলি বোনাস (৫০-১০০ পয়েন্ট), লাকি স্পিন হুইল (প্রতি স্পিনে ১০-৫০০ পয়েন্ট), ভিডিও ওয়াচ (৩০ পয়েন্ট), নলেজ ও ম্যাথ কুইজ (২০ পয়েন্ট), গোল্ডেন স্ক্র্যাচ কার্ড (১৫-৩০০ পয়েন্ট)।
        ৬. নিয়মভঙ্গ ও ব্যান: ভিপিএন বা কোনো হ্যাকিং টুল ব্যবহার করলে একাউন্ট সাময়িক বা স্থায়ীভাবে সাসপেন্ড হতে পারে।
        ৭. টেলিগ্রাম সাপোর্ট: কোনো অতিরিক্ত জিজ্ঞাসায় আমাদের অফিশিয়াল টেলিগ্রাম কমিউনিটি গ্রুপে জয়েন করতে পারেন।
        
        সর্বদা শুদ্ধ, সহজ ও সুন্দর বাংলায় বুলেট পয়েন্ট সহযোগে চমৎকারভাবে উত্তর দাও।
    """.trimIndent()

    suspend fun askTakaFlowAi(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If no valid key or placeholder key, use offline smart AI knowledge response
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext getOfflineAiResponse(prompt)
        }

        try {
            val jsonBody = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                val systemInstructionObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply { put("text", SYSTEM_INSTRUCTION) })
                    }
                    put("parts", partsArray)
                }
                put("systemInstruction", systemInstructionObj)
            }

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseString = response.body?.string() ?: ""

            if (response.isSuccessful && responseString.isNotEmpty()) {
                val responseJson = JSONObject(responseString)
                val candidates = responseJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val firstCandidate = candidates.getJSONObject(0)
                    val content = firstCandidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "")
                        if (text.isNotBlank()) {
                            return@withContext text
                        }
                    }
                }
            }
            // Fallback if API returned error or empty
            getOfflineAiResponse(prompt)
        } catch (e: Exception) {
            Log.e(TAG, "Error in Gemini API call: ${e.message}")
            getOfflineAiResponse(prompt)
        }
    }

    fun getOfflineAiResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("পেমেন্ট") || lower.contains("উইথড্র") || lower.contains("টাকা") || lower.contains("বিকাশ") || lower.contains("নগদ") || lower.contains("rocket") || lower.contains("payment") -> {
                """
                💳 **পেমেন্ট পাওয়ার নিয়ম ও সময়সূচি:**
                
                • **বিকাশ (bKash):** সর্বনিম্ন ৫০ টাকা (৫,০০০ পয়েন্ট)
                • **নগদ (Nagad):** সর্বনিম্ন ৫০ টাকা (৫,০০০ পয়েন্ট)
                • **রকেট (Rocket):** সর্বনিম্ন ১০০ টাকা (১০,০০০ পয়েন্ট)
                
                ⏱️ **পেমেন্ট আসার সময়:** 
                উইথড্র রিকোয়েস্ট সাবমিট করার পর আমাদের অটোমেটিক গেটওয়ে **১ থেকে ২৪ ঘণ্টার মধ্যে** পেমেন্ট ক্লিয়ার করে। 'হিস্ট্রি' পেইজে লাইভ স্ট্যাটাস দেখতে পাবেন:
                • ⏳ **পেন্ডিং:** রিকোয়েস্ট পর্যালোচনা চলছে
                • 🔄 **প্রসেসিং:** পেমেন্ট ব্যাংক/এমএফএস লাইনে আছে
                • ✅ **পেমেন্ট এপ্রুভড:** আপনার নম্বরে টাকা ও TrxID পাঠানো হয়েছে!
                """.trimIndent()
            }
            lower.contains("রেফার") || lower.contains("refer") || lower.contains("কোড") || lower.contains("লিঙ্ক") -> {
                """
                🚀 **কীভাবে দ্রুত রেফার করে আনলিমিটেড পয়েন্ট আয় করবেন:**
                
                ১. আপনার প্রোফাইল বা হোম স্ক্রিন থেকে ইউনিক **রেফারেল কোড ও লিঙ্ক** কপি করুন।
                ২. ফেসবুক, হোয়াটসঅ্যাপ এবং টেলিগ্রাম গ্রুপে বন্ধুদের সাথে শেয়ার করুন।
                ৩. আপনার কোড ব্যবহার করে কেউ সাইন আপ করলেই পাবেন **৫০০ পয়েন্ট (৳৫)** ইনস্ট্যান্ট বোনাস!
                ৪. আপনার বন্ধুও সাথে সাথে পাবে **২০০ পয়েন্ট** ওয়েলকাম বোনাস!
                
                💡 *টিপ:* প্রতিদিন ৫ জন বন্ধুকে ইনভাইট করলেই খুব সহজে ২৫০০ পয়েন্ট (৳২৫) আয় করতে পারবেন।
                """.trimIndent()
            }
            lower.contains("কাজ") || lower.contains("টাস্ক") || lower.contains("পয়েন্ট") || lower.contains("স্পিন") || lower.contains("কুইজ") || lower.contains("বোনাস") || lower.contains("earn") -> {
                """
                🎯 **টাকাফ্লো-তে পয়েন্ট আয়ের প্রধান মাধ্যমসমূহ:**
                
                🎁 **১. ডেইলি বোনাস:** প্রতিদিন অ্যাপে লগইন করে ফ্রি ৫০-১০০ পয়েন্ট ক্লেইম করুন।
                🎡 **২. লাকি স্পিন হুইল:** চাকা ঘুরিয়ে প্রতি স্পিনে ১০ থেকে ৫০০ পর্যন্ত পয়েন্ট জিতুন!
                🎬 **৩. ভিডিও ওয়াচ টাস্ক:** ছোট প্রোমোশনাল ভিডিও দেখে প্রতিবারে ৩০ পয়েন্ট অর্জন করুন।
                🧠 **৪. কুইজ টাস্ক:** সহজ গণিত ও সাধারণ জ্ঞান প্রশ্নের সঠিক উত্তরে ২০ পয়েন্ট।
                ✨ **৫. গোল্ডেন স্ক্র্যাচ কার্ড:** কার্ড স্ক্র্যাচ করে সারপ্রাইজ বোনাস জিতুন।
                👥 **৬. রেফারেল বোনাস:** প্রতি রেফারে ৫০০ পয়েন্ট!
                """.trimIndent()
            }
            lower.contains("একাউন্ট") || lower.contains("লগইন") || lower.contains("সাইন আপ") || lower.contains("পাসওয়ার্ড") -> {
                """
                🔐 **একাউন্ট খোলার নির্দেশিকা ও সুরক্ষা নীতি:**
                
                • একটি ডিভাইসে শুধুমাত্র একটি ভেরিফাইড একাউন্ট ব্যবহার করা যাবে।
                • সাইন আপ করার সময় আপনার আসল ফোন নম্বর/ইমেইল এবং একটি শক্তিশালী পাসওয়ার্ড দিন।
                • রেফারেল কোড থাকলে তা দিন, এতে অতিরিক্ত ২০০ পয়েন্ট ওয়েলকাম বোনাস যুক্ত হবে।
                • কোনো ধরনের VPN, Auto-Clicker বা ক্লোন অ্যাপ ব্যবহার সম্পূর্ণ নিষিদ্ধ।
                """.trimIndent()
            }
            else -> {
                """
                👋 **টাকাফ্লো স্মার্ট এআই সাপোর্টে আপনাকে স্বাগতম!**
                
                আমি আপনাকে নিম্নলিখিত বিষয়গুলোতে দ্রুত সাহায্য করতে পারি:
                • 💰 পেমেন্ট পাওয়ার নিয়ম ও উইথড্রল স্ট্যাটাস
                • 👥 রেফারেল বোনাস বৃদ্ধি করার সেরা উপায়
                • 🎡 ডেইলি বোনাস, স্পিন হুইল ও কুইজের কাজের নিয়ম
                • 🛡️ একাউন্ট নিরাপত্তা ও কমিউনিটি গাইডলাইন
                
                আপনার কোনো নির্দিষ্ট প্রশ্ন থাকলে সরাসরি নিচে লিখে সেন্ড করুন অথবা উপরের অপশনগুলো বেছে নিন।
                """.trimIndent()
            }
        }
    }
}
