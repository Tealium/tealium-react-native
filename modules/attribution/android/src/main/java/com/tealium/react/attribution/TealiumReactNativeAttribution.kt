package com.tealium.react.attribution

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap
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
import com.tealium.react.safeGetBoolean

class TealiumReactNativeAttributionPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(TealiumReactAttribution(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class TealiumReactAttribution(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), OptionalModule {

    private var _installReferrerEnabled: Boolean? = null
    private var _adIdentifierEnabled: Boolean? = null

    @ReactMethod
    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        if (_installReferrerEnabled == true) {
            config.modules.add(Modules.InstallReferrer)
        }
        if (_adIdentifierEnabled == true) {
            config.modules.add(Modules.AdIdentifier)
        }
    }

    @ReactMethod
    fun configure(config: ReadableMap?) {
        config?.let {
            it.safeGetBoolean(KEY_INSTALL_REFERRER_ENABLED)?.let { installReferrerEnabled ->
                _installReferrerEnabled = installReferrerEnabled
            }

            it.safeGetBoolean(KEY_AD_IDENTIFIER_ENABLED)?.let { adIdEnabled ->
                _adIdentifierEnabled = adIdEnabled
            }
        }
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
        const val NAME = "TealiumReactAttribution"

        private const val KEY_INSTALL_REFERRER_ENABLED = "androidInstallReferrerEnabled"
        private const val KEY_AD_IDENTIFIER_ENABLED = "androidAdIdentifierEnabled"
    }
}
