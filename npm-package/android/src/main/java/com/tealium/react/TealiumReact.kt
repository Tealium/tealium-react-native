package com.tealium.react

import android.app.Application
import android.util.Log
import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import com.tealium.core.*
import com.tealium.core.consent.*
import com.tealium.react.BuildConfig.TAG
import com.tealium.remotecommanddispatcher.remoteCommands
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class TealiumReactNative : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(TealiumReact(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}

class TealiumReact(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = MODULE_NAME
    private var tealium: Tealium? = null

    @ReactMethod
    fun initialize(configMap: ReadableMap) {
        getApplication()?.let { app ->
            configMap.toTealiumConfig(app)?.let { config ->
                tealium = Tealium.create(INSTANCE_NAME, config) {
                    Log.d(TAG, "Instance Initialized: ${this.key}")
                    events.subscribe(EmitterListeners(reactContext))
                }
            }
        } ?: run {
            Log.w(TAG, "Failed to initialize instance.")
        }
    }

    private fun getApplication(): Application? {
        var app: Application? = null
        // ReactApplicationContext only holds a weak reference to the Current Activity so we need to
        // handle the case where it is null properly to avoid an unhandled exception.
        try {
            app = reactApplicationContext.getCurrentActivity()?.getApplication()
                    ?: currentActivity?.getApplication()
                            ?: reactApplicationContext.getApplicationContext() as Application
        } catch (ex: NullPointerException) {
            Log.d(TAG, "getApplication: method called on null object. ", ex)
        } catch (ex: ClassCastException) {
            Log.d(TAG, "getApplication: failed to cast to Application. ", ex)
        }
        return app
    }

    @ReactMethod
    fun terminateInstance() {
        Tealium.destroy(INSTANCE_NAME)
    }

    @ReactMethod
    fun track(data: ReadableMap) {
        dispatchFromMap(data).let {
            tealium?.track(it)
        }
    }

    @ReactMethod
    fun addToDataLayer(data: ReadableMap, expiryString: String) {
        tealium?.apply {
            data.entryIterator.forEach { mutableEntry ->
                mutableEntry.key?.let { key ->
                    mutableEntry.value?.let { value ->
                        val expiry = expiryFromString(expiryString)
                        when (value) {
                            is String -> dataLayer.putString(key, value, expiry)
                            is Int -> dataLayer.putInt(key, value, expiry)
                            is Long -> dataLayer.putLong(key, value, expiry)
                            is Double -> dataLayer.putDouble(key, value, expiry)
                            is Boolean -> dataLayer.putBoolean(key, value, expiry)
                            is ReadableArray -> {
                                if (value.size() <= 0) return

                                if (value.isSingleType()) {
                                    val type = value.getType(0)
                                    when (type) {
                                        ReadableType.Boolean -> dataLayer.putBooleanArray(key, value.toTyped(), expiry)
                                        ReadableType.String -> dataLayer.putStringArray(key, value.toTyped(), expiry)
                                        ReadableType.Number -> dataLayer.putDoubleArray(key, value.toTyped(), expiry)
                                        ReadableType.Null -> {
                                            // don't save nulls.
                                        }
                                        // Maps/Arrays will be serialized to JSON Strings
                                        else -> dataLayer.putString(key, value.toJSONArray().toString(), expiry)
                                    }
                                } else {
                                    // Mixed Arrays will be serialized to JSON Strings
                                    dataLayer.putString(key, value.toJSONArray().toString(), expiry)
                                }
                            }
                            is ReadableMap -> {
                                dataLayer.putJsonObject(key, value.toJSONObject(), expiry)
                            }
                        }
                    }
                }
            }
        }
    }

    @ReactMethod
    fun getFromDataLayer(key: String, callback: Callback) {
        tealium?.dataLayer?.get(key)?.let {
            val payload = when (it) {
                is Array<*> -> JSONArray(it).toWritableArray()
                is JSONObject -> it.toWritableMap()
                is String -> {
                    try {
                        // Mixed Arrays and Arrays of Arrays/Objects are serialized to string.
                        // check if we need to deserialize it back here, else return the String value
                        if (it.startsWith("[") && it.endsWith("]")) {
                            JSONArray(it).toWritableArray()
                        } else it
                    } catch (jex: JSONException) {
                       it
                    }
                }
                else -> it
            }
            callback.invoke(payload)
        }
    }

    @ReactMethod
    fun removeFromDataLayer(keys: ReadableArray) {
        tealium?.apply {
            keys.toArrayList().forEach {
                dataLayer.remove(it.toString())
            }
        }
    }

    @ReactMethod
    fun setConsentStatus(status: String) {
        tealium?.apply {
            consentManager.userConsentStatus = ConsentStatus.consentStatus(status)
        }
    }

    @ReactMethod
    fun getConsentStatus(callback: Callback) {
        callback(tealium?.consentManager?.userConsentStatus?.value
                ?: ConsentStatus.UNKNOWN.value)
    }

    @ReactMethod
    fun setConsentCategories(categories: ReadableArray) {
        tealium?.apply {
            val categoryStrings: List<String> = categories.toArrayList().map { it.toString() }
            consentManager.userConsentCategories = ConsentCategory.consentCategories(categoryStrings.toSet())
        }
    }

    @ReactMethod
    fun getConsentCategories(callback: Callback) {
        val writableArray = Arguments.createArray()
        tealium?.consentManager?.userConsentCategories?.forEach {
            writableArray.pushString(it.value)
        }
        callback(writableArray)
    }

    @ReactMethod
    fun addRemoteCommand(id: String) {
        Log.d("tealium-react", "addRemoteCommand: $id")
        tealium?.remoteCommands?.add(RemoteCommandListener(reactContext, id))
    }

    @ReactMethod
    fun removeRemoteCommand(id: String) {
        Log.d("tealium-react", "removeRemoteCommand: $id")
        tealium?.remoteCommands?.remove(id)
    }

    @ReactMethod
    fun joinTrace(id: String) {
        tealium?.joinTrace(id)
    }

    @ReactMethod
    fun leaveTrace() {
        tealium?.leaveTrace()
    }

    @ReactMethod
    fun getVisitorId(callback: Callback) {
        callback.invoke(tealium?.visitorId ?: "")
    }
}