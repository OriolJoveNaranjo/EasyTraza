package cat.copernic.easytrazamobile.ui.albarans

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import cat.copernic.easytrazamobile.ui.components.EasyPrimaryButton
import cat.copernic.easytrazamobile.ui.components.EasySecondaryButton
import cat.copernic.easytrazamobile.ui.theme.EasyBackgroundSoft
import cat.copernic.easytrazamobile.ui.theme.EasyBorder
import cat.copernic.easytrazamobile.ui.theme.EasySurface
import cat.copernic.easytrazamobile.ui.theme.EasyText
import cat.copernic.easytrazamobile.ui.theme.EasyTextStrong
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File

@Composable
fun OcrCameraScreen(
    onOcrResult: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    val imageCapture = remember { ImageCapture.Builder().build() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EasyBackgroundSoft)
    ) {
        if (hasCameraPermission) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                }
            )

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = EasySurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, EasyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Escaneig OCR",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EasyTextStrong
                    )
                    Text(
                        text = "Enfoca l'albarà i fes la foto quan el text sigui visible.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = EasyText
                    )

                    EasyPrimaryButton(
                        text = "Fer foto i llegir text",
                        onClick = {
                            val photoFile = File(
                                context.cacheDir,
                                "albara_ocr_${System.currentTimeMillis()}.jpg"
                            )
                            val outputOptions = ImageCapture.OutputFileOptions
                                .Builder(photoFile)
                                .build()

                            imageCapture.takePicture(
                                outputOptions,
                                ContextCompat.getMainExecutor(context),
                                object : ImageCapture.OnImageSavedCallback {
                                    override fun onImageSaved(
                                        outputFileResults: ImageCapture.OutputFileResults
                                    ) {
                                        val image = InputImage.fromFilePath(
                                            context,
                                            Uri.fromFile(photoFile)
                                        )
                                        val recognizer = TextRecognition.getClient(
                                            TextRecognizerOptions.DEFAULT_OPTIONS
                                        )
                                        recognizer.process(image)
                                            .addOnSuccessListener { visionText ->
                                                onOcrResult(visionText.text)
                                            }
                                            .addOnFailureListener {
                                                onOcrResult("")
                                            }
                                    }

                                    override fun onError(exception: ImageCaptureException) {
                                        exception.printStackTrace()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.height(54.dp)
                    )

                    EasySecondaryButton(
                        text = "Tornar",
                        onClick = onBack,
                        modifier = Modifier.height(50.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Cal donar permís de càmera per escanejar l'albarà.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = EasyText
                )

                EasyPrimaryButton(
                    text = "Donar permís",
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                )
            }
        }
    }
}
