@file:OptIn(ExperimentalPermissionsApi::class)

package app.parcial.parcial3android.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext

@Composable
fun Permission(
    permissions: List<String>,
    rationaleText: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    var permissionsGranted by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        permissionsGranted = result.all { it.value }
    }

    LaunchedEffect(Unit) {
        launcher.launch(permissions.toTypedArray())
    }

    if (permissionsGranted) {
        content()
    } else {
        // Manejo de permisos denegados
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = rationaleText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    launcher.launch(permissions.toTypedArray())
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Solicitar Permisos")
            }
        }
    }
}
