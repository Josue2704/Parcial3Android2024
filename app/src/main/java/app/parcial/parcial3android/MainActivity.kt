package app.parcial.parcial3android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import app.parcial.parcial3android.ui.theme.Parcial3AndroidTheme
import app.parcial.parcial3android.utils.Permission

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Parcial3AndroidTheme {
                val permissions = mutableListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                )

                // Añadir permisos de almacenamiento según la versión de Android
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                } else {
                    permissions.addAll(
                        listOf(
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO
                        )
                    )
                }

                Permission(
                    permissions = permissions,
                    rationaleText = "Se necesitan permisos para acceder a la cámara y almacenamiento."
                ) {
                    MainScreen()
                }
            }
        }
    }
}