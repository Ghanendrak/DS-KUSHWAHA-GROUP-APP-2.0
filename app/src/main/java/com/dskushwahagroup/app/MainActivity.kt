package com.dskushwahagroup.app

import android.os.Bundle
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF071426)
private val Navy2 = Color(0xFF102744)
private val Gold = Color(0xFFD7B56D)

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
    var searchText by remember { mutableStateOf("") }
    var cart by remember { mutableStateOf(mapOf<String, Int>()) }
    var checkout by remember { mutableStateOf(false) }
    val divisions = listOf(
        Division("DS Properties", "Plots • Land • Homes • Apartments", Icons.Default.HomeWork),
        Division("DS Building Materials", "Quality materials for every build", Icons.Default.Construction),
        Division("DS Poultry Farm", "Farm fresh poultry", Icons.Default.Pets),
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
                    listOf(
                        Triple("Home", Icons.Default.Home, "Home"),
                        Triple("Categories", Icons.Default.GridView, "Categories"),
                        Triple("Cart", Icons.Default.ShoppingCart, "Cart"),
                        Triple("Account", Icons.Default.Person, "Account")
                    ).forEach { item ->
                        NavigationBarItem(
                            selected = selected == item.first,
                            onClick = { selected = item.first; activeDivision = null },
                            icon = { Icon(item.second, contentDescription = item.first) },
                            label = { Text(item.third) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Gold, selectedTextColor = Gold,
                                indicatorColor = Gold.copy(alpha = .16f),
                                unselectedIconColor = Color.LightGray, unselectedTextColor = Color.LightGray
                            )
                        )
                    }
                }
            }
        ) { padding ->
            if (checkout) {
                CheckoutScreen(cart, Modifier.padding(padding), onBack = { checkout = false }, onPlaceOrder = { checkout = false; selected = "Cart" })
            } else if (activeDivision != null) {
                DivisionDetail(activeDivision!!, Modifier.padding(padding), onBack = { activeDivision = null }, onAdd = {
                    cart = cart + (it.name to ((cart[it.name] ?: 0) + 1))
                    activeDivision = null
                    selected = "Cart"
                })
            } else when (selected) {
                "Categories" -> BusinessList(divisions, searchText, { searchText = it }, Modifier.padding(padding)) { activeDivision = it }
                "Cart" -> CartScreen(cart, Modifier.padding(padding),
                    onIncrease = { name -> cart = cart + (name to ((cart[name] ?: 0) + 1)) },
                    onDecrease = { name -> val n = (cart[name] ?: 0) - 1; cart = if (n <= 0) cart - name else cart + (name to n) },
                    onRemove = { name -> cart = cart - name },
                    onCheckout = { checkout = true })
                "Account" -> AccountScreen(Modifier.padding(padding))
                else -> HomeScreen(divisions, searchText, { searchText = it }, Modifier.padding(padding),
                    onExplore = { selected = "Categories" }, onSelect = { activeDivision = it })
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
fun SearchBar(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search for businesses, products & services", fontSize = 12.sp) },
        leadingIcon = { Icon(Icons.Default.Search, null, tint = Gold) },
        trailingIcon = { Icon(Icons.Default.Mic, null, tint = Gold) },
        singleLine = true, shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White, unfocusedTextColor = Color.White,
            focusedBorderColor = Gold, unfocusedBorderColor = Color(0xFF52647A),
            focusedContainerColor = Navy2, unfocusedContainerColor = Navy2,
            focusedPlaceholderColor = Color.LightGray, unfocusedPlaceholderColor = Color.LightGray,
            cursorColor = Gold
        )
    )
}

