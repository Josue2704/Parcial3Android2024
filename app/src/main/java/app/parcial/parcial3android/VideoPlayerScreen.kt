package app.parcial.parcial3android

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(navController: NavHostController, videoUri: Uri) {
    val context = LocalContext.current
    var player: ExoPlayer? by remember { mutableStateOf(null) }

    DisposableEffect(context) {
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }

        onDispose {
            player?.release()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Video Player
        if (player != null) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { PlayerView(context).apply { this.player = player } }
            )
        } else {
            Toast.makeText(context, "Error al cargar el video", Toast.LENGTH_SHORT).show()
        }

        // Botón de regreso
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(16.dp) // Ajusta la posición del botón
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Regresar",
            )
        }
    }
}

