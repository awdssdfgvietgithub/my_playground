package com.example.test.features.liveness

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.face.FaceLandmark
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.test.databinding.FragmentLivenessBinding
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import kotlin.math.abs

class LivenessFragment : Fragment() {
    private var _binding: FragmentLivenessBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageCapture: ImageCapture
    private var currentFacePose: String = "FRONT"
    private val cameraPermission = Manifest.permission.CAMERA

    // Set lưu các tư thế đã chụp
    private val capturedPoses = mutableSetOf<String>()

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Camera permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLivenessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(cameraPermission) // Yêu cầu quyền
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(
                        ContextCompat.getMainExecutor(requireContext()),
                        FaceAnalyzer(onFacePoseDetected = { facePose ->
                            if (!capturedPoses.contains(facePose) && capturedPoses.size < 3) {
                                capturedPoses.add(facePose)
                                captureImage(facePose)
                            }
                        }, onHeadEulerAngleDetected = { x, y, z ->
                            binding.textViewX.text = x.toString()
                            binding.textViewY.text = y.toString()
                            binding.textViewZ.text = z.toString()
                        }, onError = { errorMessage ->
                            binding.textViewError.text = errorMessage
                        })
                    )
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            cameraProvider.bindToLifecycle(
                viewLifecycleOwner,
                cameraSelector,
                preview,
                imageCapture,
                imageAnalysis
            )

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage(facePose: String) {
        val photoFile =
            File(
                requireContext().externalMediaDirs.first(),
                "${facePose}_${System.currentTimeMillis()}.jpg"
            )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Toast.makeText(
                        requireContext(),
                        "Captured: $facePose",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Nếu đã chụp đủ 3 tư thế thì dừng camera
                    if (capturedPoses.size == 3) {
                        stopCamera()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("FaceCapture", "Capture failed", exception)
                }
            })
    }

    private fun stopCamera() {
        cameraProviderFuture.get().unbindAll()
        Toast.makeText(requireContext(), "Captured all face poses", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class FaceAnalyzer(
    private val onFacePoseDetected: (String) -> Unit,
    private val onHeadEulerAngleDetected: (Float, Float, Float) -> Unit,
    private val onError: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .enableTracking()
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    )

    override fun analyze(imageProxy: ImageProxy) {
        @Suppress("UnsafeOptInUsageError")
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        val brightness = calculateBrightness(imageProxy)

        if (brightness < 120) {
            onError("Ánh sáng quá tối, vui lòng chụp ở nơi sáng hơn")
            imageProxy.close()
            return
        }

        detector.process(image)
            .addOnSuccessListener { faces ->
                if (faces.size > 1) {
                    onError("Chỉ một người được phép xuất hiện trong khung hình")
                    imageProxy.close()
                    return@addOnSuccessListener
                }

                if (faces.isNotEmpty()) {
                    val face = faces[0]
                    val headEulerAngleX = face.headEulerAngleX
                    val headEulerAngleY = face.headEulerAngleY
                    val headEulerAngleZ = face.headEulerAngleZ
                    onHeadEulerAngleDetected(headEulerAngleX, headEulerAngleY, headEulerAngleZ)

                    // region CHECK EYE
                    val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
                    val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
                    if (leftEye == null || rightEye == null) {
                        onError("Vui lòng tháo kính để tiếp tục")
                        imageProxy.close()
                        return@addOnSuccessListener
                    }

                    val leftEyeOpenProb = face.leftEyeOpenProbability ?: -1f
                    val rightEyeOpenProb = face.rightEyeOpenProbability ?: -1f
                    if (leftEyeOpenProb in 0.0..0.8 && rightEyeOpenProb in 0.0..0.8) {
                        onError("Vui lòng mở mắt để tiếp tục")
                        imageProxy.close()
                        return@addOnSuccessListener
                    }
                    // endregion

                    // region CHECK FACE OFFSET
                    val frameCenterX = imageProxy.cropRect.centerX()
                    val frameCenterY = imageProxy.cropRect.centerY()
                    val faceBounds = face.boundingBox
                    val faceCenterX = faceBounds.centerX()
                    val faceCenterY = faceBounds.centerY()

                    val tolerance = imageProxy.width / 4
                    if (abs(faceCenterX - frameCenterX) > tolerance || abs(faceCenterY - frameCenterY) > tolerance) {
                        onError("Vui lòng đưa mặt vào giữa màn hình")
                        imageProxy.close()
                        return@addOnSuccessListener
                    }
                    //endregion

                    // region CHECK FACE POSE
                    val newFacePose = when {
                        (headEulerAngleX in -5.0..5.0 && headEulerAngleY in -35.0..-30.0) -> FacePoseEnum.LEFT.name
                        (headEulerAngleX in -5.0..5.0 && headEulerAngleY in 30.0..35.0) -> FacePoseEnum.RIGHT.name
                        (headEulerAngleX in -5.0..5.0 && headEulerAngleY in -5.0..5.0) -> FacePoseEnum.FRONT.name
                        else -> FacePoseEnum.UNKNOWN.name
                    }
                    // endregion

                    Log.d("FaceAnalyzer", "Face pose: $newFacePose")
                    onError("")

                    /*// Chỉ gửi sự kiện khi thay đổi tư thế mặt
                    if (newFacePose != lastFacePose) {
                        lastFacePose = newFacePose
                        onFacePoseDetected(newFacePose)
                    }*/
                } else {
                    onError("Không phát hiện khuôn mặt, vui lòng kiểm tra lại")
                }
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun calculateBrightness(imageProxy: ImageProxy): Int {
        val buffer = imageProxy.planes[0].buffer
        val pixels = ByteArray(buffer.remaining())
        buffer.get(pixels)

        var totalLuminance = 0
        for (pixel in pixels) {
            totalLuminance += pixel.toInt() and 0xFF
        }
        return totalLuminance / pixels.size
    }
}

enum class FacePoseEnum {
    UNKNOWN,
    FRONT,
    LEFT,
    RIGHT
}