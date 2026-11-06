package com.dskushwahagroup.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF071426)
private val Navy2 = Color(0xFF102744)
private val Gold = Color(0xFFD7B56D)
private val Cream = Color(0xFFF7F3E9)

data class Division(val name: String, val subtitle: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DSKApp() }
    }
}

@Composable
fun DSKApp() {
    var selected by remember { mutableStateOf("Home") }
    var activeDivision by remember { mutableStateOf<Division?>(null) }
    val divisions = listOf(
        Division("DS Properties", "Plots • Land • Homes • Apartments", Icons.Default.HomeWork),
        Division("DS Building Materials", "Quality materials for every build", Icons.Default.Construction),
        Division("DS Poultry Farm", "Farm fresh poultry", Icons.Default.Egg),
        Division("DS AQUA", "Packaged drinking water", Icons.Default.WaterDrop),
        Division("DS Smart Gadgets", "Smart tech & electronics", Icons.Default.Devices),
        Division("DS Lifestyle Brands", "Apparel & footwear", Icons.Default.Checkroom),
        Division("DS Solar Technology", "Smarter clean energy", Icons.Default.WbSunny)
    )
    MaterialTheme(colorScheme = darkColorScheme(primary = Gold, background = Navy, surface = Navy2)) {
        Scaffold(
            containerColor = Navy,
            bottomBar = {
                NavigationBar(containerColor = Navy2, contentColor = Gold) {
                    listOf("Home", "Businesses", "About").forEach { item ->
                        NavigationBarItem(
                            selected = selected == item,
                            onClick = { selected = item; activeDivision = null },
                            icon = { Icon(when(item) { "Home" -> Icons.Default.Home; "Businesses" -> Icons.Default.GridView; else -> Icons.Default.Info }, null) },
                            label = { Text(item) },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = Gold, selectedTextColor = Gold, indicatorColor = Gold.copy(alpha = .16f), unselectedIconColor = Color.LightGray, unselectedTextColor = Color.LightGray)
                        )
                    }
                }
            }
        ) { padding ->
            if (activeDivision != null) {
                DivisionDetail(activeDivision!!, Modifier.padding(padding)) { activeDivision = null }
            } else when (selected) {
                "Businesses" -> BusinessList(divisions, Modifier.padding(padding)) { activeDivision = it }
                "About" -> AboutScreen(Modifier.padding(padding))
                else -> HomeScreen(divisions, Modifier.padding(padding), onExplore = { selected = "Businesses" }, onSelect = { activeDivision = it })
            }
        }
    }
}

