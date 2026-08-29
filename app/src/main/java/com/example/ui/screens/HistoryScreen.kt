package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.WithdrawalRequest
import com.example.model.WithdrawalStatus
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.TakaFlowViewModel

@Composable
fun HistoryScreen(
    viewModel: TakaFlowViewModel,
    modifier: Modifier = Modifier
) {
    val withdrawals by viewModel.withdrawalRequests.collectAsState()
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf<WithdrawalStatus?>(null) }

    val filteredList = remember(withdrawals, selectedFilter) {
        if (selectedFilter == null) withdrawals
        else withdrawals.filter { it.status == selectedFilter }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .testTag("history_screen_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            // Header
            Column {
                Text(
                    text = "উইথড্রল হিস্ট্রি ও স্ট্যাটাস 📜",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = "আপনার সকল পেমেন্ট ট্রানজেকশন ও লাইভ স্ট্যাটাস পর্যবেক্ষণ করুন",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }
        }

        // Filter Chips Row
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                item {
                    FilterChipItem(
                        title = "সবগুলো (${withdrawals.size})",
                        isSelected = selectedFilter == null,
                        onClick = { selectedFilter = null }
                    )
                }
                item {
                    FilterChipItem(
                        title = "সফল (${withdrawals.count { it.status == WithdrawalStatus.APPROVED }})",
                        isSelected = selectedFilter == WithdrawalStatus.APPROVED,
                        onClick = { selectedFilter = WithdrawalStatus.APPROVED },
                        activeColor = NeonEmerald
                    )
                }
                item {
                    FilterChipItem(
                        title = "প্রসেসিং (${withdrawals.count { it.status == WithdrawalStatus.PROCESSING }})",
                        isSelected = selectedFilter == WithdrawalStatus.PROCESSING,
                        onClick = { selectedFilter = WithdrawalStatus.PROCESSING },
                        activeColor = NeonCyan
                    )
                }
                item {
                    FilterChipItem(
                        title = "পেন্ডিং (${withdrawals.count { it.status == WithdrawalStatus.PENDING }})",
                        isSelected = selectedFilter == WithdrawalStatus.PENDING,
                        onClick = { selectedFilter = WithdrawalStatus.PENDING },
                        activeColor = AmberGold
                    )
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "📭", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "কোনো রেকর্ড পাওয়া যায়নি",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = TextSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        } else {
            items(filteredList, key = { it.id }) { item ->
                WithdrawalHistoryCard(
                    item = item,
                    onCopyTrx = { trx ->
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("TrxID", trx)
                        clipboard.setPrimaryClip(clip)
                        viewModel.showToast("TrxID কপি করা হয়েছে: $trx 📋")
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterChipItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    activeColor: Color = NeonCyan
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) activeColor.copy(alpha = 0.25f) else Color(0x18FFFFFF))
            .border(
                1.dp,
                if (isSelected) activeColor else GlassBorderLight,
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else TextSecondary,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun WithdrawalHistoryCard(
    item: WithdrawalRequest,
    onCopyTrx: (String) -> Unit
) {
    val methodColor = Color(item.method.colorHex)

    GlassGradientCard(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 20.dp,
        gradientColors = listOf(
            methodColor.copy(alpha = 0.15f),
            Color(0x18FFFFFF),
            Color(0x100F172A)
        ),
        borderColor = if (item.status == WithdrawalStatus.APPROVED) NeonEmerald.copy(alpha = 0.4f) else GlassBorderLight,
        contentPadding = PaddingValues(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row: Method + ID and Live Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(methodColor.copy(alpha = 0.2f))
                            .border(1.dp, methodColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (item.method) {
                                com.example.model.WithdrawalMethod.BKASH -> "💖"
                                com.example.model.WithdrawalMethod.NAGAD -> "🧡"
                                com.example.model.WithdrawalMethod.ROCKET -> "💜"
                            },
                            fontSize = 18.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${item.method.titleBn} (${item.accountType.substringBefore(" ")})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                        )
                        Text(
                            text = "আইডি: #${item.id} • ${item.requestedAt}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Live Status Badge Chip
                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0x18FFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Amount and Points Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "প্রেরিত নম্বর",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                    Text(
                        text = item.targetNumber,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "৳ ${item.amountTaka}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (item.status == WithdrawalStatus.APPROVED) NeonEmeraldLight else TextPrimary,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "(-${item.pointsDeducted} পয়েন্ট)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // TrxID or Admin note if available
            if (item.trxId != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2510B981))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🔑", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TrxID: ${item.trxId}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonEmeraldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                    IconButton(
                        onClick = { onCopyTrx(item.trxId) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy TrxID",
                            tint = NeonEmeraldLight,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            if (!item.adminNote.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "বার্তা: ${item.adminNote}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                )
            }
        }
    }
}
