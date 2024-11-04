@file:OptIn(ExperimentalMaterial3Api::class)

package app.parcial.parcial3android

import android.net.Uri
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Videocam
import androidx.navigation.NavType
import androidx.navigation.navArgument
import app.parcial.parcial3android.ui.theme.Parcial3AndroidTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Parcial3Android") }) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "camera_capture",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("camera_capture") { CameraCaptureScreen(navController) }
            composable("video_capture") { VideoCaptureScreen(navController) }
            composable("gallery") { GalleryScreen(navController) }
            composable(
                route = "video_player/{videoUri}",
                arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoUri = Uri.parse(backStackEntry.arguments?.getString("videoUri"))
                VideoPlayerScreen(navController, videoUri)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf("camera_capture", "video_capture", "gallery")
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        "camera_capture" -> Icon(Icons.Default.Camera, contentDescription = null)
                        "video_capture" -> Icon(Icons.Default.Videocam, contentDescription = null)
                        "gallery" -> Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    }
                },
                label = {
                    when (screen) {
                        "camera_capture" -> Text("Foto")
                        "video_capture" -> Text("Video")
                        "gallery" -> Text("Galería")
                    }
                },
                selected = navController.currentBackStackEntry?.destination?.route == screen,
                onClick = {
                    navController.navigate(screen) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
