package com.tealium.react

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.tealium.core.Logger
import com.tealium.core.consent.ConsentManagementPolicy
import com.tealium.core.consent.ConsentStatus
import com.tealium.core.consent.UserConsentPreferences
import com.tealium.core.messaging.UserConsentPreferencesUpdatedListener
import com.tealium.remotecommands.RemoteCommand
import com.tealium.visitorservice.VisitorProfile
import com.tealium.visitorservice.VisitorUpdatedListener
import org.json.JSONException

class EmitterListeners(private val reactContext: ReactApplicationContext) : VisitorUpdatedListener, UserConsentPreferencesUpdatedListener {

    override fun onVisitorUpdated(visitorProfile: VisitorProfile) {
        try {
            VisitorProfile.toFriendlyJson(visitorProfile).toWritableMap()?.let {
                reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit(EVENT_EMITTERS_VISITOR, it)
            }
        } catch (jex: JSONException) {
            Logger.qa(BuildConfig.TAG, "Error converting VisitorProfile to WritableMap.")
            Logger.qa(BuildConfig.TAG, "${jex.message}")
        }
    }

    override fun onUserConsentPreferencesUpdated(userConsentPreferences: UserConsentPreferences, policy: ConsentManagementPolicy) {
        if (userConsentPreferences.consentStatus != ConsentStatus.UNKNOWN) return

        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(EVENT_EMITTERS_CONSENT_EXPIRED, Arguments.createMap())
    }
}

class RemoteCommandListener(private val reactContext: ReactApplicationContext, id: String, description: String = id) : RemoteCommand(id, description) {
    public override fun onInvoke(response: Response) {
        response.requestPayload.put("command_id", commandName)
        try {
            response.requestPayload.toWritableMap()?.let {
                reactContext
                        .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                        .emit(EVENT_EMITTERS_REMOTE_COMMAND, it)
            }
        } catch (jex: JSONException) {
            Logger.qa(BuildConfig.TAG, "Error converting Payload to WritableMap.")
            Logger.qa(BuildConfig.TAG, "${jex.message}")
        }
        response.send()
    }
}