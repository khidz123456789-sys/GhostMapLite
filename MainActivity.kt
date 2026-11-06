package com.ghostmap.lite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF182230)
private val Muted = Color(0xFF667085)
private val Blue = Color(0xFF2864DC)
private val Canvas = Color(0xFFF5F7FB)
private val Line = Color(0xFFE5EAF2)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GhostMapApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GhostMapApp() {
    var selected by remember { mutableStateOf(0) }
    var config by remember { mutableStateOf("Default") }
    var note by remember { mutableStateOf("") }
    var savedNotes by remember { mutableStateOf(listOf<String>()) }
    val context = LocalContext.current

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Blue, background = Canvas, surface = Color.White,
            onBackground = Ink, onSurface = Ink
        )
    ) {
        Scaffold(
            containerColor = Canvas,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("GhostMap Lite", fontWeight = FontWeight.Bold, color = Ink)
                            Text("Công cụ cá nhân • Miễn phí", fontSize = 12.sp, color = Muted)
                        }
                    },
                    actions = {
                        IconButton(onClick = { selected = 3 }) {
                            Icon(Icons.Default.Settings, contentDescription = "Cài đặt", tint = Ink)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(selected == 0, { selected = 0 }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Trang chủ") })
                    NavigationBarItem(selected == 1, { selected = 1 }, icon = { Icon(Icons.Default.Map, null) }, label = { Text("Bản đồ") })
                    NavigationBarItem(selected == 2, { selected = 2 }, icon = { Icon(Icons.Default.Build, null) }, label = { Text("Công cụ") })
                    NavigationBarItem(selected == 3, { selected = 3 }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Cài đặt") })
                }
            }
        ) { padding ->
            when (selected) {
                0 -> HomeScreen(padding, config, onMap = { selected = 1 }, onTools = { selected = 2 })
                1 -> MapScreen(padding, onOpen = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openstreetmap.org"))
                    context.startActivity(intent)
                })
                2 -> ToolsScreen(padding, note, { note = it }, savedNotes, {
                    if (note.isNotBlank()) { savedNotes = savedNotes + note.trim(); note = "" }
                })
                else -> SettingsScreen(padding, config, { config = it })
            }
        }
    }
}

@Composable
private fun HomeScreen(padding: PaddingValues, config: String, onMap: () -> Unit, onTools: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            Surface(shape = RoundedCornerShape(24.dp), color = Blue, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Xin chào!", color = Color.White.copy(alpha = .8f))
                    Text("Không gian công cụ
của bạn.", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold, lineHeight = 30.sp)
                    Text("Gọn nhẹ • Độc lập • Không cần tài khoản", color = Color.White.copy(alpha = .85f), fontSize = 13.sp)
                    Button(onClick = onMap, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Blue)) {
                        Text("Mở bản đồ"); Icon(Icons.Default.ArrowForward, null, Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
        item { Text("Tổng quan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Ink) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Cấu hình", config, Modifier.weight(1f))
                StatCard("Trạng thái", "Sẵn sàng", Modifier.weight(1f))
            }
        }
        item { FeatureCard("Bản đồ", "Mở OpenStreetMap để xem bản đồ trực tuyến.", Icons.Default.Map, onMap) }
        item { FeatureCard("Công cụ nhanh", "Ghi chú và lưu các mục cần nhớ trên phiên làm việc.", Icons.Default.Build, onTools) }
        item {
            Text("Phiên bản 1.0.0 • GhostMap Lite là dự án độc lập, không liên kết với GhostMap gốc.",
                color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier, shape = RoundedCornerShape(18.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
        Column(Modifier.padding(16.dp)) {
            Text(title, color = Muted, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text(value, color = Ink, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FeatureCard(title: String, subtitle: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White, border = androidx.compose.foundation.BorderStroke(1.dp, Line),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(shape = RoundedCornerShape(12.dp), color = Color(0xFFEAF0FF)) {
                Icon(icon, null, tint = Blue, modifier = Modifier.padding(12.dp))
            }
            Column(Modifier.weight(1f).padding(start = 14.dp)) {
                Text(title, fontWeight = FontWeight.Bold, color = Ink)
                Text(subtitle, color = Muted, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Muted)
        }
    }
}

@Composable
private fun MapScreen(padding: PaddingValues, onOpen: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Bản đồ", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Surface(color = Color.White, shape = RoundedCornerShape(20.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(Icons.Default.Map, null, tint = Blue, modifier = Modifier.size(42.dp))
                Text("OpenStreetMap", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Bản đồ được mở trong trình duyệt. GhostMap Lite không thu thập vị trí của bạn.", color = Muted)
                Button(onClick = onOpen, modifier = Modifier.fillMaxWidth()) { Text("Mở OpenStreetMap") }
            }
        }
        Text("Lưu ý: cần kết nối Internet để tải bản đồ.", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ToolsScreen(padding: PaddingValues, note: String, onNote: (String) -> Unit, notes: List<String>, onSave: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Công cụ nhanh", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Surface(color = Color.White, shape = RoundedCornerShape(18.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Ghi chú phiên làm việc", fontWeight = FontWeight.Bold)
                OutlinedTextField(value = note, onValueChange = onNote, modifier = Modifier.fillMaxWidth(), placeholder = { Text("Nhập ghi chú...") })
                Button(onClick = onSave, enabled = note.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Lưu ghi chú") }
            }
        }
        Text("Ghi chú đã lưu trong phiên này", fontWeight = FontWeight.Bold)
        if (notes.isEmpty()) Text("Chưa có ghi chú.", color = Muted)
        notes.forEachIndexed { index, item ->
            Surface(color = Color.White, shape = RoundedCornerShape(12.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Line)) {
                Text("${index + 1}. $item", Modifier.fillMaxWidth().padding(14.dp))
            }
        }
    }
}

@Composable
private fun SettingsScreen(padding: PaddingValues, config: String, onConfig: (String) -> Unit) {
    Column(Modifier.fillMaxSize().padding(padding).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Cài đặt", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text("Cấu hình đang chọn", color = Muted)
        listOf("Default", "Minimal", "Field Notes").forEach { item ->
            Surface(color = Color.White, shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (config == item) Blue else Line),
                modifier = Modifier.fillMaxWidth().clickable { onConfig(item) }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text(item, fontWeight = FontWeight.Bold)
                        Text(if (item == "Default") "Thiết lập tiêu chuẩn" else "Bộ thiết lập $item", color = Muted, fontSize = 12.sp)
                    }
                    if (config == item) Icon(Icons.Default.CheckCircle, null, tint = Blue)
                }
            }
        }
        Text("GhostMap Lite 1.0.0", color = Muted, fontSize = 12.sp)
    }
}
