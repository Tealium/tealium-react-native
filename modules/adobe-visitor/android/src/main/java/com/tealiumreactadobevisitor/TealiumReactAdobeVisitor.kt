package com.tealiumreactadobevisitor

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.*
import com.facebook.react.uimanager.ViewManager

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
    private var _adobe_auth_state: String? = null
    private var _adobe_data_provider_id: String? = null
    private var _adobe_custom_visitor_id: String? = null

    override fun initialize() {
        super.initialize()
        reactContext.getNativeModule(TealiumReact::class.java)?.registerOptionalModule(this)
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
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

            it.safeGetString(KEY_ADOBE_VISITOR_AUTH_STATE)?.let {
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
    fun linkExistingEcidToKnownIdentifier() {
        Tealium[INSTANCE_NAME]?.adobeVisitor?.linkExistingEcidToKnownIdentifier()
    }

    @ReactMethod
    fun resetVisitor() {
        Tealium[INSTANCE_NAME]?.adobeVisitor?.resetVisitor()
    }

    @ReactMethod
    fun decorateUrl() {
        Tealium[INSTANCE_NAME]?.adobeVisitor?.decorateUrl()
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
    fun setAuthState(state: String) {
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
