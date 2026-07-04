package com.example.pc2romankelly22200195.presentation.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pc2romankelly22200195.dat.model.ConversionModel
import com.example.pc2romankelly22200195.data.remote.FirebaseAuthManager
import com.example.pc2romankelly22200195.data.remote.FirestoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HistoryScreen(navController: NavController) {
    var conversions by remember { mutableStateOf<List<ConversionModel>>(emptyList()) }

    LaunchedEffect(Unit) {
        val uid = FirebaseAuthManager.getCurrentUid() ?: return@LaunchedEffect
        CoroutineScope(Dispatchers.Main).launch {
            val result = FirestoreManager.getConversions(uid)
            if (result.isSuccess) conversions = result.getOrDefault(emptyList())
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).statusBarsPadding()) {
        Text("Historial de Conversiones", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (conversions.isEmpty()) {
            Text("No hay conversiones registradas.")
        } else {
            LazyColumn {
                items(conversions) { conv ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${conv.amount} ${conv.fromCurrency} → ${String.format("%.2f", conv.result)} ${conv.toCurrency}")
                            Text(conv.date, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}