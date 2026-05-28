package cat.copernic.easytrazamobile.ui.albarans

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import cat.copernic.easytrazamobile.R
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

/**
 * Camera screen that captures a delivery-note image and extracts text with ML Kit OCR.
 *
 * The capture is configured at maximum JPEG quality and the OCR is executed twice:
 * first using the original image and then using a contrast-enhanced grayscale bitmap.
 * The longest useful result is returned, which normally improves delivery notes with
 * weak contrast, shadows or small printed text.
 */
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

    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
            .setJpegQuality(100)
            .build()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EasyBackgroundSoft)
    ) {
        if (hasCameraPermission) {
            CameraPreview(
                imageCapture = imageCapture,
                lifecycleOwner = lifecycleOwner,
                modifier = Modifier.fillMaxSize()
            )

            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = EasySurface),
                border = BorderStroke(1.dp, EasyBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.ocr_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EasyTextStrong
                    )
                    Text(
                        text = stringResource(R.string.ocr_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = EasyText
                    )

                    EasyPrimaryButton(
                        text = stringResource(R.string.ocr_take_photo),
                        onClick = {
                            captureAndReadText(
                                context = context,
                                imageCapture = imageCapture,
                                onOcrResult = onOcrResult
                            )
                        },
                        modifier = Modifier.height(54.dp)
                    )

                    EasySecondaryButton(
                        text = stringResource(R.string.common_back),
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
                    text = stringResource(R.string.ocr_permission_required),
                    style = MaterialTheme.typography.bodyLarge,
                    color = EasyText
                )

                EasyPrimaryButton(
                    text = stringResource(R.string.ocr_grant_permission),
                    onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }
                )
            }
        }
    }
}

/** Hosts the CameraX preview inside Compose. */
@Composable
private fun CameraPreview(
    imageCapture: ImageCapture,
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
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
}

/** Captures an image and returns the best recognized OCR text. */
private fun captureAndReadText(
    context: android.content.Context,
    imageCapture: ImageCapture,
    onOcrResult: (String) -> Unit
) {
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
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                recognizeBestText(
                    context = context,
                    photoFile = photoFile,
                    onResult = onOcrResult
                )
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                onOcrResult("")
            }
        }
    )
}

/** Runs OCR over the original and enhanced versions and returns the strongest result. */
private fun recognizeBestText(
    context: android.content.Context,
    photoFile: File,
    onResult: (String) -> Unit
) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val originalImage = InputImage.fromFilePath(context, Uri.fromFile(photoFile))

    recognizer.process(originalImage)
        .addOnSuccessListener { originalText ->
            val enhancedBitmap = createEnhancedBitmap(photoFile)

            if (enhancedBitmap == null) {
                onResult(originalText.text)
                return@addOnSuccessListener
            }

            val enhancedImage = InputImage.fromBitmap(enhancedBitmap, 0)
            recognizer.process(enhancedImage)
                .addOnSuccessListener { enhancedText ->
                    onResult(selectBestOcrText(originalText.text, enhancedText.text))
                    enhancedBitmap.recycle()
                }
                .addOnFailureListener {
                    onResult(originalText.text)
                    enhancedBitmap.recycle()
                }
        }
        .addOnFailureListener {
            onResult("")
        }
}

/** Creates a grayscale, high-contrast bitmap to help OCR detect low quality text. */
private fun createEnhancedBitmap(photoFile: File): Bitmap? {
    val decodedBitmap = BitmapFactory.decodeFile(photoFile.absolutePath) ?: return null
    val rotatedBitmap = rotateBitmapIfNeeded(decodedBitmap, photoFile)
    val scaledBitmap = scaleBitmapForOcr(rotatedBitmap)

    if (rotatedBitmap !== decodedBitmap) {
        decodedBitmap.recycle()
    }
    if (scaledBitmap !== rotatedBitmap) {
        rotatedBitmap.recycle()
    }

    val output = Bitmap.createBitmap(
        scaledBitmap.width,
        scaledBitmap.height,
        Bitmap.Config.ARGB_8888
    )

    val colorMatrix = ColorMatrix().apply {
        setSaturation(0f)
        postConcat(
            ColorMatrix(
                floatArrayOf(
                    1.45f, 0f, 0f, 0f, -35f,
                    0f, 1.45f, 0f, 0f, -35f,
                    0f, 0f, 1.45f, 0f, -35f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        )
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        colorFilter = ColorMatrixColorFilter(colorMatrix)
        isFilterBitmap = true
    }

    Canvas(output).drawBitmap(scaledBitmap, 0f, 0f, paint)
    scaledBitmap.recycle()

    return output
}

/** Rotates the bitmap according to the EXIF orientation saved by CameraX. */
private fun rotateBitmapIfNeeded(bitmap: Bitmap, photoFile: File): Bitmap {
    val orientation = ExifInterface(photoFile.absolutePath)
        .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

    val degrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90f
        ExifInterface.ORIENTATION_ROTATE_180 -> 180f
        ExifInterface.ORIENTATION_ROTATE_270 -> 270f
        else -> 0f
    }

    if (degrees == 0f) return bitmap

    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

/** Upscales small photos because ML Kit usually reads printed text better at larger sizes. */
private fun scaleBitmapForOcr(bitmap: Bitmap): Bitmap {
    val minSide = minOf(bitmap.width, bitmap.height)

    if (minSide >= 1600) return bitmap

    val scale = 1600f / minSide
    val width = (bitmap.width * scale).toInt()
    val height = (bitmap.height * scale).toInt()

    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

/** Chooses the OCR result that contains more usable words. */
private fun selectBestOcrText(originalText: String, enhancedText: String): String {
    val originalScore = scoreOcrText(originalText)
    val enhancedScore = scoreOcrText(enhancedText)

    return if (enhancedScore > originalScore) enhancedText else originalText
}

/** Scores OCR output by counting meaningful text tokens. */
private fun scoreOcrText(text: String): Int = text
    .split(Regex("\\s+"))
    .count { token -> token.length >= 2 && token.any { it.isLetterOrDigit() } }
