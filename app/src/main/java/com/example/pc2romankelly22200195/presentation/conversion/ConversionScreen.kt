package com.example.pc2romankelly22200195.presentation.conversion

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pc2romankelly22200195.data.remote.FirebaseAuthManager
import com.example.pc2romankelly22200195.data.remote.FirestoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen(navController: NavController) {
    val currencies = listOf("USD", "EUR", "PEN", "GBP", "JPY")
    val rates = mapOf("USD" to 1.0, "EUR" to 0.92, "PEN" to 3.72, "GBP" to 0.79, "JPY" to 149.50)

    var monto by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("USD") }
    var toCurrency by remember { mutableStateOf("EUR") }
    var resultado by remember { mutableStateOf("") }
    var expandedFrom by remember { mutableStateOf(false) }
    var expandedTo by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Conversor de Divisas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = monto, onValueChange = { monto = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text("De", modifier = Modifier.align(Alignment.Start))
        ExposedDropdownMenuBox(expanded = expandedFrom, onExpandedChange = { expandedFrom = it }) {
            OutlinedTextField(
                value = fromCurrency, onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFrom) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedFrom, onDismissRequest = { expandedFrom = false }) {
                currencies.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { fromCurrency = it; expandedFrom = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        Text("A", modifier = Modifier.align(Alignment.Start))
        ExposedDropdownMenuBox(expanded = expandedTo, onExpandedChange = { expandedTo = it }) {
            OutlinedTextField(
                value = toCurrency, onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTo) },
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expandedTo, onDismissRequest = { expandedTo = false }) {
                currencies.forEach {
                    DropdownMenuItem(text = { Text(it) }, onClick = { toCurrency = it; expandedTo = false })
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val amount = monto.toDoubleOrNull()
                if (amount == null) {
                    Toast.makeText(context, "Ingrese un monto válido", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                val amountInUSD = amount / (rates[fromCurrency] ?: 1.0)
                val converted = amountInUSD * (rates[toCurrency] ?: 1.0)
                val resultFormatted = String.format("%.2f", converted)
                resultado = "$amount $fromCurrency equivalen a $resultFormatted $toCurrency"

                val uid = FirebaseAuthManager.getCurrentUid() ?: return@Button
                CoroutineScope(Dispatchers.Main).launch {
                    FirestoreManager.saveConversion(uid, amount, fromCurrency, toCurrency, converted)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Convertir") }

        Spacer(modifier = Modifier.height(12.dp))
        if (resultado.isNotEmpty()) {
            Text(resultado, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))
        TextButton(onClick = { navController.navigate("history") }) {
            Text("Ver historial")
        }
        TextButton(onClick = {
            FirebaseAuthManager.logout()
            navController.navigate("login") { popUpTo("conversion") { inclusive = true } }
        }) {
            Text("Cerrar sesión")
        }
    }
}