Parcial3Android
Este proyecto es una aplicación de Android desarrollada en Kotlin utilizando Jetpack Compose. La aplicación permite capturar fotos y videos utilizando la cámara del dispositivo y visualizarlos en una galería integrada. Está diseñada para funcionar en dispositivos con versiones de Android modernas y hace uso de las últimas API y buenas prácticas recomendadas por Google.
Contenido
•	Características
•	Estructura del Proyecto
•	Dependencias
•	Explicación del Código
o	MainActivity.kt
o	Permissions.kt
o	VideoCaptureUI.kt
o	GalleryScreen.kt
•	Cómo Ejecutar la Aplicación
•	Conclusión
Características
•	Captura de Fotos y Videos: Utiliza CameraX para capturar fotos y videos de alta calidad.
•	Grabación de Audio: Graba audio junto con el video, siempre y cuando se otorguen los permisos necesarios.
•	Almacenamiento en MediaStore: Las fotos y videos se guardan en el almacenamiento público del dispositivo, haciéndolos accesibles para otras aplicaciones.
•	Gestión de Permisos: Maneja los permisos de cámara, audio y almacenamiento de manera dinámica y adaptada a las diferentes versiones de Android.
•	Interfaz de Usuario con Jetpack Compose: La UI está construida completamente con Jetpack Compose, siguiendo las prácticas modernas de desarrollo.

 
Dependencias
El proyecto utiliza las siguientes dependencias principales:
•	Jetpack Compose: Para la construcción de la interfaz de usuario.
•	CameraX: Para la captura de fotos y videos.
•	Material Design 3: Para componentes de interfaz de usuario modernos.
•	Coil: Para cargar y mostrar imágenes de forma eficiente.
•	Accompanist Permissions: Para manejar los permisos (aunque se recomienda migrar al nuevo API de permisos de Compose).
•	MediaStore API: Para guardar y acceder a archivos multimedia en el almacenamiento público.

Explicación del Código
MainActivity.kt
Esta es la actividad principal de la aplicación. Configura el tema y establece el contenido inicial de la aplicación.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Parcial3AndroidTheme {
                // Lista de permisos necesarios
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

                // Componente para manejar permisos
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


Puntos Clave:
•	Gestión de Permisos: Determina qué permisos solicitar según la versión de Android.
•	Inicia MainScreen: Una vez que se han otorgado los permisos, se muestra la pantalla principal.
Permissions.kt
Este archivo contiene el componente que maneja los permisos necesarios para la aplicación.
@Composable
fun Permission(
    permissions: List<String>,
    rationaleText: String,
    content: @Composable () -> Unit
) {
    // Estado y lógica para manejar los permisos
    // ...
}
Puntos Clave:
•	Uso de rememberLauncherForActivityResult: Maneja las solicitudes de permisos utilizando el API oficial de permisos de Compose.
•	Manejo de Diferentes Estados: Gestiona los casos cuando los permisos son concedidos, denegados o denegados permanentemente.
•	Interfaz de Usuario Amigable: Proporciona mensajes claros al usuario y opciones para habilitar los permisos desde la configuración si es necesario.




VideoCaptureUI.kt
Este componente se encarga de la captura de video utilizando CameraX.
@Composable
fun VideoCaptureUI(
    onVideoRecorded: (Uri) -> Unit,
    onError: (Exception) -> Unit
) {
    // Variables y estados necesarios
    // ...

    // Configuración de CameraX y Recorder
    // ...

    // Interfaz de usuario para mostrar la vista previa y el botón de grabación
    // ...
}
Puntos Clave:
•	Configuración de CameraX: Inicializa y configura CameraX para la captura de video.
•	Grabación con Audio: Verifica si se tiene el permiso de audio antes de habilitarlo.
•	Almacenamiento en MediaStore: Guarda los videos en el almacenamiento público utilizando MediaStore, lo que permite que sean accesibles para otras aplicaciones.
•	Manejo de Estados: Controla el estado de la grabación para evitar errores y proporcionar una experiencia fluida al usuario.


GalleryScreen.kt
Esta pantalla muestra una lista de imágenes y videos capturados.
@Composable
fun GalleryScreen() {
    // Obtiene y muestra las imágenes y videos del MediaStore
    // ...
}
Puntos Clave:
•	Acceso al MediaStore: Obtiene los URIs de las imágenes y videos almacenados en el dispositivo.
•	Visualización de Contenido: Utiliza LazyColumn para mostrar una lista de imágenes y videos.
MainScreen.kt
Controla la navegación entre las diferentes pantallas de la aplicación.
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
            composable("gallery") { GalleryScreen() }
        }
    }
}
Puntos Clave:
•	Navegación con NavHost: Define las rutas y composables asociados para la navegación.
•	Barra de Navegación Inferior: Proporciona acceso rápido a las diferentes secciones de la aplicación.
•	Uso de APIs Experimentales: Hace uso de componentes de Material3 que son experimentales, optando por su uso con @OptIn.
________________________________________
Cómo Ejecutar la Aplicación
1.	Clonar el Repositorio: Descarga o clona el repositorio en tu máquina local.
2.	Abrir en Android Studio: Abre el proyecto en Android Studio Electric Eel o superior para garantizar la compatibilidad con Jetpack Compose.
3.	Configurar el SDK: Asegúrate de tener instalado el SDK de Android 13 (API 33) o la versión objetivo de la aplicación.
4.	Sincronizar el Proyecto: Ejecuta una sincronización de Gradle para asegurar que todas las dependencias estén correctamente descargadas.
5.	Ejecutar en un Dispositivo o Emulador:
o	Dispositivo Físico: Conecta tu dispositivo Android y habilita el modo de desarrollador y la depuración USB.
o	Emulador: Configura un emulador con una versión de Android compatible.
6.	Compilar y Ejecutar: Haz clic en el botón de "Ejecutar" en Android Studio para compilar y desplegar la aplicación.
Conclusión
Este proyecto demuestra cómo utilizar CameraX y Jetpack Compose para crear una aplicación moderna de captura de fotos y videos en Android. Al hacer uso de las últimas APIs y prácticas recomendadas, se garantiza una aplicación robusta y compatible con las versiones más recientes de Android.
________________________________________
Notas Adicionales:
•	Migración de Permisos: Se recomienda migrar al API oficial de manejo de permisos en Jetpack Compose para asegurar compatibilidad futura y soporte.
•	Compatibilidad con Diferentes Versiones de Android: El manejo condicional de permisos y funciones asegura que la aplicación funcione correctamente en una amplia gama de dispositivos.
•	Contribuciones y Mejoras: Si deseas contribuir al proyecto o tienes sugerencias de mejora, siéntete libre de hacerlo.
Contacto:
•	Desarrollador: Josue Emanuel Hernandez Ortega
•	Correo Electrónico: josue30759@gmail.com
