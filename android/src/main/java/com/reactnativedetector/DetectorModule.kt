package com.reactnativedetector

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import android.app.Activity

class DetectorModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext), ScreenshotDetectionListener {
    private var screenshotDetectionDelegate: ScreenshotDetectionDelegate? = null

    override fun getName(): String {
        return "Detector"
    }

    @ReactMethod
    fun startScreenshotDetection() {
        val currentActivity = reactContext.currentActivity
        if (currentActivity != null) {
            screenshotDetectionDelegate = ScreenshotDetectionDelegate(currentActivity, this)
            screenshotDetectionDelegate?.startScreenshotDetection()
        }
    }

    @ReactMethod
    fun stopScreenshotDetection() {
        screenshotDetectionDelegate?.stopScreenshotDetection()
        screenshotDetectionDelegate = null;
    }

    override fun onScreenCaptured(path: String) {
        reactContext
            .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
            .emit("UIApplicationUserDidTakeScreenshotNotification", null)
    }

    override fun onScreenCapturedWithDeniedPermission() {
        // Todo: send user notification.
    }
}