@Composable
fun HomeScreen(divisions: List<Division>, search: String, onSearch: (String) -> Unit, modifier: Modifier, onExplore: () -> Unit, onSelect: (Division) -> Unit) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).background(Navy).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark(48)
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f)) {
                Text("DS KUSHWAHA GROUP", color = Gold, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                Text("Mauranipur • Uttar Pradesh", color = Color.LightGray, fontSize = 11.sp)
            }
            Icon(Icons.Default.NotificationsNone, null, tint = Color.White, modifier = Modifier.size(25.dp))
        }
        Spacer(Modifier.height(14.dp))
        SearchBar(search, onSearch)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.LocationOn, null, tint = Gold, modifier = Modifier.size(18.dp))
            Text("Delivering services across your location", color = Color.LightGray, fontSize = 11.sp)
        }
        Spacer(Modifier.height(16.dp))
        Box(Modifier.fillMaxWidth().background(Brush.linearGradient(listOf(Navy2, Color(0xFF1D3654))), RoundedCornerShape(22.dp)).padding(20.dp)) {
            Column {
                Text("WELCOME TO", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(7.dp))
                Text("One Group.\nSeven Businesses.\nEndless Possibilities.", color = Color.White, fontSize = 27.sp, lineHeight = 33.sp, fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.height(9.dp))
                Text("Discover trusted products and services, all in one place.", color = Color(0xFFD5DCE5), fontSize = 13.sp)
                Spacer(Modifier.height(15.dp))
                Button(onClick = onExplore, colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy), shape = RoundedCornerShape(12.dp)) {
                    Text("EXPLORE NOW", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(7.dp)); Icon(Icons.Default.ArrowForward, null)
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("SHOP BY CATEGORY", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("VIEW ALL", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onExplore))
        }
        Spacer(Modifier.height(12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            divisions.take(4).forEach { item ->
                Column(Modifier.weight(1f).clickable { onSelect(item) }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(56.dp).background(Navy2, CircleShape), contentAlignment = Alignment.Center) {
                        Icon(item.icon, null, tint = Gold, modifier = Modifier.size(27.dp))
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(item.name.removePrefix("DS "), color = Color.White, fontSize = 9.sp, textAlign = TextAlign.Center, maxLines = 2, lineHeight = 11.sp)
                }
            }
        }
        Spacer(Modifier.height(22.dp))
        Text("FEATURED BUSINESSES", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(11.dp))
        divisions.drop(4).forEach { DivisionCard(it) { onSelect(it) }; Spacer(Modifier.height(10.dp)) }
        Text("Built on trust. Growing with you.", color = Gold, fontSize = 13.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(Modifier.height(14.dp))
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
fun BusinessList(divisions: List<Division>, search: String, onSearch: (String) -> Unit, modifier: Modifier, onSelect: (Division) -> Unit) {
    Column(modifier.fillMaxSize().background(Navy).padding(16.dp)) {
        Text("All Categories", color = Gold, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Text("Explore all seven businesses of our group.", color = Color.LightGray, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        SearchBar(search, onSearch)
        Spacer(Modifier.height(14.dp))
        val filtered = divisions.filter { it.name.contains(search, true) || it.subtitle.contains(search, true) }
        LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered) { item ->
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
fun DivisionDetail(item: Division, modifier: Modifier, onBack: () -> Unit, onAdd: (Division) -> Unit) {
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
        Spacer(Modifier.height(24.dp))
        Button(onClick = { onAdd(item) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy)) {
            Icon(Icons.Default.AddShoppingCart, null); Spacer(Modifier.width(8.dp)); Text("ADD TO CART", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CartScreen(cart: Map<String, Int>, modifier: Modifier, onIncrease: (String) -> Unit, onDecrease: (String) -> Unit, onRemove: (String) -> Unit, onCheckout: () -> Unit) {
    Column(modifier.fillMaxSize().background(Navy).padding(18.dp)) {
        Text("My Cart", color = Gold, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Text("${cart.values.sum()} item(s)", color = Color.LightGray, fontSize = 12.sp)
        Spacer(Modifier.height(18.dp))
        if (cart.isEmpty()) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(Icons.Default.ShoppingCart, null, tint = Gold, modifier = Modifier.size(64.dp))
                Spacer(Modifier.height(14.dp))
                Text("Your cart is empty", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Browse a business category and add an item or service.", color = Color.LightGray, fontSize = 12.sp, textAlign = TextAlign.Center)
            }
        } else {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                cart.forEach { (name, qty) ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp).background(Navy2, RoundedCornerShape(14.dp)).padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Price on request", color = Gold, fontSize = 11.sp)
                        }
                        IconButton(onClick = { onDecrease(name) }) { Icon(Icons.Default.RemoveCircleOutline, null, tint = Gold) }
                        Text(qty.toString(), color = Color.White, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { onIncrease(name) }) { Icon(Icons.Default.AddCircleOutline, null, tint = Gold) }
                        IconButton(onClick = { onRemove(name) }) { Icon(Icons.Default.DeleteOutline, null, tint = Color.LightGray) }
                    }
                }
            }
            Button(onClick = onCheckout, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy), shape = RoundedCornerShape(13.dp)) {
                Text("PROCEED TO CHECKOUT", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}

@Composable
fun CheckoutScreen(cart: Map<String, Int>, modifier: Modifier, onBack: () -> Unit, onPlaceOrder: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("Mauranipur") }
    var pincode by remember { mutableStateOf("284204") }
    var notes by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(modifier.fillMaxSize().background(Navy).verticalScroll(rememberScrollState()).padding(18.dp)) {
        Text("‹  BACK TO CART", color = Gold, fontWeight = FontWeight.Bold, modifier = Modifier.clickable(onClick = onBack))
        Spacer(Modifier.height(16.dp))
        Text("Delivery Details", color = Gold, fontSize = 25.sp, fontWeight = FontWeight.ExtraBold)
        Text("Please enter the customer's delivery/contact information.", color = Color.LightGray, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        CheckoutField("Customer name *", name) { name = it }
        CheckoutField("Mobile number *", phone) { phone = it.filter(Char::isDigit).take(10) }
        CheckoutField("Full delivery address *", address) { address = it }
        CheckoutField("City / Town *", city) { city = it }
        CheckoutField("PIN code *", pincode) { pincode = it.filter(Char::isDigit).take(6) }
        CheckoutField("Landmark / delivery instructions (optional)", notes) { notes = it }
        Spacer(Modifier.height(12.dp))
        Text("Order summary: ${cart.values.sum()} item(s)", color = Color.White, fontWeight = FontWeight.Bold)
        Text("Final price and delivery availability will be confirmed by our team.", color = Color.LightGray, fontSize = 11.sp)
        if (error.isNotBlank()) Text(error, color = Color(0xFFFF8A80), fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            error = when {
                name.isBlank() || address.isBlank() || city.isBlank() -> "Please fill all required details."
                phone.length != 10 -> "Please enter a valid 10-digit mobile number."
                pincode.length != 6 -> "Please enter a valid 6-digit PIN code."
                else -> ""
            }
            if (error.isBlank()) {
                val orderLines = cart.entries.joinToString("\\n") { "• ${it.key} × ${it.value}" }
                val message = "DS KUSHWAHA GROUP - Order / Enquiry\\n\\nCustomer: $name\\nMobile: $phone\\nAddress: $address\\nCity: $city\\nPIN: $pincode\\nLandmark / Instructions: $notes\\n\\nItems:\\n$orderLines\\n\\nPlease confirm price and availability."
                val url = "https://wa.me/919621581509?text=${Uri.encode(message)}"
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                onPlaceOrder()
            }
        }, modifier = Modifier.fillMaxWidth().height(52.dp), colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Navy)) {
            Text("SUBMIT ORDER / ENQUIRY", fontWeight = FontWeight.ExtraBold)
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun CheckoutField(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), singleLine = label != "Full delivery address *" && !label.startsWith("Landmark"), colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.White, unfocusedTextColor = Color.White,
        focusedBorderColor = Gold, unfocusedBorderColor = Color(0xFF52647A),
        focusedLabelColor = Gold, unfocusedLabelColor = Color.LightGray,
        cursorColor = Gold, focusedContainerColor = Navy2, unfocusedContainerColor = Navy2
    ))
}

@Composable
fun AccountScreen(modifier: Modifier) {
    Column(modifier.fillMaxSize().background(Navy).padding(22.dp)) {
        Text("My Account", color = Gold, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(Modifier.height(22.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            BrandMark(64); Spacer(Modifier.width(14.dp))
            Column {
                Text("Welcome to DS KUSHWAHA GROUP", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Customer account", color = Color.LightGray, fontSize = 12.sp)
            }
        }
        Spacer(Modifier.height(24.dp))
        listOf("My Orders & Enquiries", "Saved Addresses", "Customer Support", "About DS KUSHWAHA GROUP").forEach {
            Row(Modifier.fillMaxWidth().padding(vertical = 15.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(it, color = Color.White, modifier = Modifier.weight(1f), fontSize = 14.sp)
                Icon(Icons.Default.ChevronRight, null, tint = Gold)
            }
            HorizontalDivider(color = Color.White.copy(alpha = .12f))
        }
        Spacer(Modifier.height(20.dp))
        Text("Support: 9621581509  •  8933979022", color = Gold, fontSize = 13.sp)
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
        Text("Bank of India ke bagal mein, Nadi Pull ke paas, Mauranipur - 284204", color = Color.LightGray, fontSize = 12.sp, textAlign = TextAlign.Center)
    }
}
