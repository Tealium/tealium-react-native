package com.tealium.react.adobevisitor

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.uimanager.ViewManager
import com.tealium.adobe.api.AdobeVisitor
import com.tealium.adobe.api.ResponseListener
import com.tealium.adobe.api.UrlDecoratorHandler
import com.tealium.adobe.kotlin.*
import com.tealium.core.Collectors
import com.tealium.core.Tealium
import com.tealium.core.TealiumConfig
import com.tealium.react.*
import java.net.URL

class TealiumReactNativeAdobeVisitor : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf(TealiumReactAdobeVisitor(reactContext))
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}

class TealiumReactAdobeVisitor(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext),
    OptionalModule {

    private var _adobeOrgId: String? = null
    private var _adobeExistingEcid: String? = null
    private var _adobeRetries: Int? = null
    private var _adobeAuthState: Int? = null
    private var _adobeDataProviderId: String? = null
    private var _adobeCustomVisitorId: String? = null

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        // add the module reference
        config.collectors.add(Collectors.AdobeVisitor)

        _adobeOrgId?.let {
            config.adobeVisitorOrgId = it
        }

        _adobeExistingEcid?.let {
            config.existingVisitorId = it
        }

        _adobeRetries?.let {
            config.adobeVisitorRetries = it
        }

        _adobeAuthState?.let {
            config.adobeVisitorAuthState = it
        }

        _adobeDataProviderId?.let {
            config.adobeVisitorDataProviderId = it
        }

        _adobeCustomVisitorId?.let {
            config.adobeVisitorCustomVisitorId = it
        }
    }

    @ReactMethod
    fun configure(config: ReadableMap?) {
        config?.let {
            it.safeGetString(KEY_ADOBE_VISITOR_ORG_ID)?.let { orgId ->
                _adobeOrgId = orgId
            }

            it.safeGetString(KEY_ADOBE_VISITOR_EXISTING_ECID)?.let { ecid ->
                _adobeExistingEcid = ecid
            }

            it.safeGetInt(KEY_ADOBE_VISITOR_RETRIES)?.let { retries ->
                _adobeRetries = retries
            }

            it.safeGetInt(KEY_ADOBE_VISITOR_AUTH_STATE)?.let { state ->
                _adobeAuthState = state
            }

            it.safeGetString(KEY_ADOBE_VISITOR_DATA_PROVIDER_ID)?.let { dataProviderId ->
                _adobeDataProviderId = dataProviderId
            }

            it.safeGetString(KEY_ADOBE_VISITOR_CUSTOM_VISITOR_ID)?.let { customId ->
                _adobeCustomVisitorId = customId
            }
        }
    }

    @ReactMethod
    fun getCurrentAdobeVisitor(callback: Callback) {
        callback.invoke(Tealium[INSTANCE_NAME]?.adobeVisitorApi?.visitor?.toReadableMap())
    }

    @ReactMethod
    fun linkEcidToKnownIdentifier(
        knownId: String,
        adobeDataProviderId: String,
        authState: Int,
        callback: Callback?
    ) {
        Tealium[INSTANCE_NAME]?.adobeVisitorApi?.linkEcidToKnownIdentifier(
            knownId,
            adobeDataProviderId,
            authState,
            object : ResponseListener<AdobeVisitor> {
                override fun failure(errorCode: Int, ex: Exception?) {
                    callback?.invoke("Failed to link existing Ecid with error code: $errorCode and exception ${ex?.message}")
                }

                override fun success(data: AdobeVisitor) {
                    callback?.invoke(data.toReadableMap())
                }
            }
        )
    }

    @ReactMethod
    fun resetVisitor() {
        Tealium[INSTANCE_NAME]?.adobeVisitorApi?.resetVisitor()
    }

    @ReactMethod
    fun decorateUrl(url: String, callback: Callback) {
        Tealium[INSTANCE_NAME]?.adobeVisitorApi?.decorateUrl(
            URL(url),
            object : UrlDecoratorHandler {
                override fun onDecorateUrl(url: URL) {
                    callback.invoke(url.toString())
                }
            }
        )
    }

    override fun getName(): String {
        return MODULE_NAME
    }

    companion object {
        const val MODULE_NAME = "TealiumReactAdobeVisitor"

        private const val KEY_ADOBE_VISITOR_ORG_ID = "adobeVisitorOrgId"
        private const val KEY_ADOBE_VISITOR_EXISTING_ECID = "adobeVisitorExistingEcid"
        private const val KEY_ADOBE_VISITOR_RETRIES = "adobeVisitorRetries"
        private const val KEY_ADOBE_VISITOR_AUTH_STATE = "adobeVisitorAuthState"
        private const val KEY_ADOBE_VISITOR_DATA_PROVIDER_ID = "adobeVisitorDataProviderId"
        private const val KEY_ADOBE_VISITOR_CUSTOM_VISITOR_ID = "adobeVisitorCustomVisitorId"
    }
}