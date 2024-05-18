package com.base.app.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureRequest
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.base.app.R
import com.base.app.databinding.ActivityCameraApiactivityBinding


class CameraAPIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraApiactivityBinding
    val PERMISSION_CODE = 101
    private lateinit var cameraManager: CameraManager
    private lateinit var capReq: CaptureRequest.Builder
    private lateinit var handlerThread: HandlerThread
    private lateinit var handler: Handler
    private lateinit var cameraCaptureSession: CameraCaptureSession
    private lateinit var cameraDevice: CameraDevice
    private lateinit var captureRequest: CaptureRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_apiactivity)

        getCameraPermission()
        with(binding) {
            cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
            handlerThread = HandlerThread("videoThread")
            handlerThread.start()
            handler = Handler((handlerThread).looper)

            textureCameraView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    openCamera()
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                }

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                    return false
                }

                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun openCamera() {
        cameraManager.openCamera(
            cameraManager.cameraIdList[0],
            object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    capReq = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    val surface = Surface(binding.textureCameraView.surfaceTexture)
                    capReq.addTarget(surface)

                    cameraDevice.createCaptureSession(
                        listOf(surface),
                        object : CameraCaptureSession.StateCallback() {
                            override fun onConfigured(session: CameraCaptureSession) {
                                cameraCaptureSession = session
                                cameraCaptureSession.setRepeatingRequest(capReq.build(), null, null)
                            }

                            override fun onConfigureFailed(session: CameraCaptureSession) {
                            }

                        },
                        handler
                    )
                }

                override fun onDisconnected(camera: CameraDevice) {
                }

                override fun onError(camera: CameraDevice, error: Int) {
                }

            },
            handler
        )
    }

    private fun getCameraPermission() {
        val permission = mutableListOf<String>()
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permission.add(android.Manifest.permission.CAMERA)
        }
        if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permission.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permission.size > 0) {
            requestPermissions(permission.toTypedArray(), PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                getCameraPermission()
            }
        }
    }
}