package com.tealiumreactadobevisitor

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

    private var _adobe_org_id: String? = null
    private var _adobe_existing_ecid: String? = null
    private var _adobe_retries: Int? = null
    private var _adobe_auth_state: Int? = null
    private var _adobe_data_provider_id: String? = null
    private var _adobe_custom_visitor_id: String? = null

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    override fun configure(config: TealiumConfig) {
        // add the module reference
        config.collectors.add(Collectors.AdobeVisitor)

        _adobe_org_id?.let {
            config.adobeVisitorOrgId = it
        }

        _adobe_existing_ecid?.let {
            config.existingVisitorId = it
        }

        _adobe_retries?.let {
            config.adobeVisitorRetries = it
        }

        _adobe_auth_state?.let {
            config.adobeVisitorAuthState = it
        }

        _adobe_data_provider_id?.let {
            config.adobeVisitorDataProviderId = it
        }

        _adobe_custom_visitor_id?.let {
            config.adobeVisitorCustomVisitorId = it
        }
    }

    @ReactMethod
    fun configure(config: ReadableMap?) {
        config?.let {
            it.safeGetString(KEY_ADOBE_VISITOR_ORG_ID)?.let {
                setOrgId(it)
            }

            it.safeGetString(KEY_ADOBE_VISITOR_EXISTING_ECID)?.let {
                setExistingEcid(it)
            }

            it.safeGetInt(KEY_ADOBE_VISITOR_RETRIES)?.let {
                setRetries(it)
            }

            it.safeGetInt(KEY_ADOBE_VISITOR_AUTH_STATE)?.let {
                setAuthState(it)
            }

            it.safeGetString(KEY_ADOBE_VISITOR_DATA_PROVIDER_ID)?.let {
                setDataProviderId(it)
            }

            it.safeGetString(KEY_ADOBE_VISITOR_CUSTOM_VISITOR_ID)?.let {
                setCustomVisitorId(it)
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
        callback: Callback
    ) {
        Tealium[INSTANCE_NAME]?.adobeVisitorApi?.linkEcidToKnownIdentifier(
            knownId,
            adobeDataProviderId,
            if (authState == -1) null else authState,
            object : ResponseListener<AdobeVisitor> {
                override fun failure(errorCode: Int, ex: Exception?) {
                    callback.invoke("Failed to link existing Ecid with error code: $errorCode and exception ${ex?.message}")
                }

                override fun success(data: AdobeVisitor) {
                    callback.invoke(data.toReadableMap())
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

    @ReactMethod
    fun setOrgId(orgId: String) {
        _adobe_org_id = orgId
    }

    @ReactMethod
    fun setExistingEcid(ecid: String) {
        _adobe_existing_ecid = ecid
    }

    @ReactMethod
    fun setRetries(retries: Int) {
        _adobe_retries = retries
    }

    @ReactMethod
    fun setAuthState(state: Int) {
        _adobe_auth_state = state
    }

    @ReactMethod
    fun setDataProviderId(dataProviderId: String) {
        _adobe_data_provider_id = dataProviderId
    }

    @ReactMethod
    fun setCustomVisitorId(customId: String) {
        _adobe_custom_visitor_id = customId
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