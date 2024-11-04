package app.parcial.parcial3android

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import java.io.File

@Composable
fun GalleryScreen(navController: NavHostController) {
    val context = LocalContext.current
    val imagesDir = context.getExternalFilesDir("images")
    val videosDir = context.getExternalFilesDir("videos")

    val imageFiles: List<File> = imagesDir?.listFiles()?.sortedByDescending { it.lastModified() }?.toList() ?: emptyList()
    val videoFiles: List<File> = videosDir?.listFiles()?.sortedByDescending { it.lastModified() }?.toList() ?: emptyList()

    LazyColumn {
        items(imageFiles) { file ->
            val uri = Uri.fromFile(file)
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp)
            )
        }
        items(videoFiles) { file ->
            val uri = Uri.fromFile(file)
            Text(
                text = "Video: ${file.name}",
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("video_player/${Uri.encode(uri.toString())}")
                    }
            )
        }
    }
}

