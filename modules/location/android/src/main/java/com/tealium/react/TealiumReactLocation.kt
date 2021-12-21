package com.tealium.react

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import com.tealium.core.*
import com.tealium.location.Location
import com.tealium.location.geofenceFilename
import com.tealium.location.location
import com.tealium.location.overrideGeofenceUrl


class TealiumReactNativeLocation : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(TealiumReactLocation(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}

class TealiumReactLocation(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext),
    OptionalModule {

    private var _geofenceFile: String? = null
    private var _geofenceUrl: String? = null
    private var _highAccuracy: Boolean = false
    private var _interval: Int = 60000

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    @ReactMethod
    fun configure(config: ReadableMap?) {
        config?.let {
            it.safeGetString(KEY_GEOFENCE_URL)?.let { url ->
                setGeofenceUrl(url)
            }

            it.safeGetString(KEY_GEOFENCE_FILE)?.let { path ->
                setGeofenceFile(path)
            }

            it.safeGetString(KEY_ACCURACY)?.let { acc ->
                setAccuracyString(acc)
            } ?: it.safeGetBoolean(KEY_ACCURACY)?.let { acc ->
                setAccuracyBoolean(acc)
            }

            it.safeGetInt(KEY_INTERVAL)?.let { interval ->
                setInterval(interval)
            }
        }
    }

    @ReactMethod
    fun setGeofenceFile(path: String) {
        _geofenceFile = path
    }

    @ReactMethod
    fun setGeofenceUrl(url: String) {
        _geofenceUrl = url
    }

    @ReactMethod
    fun setAccuracyBoolean(accuracy: Boolean) {
        _highAccuracy = accuracy
    }

    @ReactMethod
    fun setAccuracyString(accuracy: String) {
        when(accuracy) {
            "high" -> _highAccuracy = true
            "low" -> _highAccuracy = false
        }
    }

    @ReactMethod
    fun setInterval(interval: Int) {
        _interval = interval
    }

    // Runtime Methods

    @ReactMethod
    fun lastLocation(callback: Callback?) {
        Tealium[INSTANCE_NAME]?.location?.lastLocation()?.let {
            val map = Arguments.createMap()
            map.putDouble(KEY_LATITUDE, it.latitude)
            map.putDouble(KEY_LONGITUDE, it.longitude)
            callback?.invoke(map)
        }
    }

    @ReactMethod
    fun startLocationTracking() {
        Tealium[INSTANCE_NAME]?.location?.startLocationTracking(_highAccuracy, _interval)
    }

    @ReactMethod
    fun stopLocationTracking() {
        Tealium[INSTANCE_NAME]?.location?.stopLocationTracking()
    }

    override fun configure(config: TealiumConfig) {
        // add the module reference
        config.collectors.add(Collectors.Location)

        _geofenceFile?.let {
            config.geofenceFilename = it
        }
        _geofenceUrl?.let {
            config.overrideGeofenceUrl = it
        }
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        const val MODULE_NAME: String = "TealiumReactLocation"

        private const val KEY_GEOFENCE_URL = "geofenceUrl"
        private const val KEY_GEOFENCE_FILE = "geofenceFile"
        private const val KEY_ACCURACY = "accuracy"
        private const val KEY_INTERVAL = "interval"

        private const val KEY_LATITUDE = "lat"
        private const val KEY_LONGITUDE = "lng"
    }
}