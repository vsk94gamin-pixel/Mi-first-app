package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun AdminScreen(
    viewModel: TakaFlowViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val requests by viewModel.withdrawalRequests.collectAsState()
    val user by viewModel.currentUser.collectAsState()

    val pendingCount = requests.count { it.status == WithdrawalStatus.PENDING }
    val processingCount = requests.count { it.status == WithdrawalStatus.PROCESSING }
    val approvedTotalTaka = requests.filter { it.status == WithdrawalStatus.APPROVED }.sumOf { it.amountTaka }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 18.dp)
            .testTag("admin_screen_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x35F43F5E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛠️", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "এডমিন কন্ট্রোল প্যানেল",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "লাইভ উইথড্রল টেস্টিং ও ম্যানেজমেন্ট",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CoralRose,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Admin",
                        tint = TextSecondary
                    )
                }
            }
        }

        // Stats Overview Card
        item {
            GlassGradientCard(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 20.dp,
                gradientColors = listOf(Color(0x35F43F5E), Color(0x258B5CF6), Color(0x150F172A)),
                borderColor = CoralRose.copy(alpha = 0.5f),
                contentPadding = PaddingValues(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$pendingCount",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = AmberGoldLight,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "পেন্ডিং",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = Color(0x30FFFFFF)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$processingCount",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonCyanGlow,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "প্রসেসিং",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    Divider(
                        modifier = Modifier
                            .height(36.dp)
                            .width(1.dp),
                        color = Color(0x30FFFFFF)
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "৳ $approvedTotalTaka",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonEmeraldLight,
                                fontSize = 20.sp
                            )
                        )
                        Text(
                            text = "মোট পেআউট",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Quick Points Adjuster for Current User
        item {
            GlassBox(
                modifier = Modifier.fillMaxWidth(),
                cornerRadius = 18.dp,
                backgroundColor = Color(0x20FFFFFF),
                borderColor = GlassBorderLight,
                contentPadding = PaddingValues(14.dp)
            ) {
                Column {
                    Text(
                        text = "টেস্ট ইউজার পয়েন্ট অ্যাডজাস্ট ⚡ (বর্তমান: ${user?.points ?: 0} পয়েন্ট)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "+১,০০০ পয়েন্ট",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyanGlow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x3006B6D4))
                                .clickable { viewModel.adminAddPoints(1000) }
                                .padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "+৫,০০০ (৳৫০)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonEmeraldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x3010B981))
                                .clickable { viewModel.adminAddPoints(5000) }
                                .padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "+১০,০০০ (৳১০০)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = AmberGoldLight,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0x30F59E0B))
                                .clickable { viewModel.adminAddPoints(10000) }
                                .padding(vertical = 8.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        // Withdrawal Request List with Action Buttons
        item {
            Text(
                text = "উইথড্রল রিকোয়েস্ট ম্যানেজমেন্ট (${requests.size} টি)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 15.sp
                )
            )
        }

        items(requests, key = { it.id }) { req ->
            AdminRequestItemCard(
                req = req,
                onApprove = {
                    viewModel.adminUpdateStatus(req.id, WithdrawalStatus.APPROVED)
                },
                onProcessing = {
                    viewModel.adminUpdateStatus(req.id, WithdrawalStatus.PROCESSING)
                },
                onReject = {
                    viewModel.adminUpdateStatus(req.id, WithdrawalStatus.REJECTED)
                },
                onDelete = {
                    viewModel.adminDeleteRequest(req.id)
                }
            )
        }
    }
}

@Composable
private fun AdminRequestItemCard(
    req: WithdrawalRequest,
    onApprove: () -> Unit,
    onProcessing: () -> Unit,
    onReject: () -> Unit,
    onDelete: () -> Unit
) {
    val methodColor = Color(req.method.colorHex)

    GlassBox(
        modifier = Modifier.fillMaxWidth(),
        cornerRadius = 18.dp,
        backgroundColor = Color(0x22FFFFFF),
        borderColor = if (req.status == WithdrawalStatus.PENDING) AmberGold.copy(alpha = 0.6f) else GlassBorderLight,
        contentPadding = PaddingValues(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${req.userName} • ${req.targetNumber}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = "${req.method.titleBn} (${req.accountType}) • #${req.id}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "৳ ${req.amountTaka}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NeonEmeraldLight,
                            fontSize = 17.sp
                        )
                    )
                    StatusBadge(status = req.status)
                }
            }

            if (req.trxId != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "TrxID: ${req.trxId}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = NeonCyanGlow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color(0x15FFFFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Approve
                if (req.status != WithdrawalStatus.APPROVED) {
                    Text(
                        text = "✅ এপ্রুভ ও পে",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonEmeraldLight,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3010B981))
                            .clickable { onApprove() }
                            .padding(vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Processing
                if (req.status != WithdrawalStatus.PROCESSING && req.status != WithdrawalStatus.APPROVED) {
                    Text(
                        text = "🔄 প্রসেসিং",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyanGlow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x3006B6D4))
                            .clickable { onProcessing() }
                            .padding(vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Reject & Refund
                if (req.status != WithdrawalStatus.REJECTED) {
                    Text(
                        text = "❌ রিজেক্ট ও রিফান্ড",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = CoralRose,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0x30F43F5E))
                            .clickable { onReject() }
                            .padding(vertical = 6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }

                // Delete
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = TextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
