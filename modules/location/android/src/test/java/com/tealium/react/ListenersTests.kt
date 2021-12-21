package com.tealium.react

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.tealium.core.consent.ConsentStatus
import com.tealium.core.consent.UserConsentPreferences
import com.tealium.remotecommands.RemoteCommand
import com.tealium.visitorservice.VisitorProfile
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.json.JSONObject
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 28])
class ListenersTests {

    @MockK
    lateinit var mockReactContext: ReactApplicationContext

    @RelaxedMockK
    lateinit var mockDeviceJsEmitter: DeviceEventManagerModule.RCTDeviceEventEmitter

    lateinit var emitterListeners: EmitterListeners
    lateinit var remoteCommandListener: RemoteCommandListener

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Arguments::class)
        every { Arguments.createMap() } returns JavaOnlyMap()
        every { Arguments.createArray() } returns JavaOnlyArray()

        every { mockReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java) } returns mockDeviceJsEmitter
        emitterListeners = EmitterListeners(mockReactContext)
        remoteCommandListener = RemoteCommandListener(mockReactContext, "test")
    }

    @Test
    fun visitorListener_EmitsWritableMap_OfVisitor() {
        emitterListeners = EmitterListeners(mockReactContext)

        val visitorProfile = VisitorProfile(audiences = mapOf("audience_1" to "true"))
        emitterListeners.onVisitorUpdated(visitorProfile)

        verify(exactly = 1) {
            mockDeviceJsEmitter.emit(EVENT_EMITTERS_VISITOR, match {
                (it as WritableMap).getMap("audiences")?.getString("audience_1") == "true"
            })
        }

    }

    @Test
    fun consentListener_DoesNotExpire_WhenStatusNotUnknown() {
        var userConsentPreferences = UserConsentPreferences(ConsentStatus.CONSENTED, setOf())
        emitterListeners.onUserConsentPreferencesUpdated(userConsentPreferences, mockk())
        userConsentPreferences = UserConsentPreferences(ConsentStatus.NOT_CONSENTED, setOf())
        emitterListeners.onUserConsentPreferencesUpdated(userConsentPreferences, mockk())

        verify(exactly = 0) {
            mockDeviceJsEmitter.emit(EVENT_EMITTERS_CONSENT_EXPIRED, any())
        }
    }

    @Test
    fun consentListener_Expires_WhenStatusUnknown() {
        val userConsentPreferences = UserConsentPreferences(ConsentStatus.UNKNOWN, setOf())
        emitterListeners.onUserConsentPreferencesUpdated(userConsentPreferences, mockk())

        verify(exactly = 1) {
            mockDeviceJsEmitter.emit(EVENT_EMITTERS_CONSENT_EXPIRED, any())
        }
    }

    @Test
    fun remoteCommandListener_AddsRemoteCommandId_ToPayload() {
        val response: RemoteCommand.Response = mockk()
        val payload = JSONObject().apply {
            put("string", "value")
        }
        every { response.requestPayload } returns payload
        every { response.send() } just Runs
        remoteCommandListener.onInvoke(response)

        verify {
            mockDeviceJsEmitter.emit(EVENT_EMITTERS_REMOTE_COMMAND, match {
                (it as WritableMap).getString("command_id") == "test"
                        && it.getString("string") == "value"
            })
        }
    }
}