package com.tealium.react.momentsapi

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.uimanager.ViewManager
import com.tealium.core.Modules
import com.tealium.core.Tealium
import com.tealium.core.TealiumConfig
import com.tealium.momentsapi.EngineResponse
import com.tealium.momentsapi.ErrorCode
import com.tealium.momentsapi.MomentsApi
import com.tealium.momentsapi.ResponseListener
import com.tealium.momentsapi.momentsApi
import com.tealium.momentsapi.momentsApiReferrer
import com.tealium.momentsapi.momentsApiRegion
import com.tealium.react.INSTANCE_NAME
import com.tealium.react.OptionalModule
import com.tealium.react.TealiumReact
import com.tealium.react.safeGetString

class TealiumReactNativeMomentsApiPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(TealiumReactMomentsApi(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class TealiumReactMomentsApi(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), OptionalModule {

    private var momentsApiRegion: String? = null
    private var momentsApiReferrer: String? = null

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        config.modules.add(Modules.MomentsApi)

        momentsApiRegion?.let {
            config.momentsApiRegion = regionFromString(it)
        }

        momentsApiReferrer?.let {
            config.momentsApiReferrer = it
        }
    }

    @ReactMethod
    fun configure(config: ReadableMap?) {
        config?.let {
            it.safeGetString(KEY_MOMENTS_API_REGION)?.let { region ->
                momentsApiRegion = region
            }

            it.safeGetString(KEY_MOMENTS_API_REFERRER)?.let { referrer ->
                momentsApiReferrer = referrer
            }
        }
    }

    @ReactMethod
    fun fetchEngineResponse(engineId: String, callback: Callback) {
        Tealium[INSTANCE_NAME]?.momentsApi?.fetchEngineResponse(engineId, object : ResponseListener<EngineResponse> {
            override fun success(data: EngineResponse) {
                callback.invoke(data.toWritableMap())
            }

            override fun failure(errorCode: ErrorCode, message: String) {
                callback.invoke("Failed to fetch engine response with error code: $errorCode - $message")
            }
        })

    }

    override fun getName(): String {
        return NAME
    }

    companion object {
        const val NAME = "TealiumReactMomentsApi"

        private const val KEY_MOMENTS_API_REGION = "region"
        private const val KEY_MOMENTS_API_REFERRER = "referrer"
    }
}
