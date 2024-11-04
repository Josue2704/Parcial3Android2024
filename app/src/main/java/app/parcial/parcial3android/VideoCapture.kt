package app.parcial.parcial3android.camera

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import app.parcial.parcial3android.createFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.content.ContentValues
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import android.Manifest
import android.content.pm.PackageManager

@Composable
fun VideoCaptureUI(
    onVideoRecorded: (Uri) -> Unit,
    onError: (Exception) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var recording: Recording? by remember { mutableStateOf(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isStoppingRecording by remember { mutableStateOf(false) }

    val videoCapture: VideoCapture<Recorder> = remember {
        val recorder = Recorder.Builder()
            .setQualitySelector(QualitySelector.from(Quality.HD))
            .build()
        VideoCapture.withOutput(recorder)
    }
    val preview = remember { Preview.Builder().build() }

    LaunchedEffect(cameraProviderFuture) {
        val cameraProvider = cameraProviderFuture.await()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                videoCapture
            )
        } catch (e: Exception) {
            Log.e("VideoCapture", "Error al inicializar la cámara", e)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    this.scaleType = PreviewView.ScaleType.FILL_CENTER
                    preview.setSurfaceProvider(this.surfaceProvider)
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                if (isRecording) {
                    recording?.stop()
                    recording = null
                    isStoppingRecording = true
                } else {
                    if (isStoppingRecording) {
                        Toast.makeText(
                            context,
                            "Esperando a que se detenga la grabación anterior.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val name = "VID_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
                        val contentValues = ContentValues().apply {
                            put(MediaStore.Video.Media.DISPLAY_NAME, "$name.mp4")
                            put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                put(MediaStore.Video.Media.RELATIVE_PATH, Environment.DIRECTORY_MOVIES)
                            }
                        }

                        val mediaStoreOutputOptions = MediaStoreOutputOptions.Builder(
                            context.contentResolver,
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                        )
                            .setContentValues(contentValues)
                            .build()

                        val hasAudioPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.RECORD_AUDIO
                        ) == PackageManager.PERMISSION_GRANTED

                        recording = videoCapture.output
                            .prepareRecording(context, mediaStoreOutputOptions)
                            .apply {
                                if (hasAudioPermission) {
                                    withAudioEnabled()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Permiso de audio no concedido, el video se grabará sin audio.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            .start(ContextCompat.getMainExecutor(context)) { recordEvent ->
                                when (recordEvent) {
                                    is VideoRecordEvent.Start -> {
                                        isRecording = true
                                    }
                                    is VideoRecordEvent.Finalize -> {
                                        isRecording = false
                                        isStoppingRecording = false
                                        if (!recordEvent.hasError()) {
                                            val uri = recordEvent.outputResults.outputUri
                                            onVideoRecorded(uri)
                                            Toast.makeText(
                                                context,
                                                "Video guardado: $uri",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            onError(Exception("Error al grabar el video: ${recordEvent.error}"))
                                        }
                                    }
                                }
                            }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                contentDescription = if (isRecording) "Detener Grabación" else "Iniciar Grabación",
                tint = if (isRecording) Color.Red else Color.White,
                modifier = Modifier.size(64.dp)
            )
        }
    }
}