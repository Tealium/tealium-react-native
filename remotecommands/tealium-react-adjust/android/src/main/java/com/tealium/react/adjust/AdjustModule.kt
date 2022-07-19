package com.tealium.react.adjust

import android.app.Application
import com.adjust.sdk.AdjustConfig
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager
import com.tealium.react.RemoteCommandFactory
import com.tealium.react.TealiumReact
import com.tealium.react.safeGetString
import com.tealium.remotecommands.RemoteCommand
import com.tealium.remotecommands.adjust.AdjustRemoteCommand

open class AdjustPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(AdjustModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class AdjustModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    @ReactMethod
    fun initialize(config: ReadableMap?) {
        reactContext.getNativeModule(TealiumReact::class.java)
            ?.registerRemoteCommandFactory(AdjustRemoteCommandFactory(config?.toAdjustConfig()))
    }

    private fun ReadableMap.toAdjustConfig(): AdjustConfig {
        val appToken = getString(KEY_APP_TOKEN)
        val environment =
            if (safeGetString(KEY_ENVIRONMENT) == ENVIRONMENT_PRODUCTION_VALUE) AdjustConfig.ENVIRONMENT_PRODUCTION else AdjustConfig.ENVIRONMENT_SANDBOX
        val allowSuppressLogLevel = getBoolean(KEY_ALLOW_SUPPRESS_LOG_LEVEL)

        return AdjustConfig(
            reactContext.applicationContext,
            appToken,
            environment,
            allowSuppressLogLevel
        )
    }

    private inner class AdjustRemoteCommandFactory(config: AdjustConfig? = null) :
        RemoteCommandFactory {
        override val name = "AdjustRemoteCommand"
        private val adjustRemoteCommand = config?.let {
            AdjustRemoteCommand(
                reactContext.applicationContext as Application,
                config
            )
        } ?: AdjustRemoteCommand(reactContext.applicationContext as Application)

        override fun create(): RemoteCommand {
            return adjustRemoteCommand
        }
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        private const val MODULE_NAME = "TealiumReactAdjust"
        const val KEY_APP_TOKEN = "appToken"
        const val KEY_ENVIRONMENT = "environment"
        const val KEY_ALLOW_SUPPRESS_LOG_LEVEL = "allowSuppressLogLevel"

        const val ENVIRONMENT_PRODUCTION_VALUE = "production"
    }
}