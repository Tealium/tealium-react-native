package com.tealium.react.crashreporter

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager
import com.tealium.core.Modules
import com.tealium.core.TealiumConfig
import com.tealium.crashreporter.CrashReporter
import com.tealium.crashreporter.truncateCrashReporterStackTraces
import com.tealium.react.OptionalModule
import com.tealium.react.TealiumReact

class TealiumReactCrashReporterPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return mutableListOf(TealiumReactCrashReporter(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}

class TealiumReactCrashReporter(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext),
    OptionalModule {

    private var _truncateStackTraces: Boolean = false

    @ReactMethod
    fun initialize(truncateStackTrace: Boolean) {
        _truncateStackTraces = truncateStackTrace
    }

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        config.modules.add(Modules.CrashReporter)
        config.truncateCrashReporterStackTraces = _truncateStackTraces
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        const val MODULE_NAME = "TealiumReactCrashReporter"
    }
}