@Composable
fun BrandMark(size: Int = 72) {
    Box(Modifier.size(size.dp).background(Brush.linearGradient(listOf(Gold, Color(0xFF8F713B))), CircleShape).padding(3.dp).background(Navy, CircleShape), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("DS", color = Gold, fontSize = (size * .34).sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Text("GROUP", color = Color.White, fontSize = (size * .095).sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
    }
}

@Composable
fun HomeScreen(divisions: List<Division>, modifier: Modifier, onExplore: () -> Unit, onSelect: (Division) -> Unit) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Navy).padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark(58)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("DS KUSHWAHA GROUP", color = Gold, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                Text("A GROUP OF TRUST & EXCELLENCE", color = Color.LightGray, fontSize = 9.sp, letterSpacing = 1.3.sp)
            }
        }
        Spacer(Modifier.height(22.dp))
        Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Navy2, Color(0xFF1D3654))), RoundedCornerShape(24.dp)).padding(22.dp)) {
            Column {
                Text("WELCOME TO", color = Gold, fontSize = 12.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("One Group.\nSeven Businesses.\nEndless Possibilities.", color = Color.White, fontSize = 29.sp, lineHeight = 35.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(10.dp))
                Text("Discover our businesses and services, all in one place.", color = Color(0xFFD5DCE5), fontSize = 14.sp)
                Spacer(Modifier.height(18.dp))
                Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy), shape = RoundedCornerShape(12.dp)) {
                    Text("EXPLORE BUSINESSES", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp)); Icon(Icons.Default.ArrowForward, null)
                }
            }
        }
        Spacer(Modifier.height(24.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("OUR BUSINESSES", color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            Text("VIEW ALL", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onExplore))
        }
        Spacer(Modifier.height(12.dp))
        divisions.take(4).forEach { DivisionCard(it) { onSelect(it) }; Spacer(Modifier.height(10.dp)) }
        Spacer(Modifier.height(8.dp))
        Text("Built on trust. Growing with you.", color = Gold, fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun DivisionCard(item: Division, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().background(Navy2, RoundedCornerShape(16.dp)).clickable(onClick = onClick).padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(48.dp).background(Gold.copy(alpha = .14f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(item.icon, null, tint = Gold, modifier = Modifier.size(26.dp))
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(item.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(item.subtitle, color = Color.LightGray, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, null, tint = Gold)
    }
}

@Composable
fun BusinessList(divisions: List<Division>, modifier: Modifier, onSelect: (Division) -> Unit) {
    Column(modifier.fillMaxSize().background(Navy).padding(18.dp)) {
        Text("Our Businesses", color = Gold, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Text("Seven specialised divisions under one trusted group.", color = Color.LightGray, fontSize = 13.sp)
        Spacer(Modifier.height(18.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(divisions) { item ->
                Column(Modifier.fillMaxWidth().height(150.dp).background(Navy2, RoundedCornerShape(18.dp)).clickable { onSelect(item) }.padding(14.dp), verticalArrangement = Arrangement.SpaceBetween) {
                    Icon(item.icon, null, tint = Gold, modifier = Modifier.size(30.dp))
                    Column {
                        Text(item.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(item.subtitle, color = Color.LightGray, fontSize = 10.sp, lineHeight = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun DivisionDetail(item: Division, modifier: Modifier, onBack: () -> Unit) {
    Column(modifier.fillMaxSize().background(Navy).padding(20.dp)) {
        Text("‹  BACK", color = Gold, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onBack))
        Spacer(Modifier.height(34.dp))
        Box(Modifier.size(76.dp).background(Gold.copy(alpha = .15f), RoundedCornerShape(22.dp)), contentAlignment = Alignment.Center) { Icon(item.icon, null, tint = Gold, modifier = Modifier.size(40.dp)) }
        Spacer(Modifier.height(20.dp))
        Text(item.name, color = Gold, fontSize = 27.sp, fontWeight = FontWeight.ExtraBold)
        Text(item.subtitle, color = Color.White, fontSize = 15.sp)
        Spacer(Modifier.height(18.dp))
        Text("Welcome to ${item.name}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Spacer(Modifier.height(8.dp))
        Text("Explore products and services from DS KUSHWAHA GROUP. For enquiries and assistance, please contact our team.", color = Color.LightGray, fontSize = 14.sp, lineHeight = 22.sp)
        Spacer(Modifier.height(22.dp))
        Text("Contact: 9621581509  •  8933979022", color = Gold, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
fun AboutScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().background(Navy).padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(28.dp)); BrandMark(100); Spacer(Modifier.height(22.dp))
        Text("DS KUSHWAHA GROUP", color = Gold, fontWeight = FontWeight.ExtraBold, fontSize = 23.sp, textAlign = TextAlign.Center)
        Text("A GROUP OF TRUST & EXCELLENCE", color = Color.LightGray, fontSize = 10.sp, letterSpacing = 1.4.sp)
        Spacer(Modifier.height(24.dp))
        Text("Diversified businesses. One commitment: quality, trust and long-term relationships.", color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center, lineHeight = 25.sp)
        Spacer(Modifier.height(24.dp))
        Text("GHANENDRA KUMAR", color = Gold, fontWeight = FontWeight.Bold, fontSize = 17.sp)
        Text("Managing Director", color = Color.LightGray, fontSize = 13.sp)
        Spacer(Modifier.height(20.dp))
        Text("9621581509  •  8933979022", color = Color.White, fontSize = 14.sp)
        Spacer(Modifier.height(8.dp))
        Text("Mauranipur - 284204, Uttar Pradesh", color = Color.LightGray, fontSize = 12.sp)
    }
}
