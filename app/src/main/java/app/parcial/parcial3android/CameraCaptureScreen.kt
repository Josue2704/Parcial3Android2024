package app.parcial.parcial3android

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import app.parcial.parcial3android.CameraCapture

@Composable
fun CameraCaptureScreen(navController: NavHostController) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var flashEnabled by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraCapture(
            onImageCaptured = { uri ->
                imageUri = uri
                Toast.makeText(context, "Foto guardada en la galería", Toast.LENGTH_SHORT).show()
            },
            onError = { exception ->
                Toast.makeText(context, "Error al capturar la foto: ${exception.message}", Toast.LENGTH_SHORT).show()
            },
            flashEnabled = flashEnabled
        )

        IconButton(
            onClick = { flashEnabled = !flashEnabled },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Flash",
                tint = if (flashEnabled) Color.Yellow else Color.Gray
            )
        }
    }
}
