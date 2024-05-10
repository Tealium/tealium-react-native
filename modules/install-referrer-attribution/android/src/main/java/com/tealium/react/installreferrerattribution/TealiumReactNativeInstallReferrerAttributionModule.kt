package com.tealium.react.installreferrerattribution

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.ViewManager
import com.tealium.adidentifier.AdIdentifier
import com.tealium.adidentifier.adIdentifier
import com.tealium.core.Modules
import com.tealium.core.Tealium
import com.tealium.core.TealiumConfig
import com.tealium.installreferrer.InstallReferrer
import com.tealium.react.INSTANCE_NAME
import com.tealium.react.OptionalModule
import com.tealium.react.TealiumReact

class TealiumReactNativeInstallReferrerAttributionPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(TealiumReactNativeInstallReferrerAttributionModule(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class TealiumReactNativeInstallReferrerAttributionModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), OptionalModule {

    @ReactMethod
    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        config.modules.add(Modules.InstallReferrer)
        config.modules.add(Modules.AdIdentifier)
    }

    @ReactMethod
    fun removeAdInfo() {
        Tealium[INSTANCE_NAME]?.adIdentifier?.removeAdInfo()
    }

    @ReactMethod
    fun removeAppSetIdInfo() {
        Tealium[INSTANCE_NAME]?.adIdentifier?.removeAppSetIdInfo()
    }

    override fun getName(): String {
        return NAME
    }

    companion object {
        const val NAME = "TealiumReactInstallReferrerAttribution"
    }
}
