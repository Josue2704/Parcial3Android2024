package app.parcial.parcial3android

import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import app.parcial.parcial3android.camera.VideoCaptureUI

@Composable
fun VideoCaptureScreen(navController: NavHostController) {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }

    VideoCaptureUI(
        onVideoRecorded = { uri ->
            videoUri = uri
            Toast.makeText(context, "Video guardado en la galería", Toast.LENGTH_SHORT).show()
        },
        onError = { exception ->
            Toast.makeText(context, "Error al grabar el video: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    )
}