package com.tealium.react

import android.app.Activity
import android.app.Application
import android.util.Log
import com.facebook.react.bridge.*
import com.tealium.core.Tealium
import com.tealium.core.consent.ConsentCategory
import com.tealium.core.consent.ConsentManager
import com.tealium.core.consent.ConsentStatus
import com.tealium.core.persistence.DataLayer
import com.tealium.core.persistence.Expiry
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.lifecycle.Lifecycle
import com.tealium.lifecycle.lifecycle
import com.tealium.remotecommanddispatcher.RemoteCommandDispatcher
import com.tealium.remotecommanddispatcher.remoteCommands
import com.tealium.remotecommands.RemoteCommand
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 28])
class TealiumReactTests {

    @MockK
    lateinit var mockReactApplicationContext: ReactApplicationContext

    @MockK
    lateinit var mockActivity: Activity

    @MockK
    lateinit var mockApplication: Application

    @MockK
    lateinit var mockFile: File

    @RelaxedMockK
    lateinit var mockTealium: Tealium

    @RelaxedMockK
    lateinit var mockRemoteCommandDispatcher: RemoteCommandDispatcher

    @RelaxedMockK
    lateinit var mockCallback: Callback

    lateinit var tealiumReact: TealiumReact

    lateinit var minimalConfig: WritableMap

    private val foreverExpiry = "forever"
    private val sessionExpiry = "session"

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.i(any(), any()) } returns 0
        mockkStatic(Arguments::class)
        every { Arguments.createMap() } answers { JavaOnlyMap() }
        every { Arguments.createArray() } answers { JavaOnlyArray() }

        every { mockReactApplicationContext.currentActivity } returns mockActivity
        every { mockApplication.filesDir } returns mockFile
        every { mockActivity.application } returns mockApplication

        mockkObject(Tealium)
        val callback = slot<Tealium.() -> Unit>()
        every { Tealium.create(any(), any(), capture(callback)) } answers {
            callback.captured(mockTealium)
            mockTealium
        }
        every { mockTealium.remoteCommands } returns mockRemoteCommandDispatcher
        every { mockRemoteCommandDispatcher.add(any(), any(), any()) } just Runs

        minimalConfig = JavaOnlyMap().apply {
            putString(KEY_CONFIG_ACCOUNT, "account")
            putString(KEY_CONFIG_PROFILE, "profile")
        }

        tealiumReact = TealiumReact(mockReactApplicationContext)
        tealiumReact.initialize(minimalConfig, null)
    }

    @Test
    fun reactPackage_createModules_Contains_TealiumReact() {
        val reactPackage = TealiumReactNative()

        assertTrue(reactPackage.createNativeModules(mockk()).filterIsInstance(TealiumReact::class.java).size == 1)
    }

    @Test
    fun initialize_UsesMain_InstanceName() {
        // init called in @Before
        verify {
            Tealium.create(INSTANCE_NAME, match {
                it.accountName == "account"
                        && it.profileName == "profile"
            }, any())
        }
    }

    @Test
    fun initialize_CreatesLocalRemoteCommand_WhenCallbackSupplied() {
        val remoteCommandsArray: WritableArray = JavaOnlyArray()
        val localRemoteCommand: WritableMap = JavaOnlyMap()
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_ID, "some_command")
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_CALLBACK, null)
        remoteCommandsArray.pushMap(localRemoteCommand)

        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        tealiumReact.initialize(minimalConfig, null)

        verify(exactly = 1) {
            mockRemoteCommandDispatcher.add(match {
                it.commandName == "some_command"
            })
        }
    }

    @Test
    fun initialize_CreatesLocalRemoteCommand_WithPathAndUrl() {
        val remoteCommandsArray: WritableArray = JavaOnlyArray()
        val localRemoteCommand: WritableMap = JavaOnlyMap()
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_ID, "some_command")
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_CALLBACK, null)
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_PATH, "my-path.json")
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_URL, "localhost")
        remoteCommandsArray.pushMap(localRemoteCommand)

        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        tealiumReact.initialize(minimalConfig, null)

        verify(exactly = 1) {
            mockRemoteCommandDispatcher.add(match {
                it.commandName == "some_command"
            }, "my-path.json", "localhost")
        }
    }

    @Test
    fun initialize_DoesNot_CreateLocalRemoteCommand_WhenMissingIdOrCallback() {
        var remoteCommandsArray: WritableArray = JavaOnlyArray()
        var localRemoteCommand: WritableMap = JavaOnlyMap()
        remoteCommandsArray.pushMap(localRemoteCommand)
        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        // missing id and callback
        tealiumReact.initialize(minimalConfig, null)

        // missing callback only
        remoteCommandsArray = JavaOnlyArray()
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_ID, "some_command")
        remoteCommandsArray.pushMap(localRemoteCommand)
        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        tealiumReact.initialize(minimalConfig, null)

        // missing id
        localRemoteCommand = JavaOnlyMap()
        remoteCommandsArray = JavaOnlyArray()
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_CALLBACK, null)
        remoteCommandsArray.pushMap(localRemoteCommand)
        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        tealiumReact.initialize(minimalConfig, null)
    }

    @Test
    fun initialize_CreatesFactoryRemoteCommand_WhenNoCallback() {
        val remoteCommandsArray: WritableArray = JavaOnlyArray()
        val localRemoteCommand: WritableMap = JavaOnlyMap()
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_ID, "factory_command")
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_PATH, "my-path.json")
        localRemoteCommand.putString(KEY_REMOTE_COMMANDS_URL, "localhost")
        remoteCommandsArray.pushMap(localRemoteCommand)

        minimalConfig.putArray(KEY_REMOTE_COMMANDS_CONFIG, remoteCommandsArray)
        // no factory registered yet; no command should be created.
        tealiumReact.initialize(minimalConfig, null)

        // register factory
        tealiumReact.registerRemoteCommandFactory(TestRemoteCommandFactory("factory_command"))
        tealiumReact.initialize(minimalConfig, null)

        verify(exactly = 1) {
            mockRemoteCommandDispatcher.add(match {
                it.commandName == "factory_command"
            }, "my-path.json", "localhost")
        }
    }

    @Test
    fun initialize_CallsCallback_OnSuccess() {
        val callback: Callback = mockk(relaxed = true)
        tealiumReact.initialize(minimalConfig, callback)

        verify(timeout = 1500) {
            callback.invoke(true)
        }
    }

    @Test
    fun initialize_CallsCallback_OnFailure() {
        val callback: Callback = mockk(relaxed = true)
        tealiumReact.initialize(JavaOnlyMap(), callback)

        verify(timeout = 1500) {
            callback.invoke(false)
        }
    }

    @Test
    fun initialize_TracksLifecycle_IfEnabled() {
        val mockLifecycle: Lifecycle = mockk(relaxed = true)
        every { mockTealium.lifecycle } returns mockLifecycle
        minimalConfig.putBoolean(KEY_LIFECYCLE_AUTO_TRACKING_ENABLED, true)

        tealiumReact.initialize(minimalConfig, null)

        verify(timeout = 1500) {
            mockLifecycle.onActivityResumed(any())
        }
    }

    @Test
    fun initialize_DoesNotTrackLifecycle_IfDisabled() {
        val mockLifecycle: Lifecycle = mockk(relaxed = true)
        every { mockTealium.lifecycle } returns mockLifecycle
        minimalConfig.putBoolean(KEY_LIFECYCLE_AUTO_TRACKING_ENABLED, false)

        tealiumReact.initialize(minimalConfig, null)

        verify(exactly = 0, timeout = 1500) {
            mockLifecycle.onActivityResumed(any())
        }
    }

    @Test
    fun terminate_CallsDestroy() {
        tealiumReact.terminateInstance()

        verify {
            Tealium.destroy(INSTANCE_NAME)
        }
    }

    @Test
    fun track_CallsTrack() {
        val eventMap = JavaOnlyMap().apply {
            putString(KEY_TRACK_EVENT_TYPE, "event")
            putString(KEY_TRACK_EVENT_NAME, "myEvent")
        }
        val viewMap = JavaOnlyMap().apply {
            putString(KEY_TRACK_EVENT_TYPE, "view")
            putString(KEY_TRACK_VIEW_NAME, "myView")
        }

        tealiumReact.track(eventMap)
        tealiumReact.track(viewMap)

        verify {
            mockTealium.track(match {
                it is TealiumEvent
                        && it.eventName == "myEvent"
            })
            mockTealium.track(match {
                it is TealiumView
                        && it.viewName == "myView"
            })
        }
    }

    @Test
    fun addToDataLayer_String() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putString("string", "value")
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putString("string", "value", Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_Int() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putInt("int", 100)
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        // ReactNative represents ints as doubles
        verify {
            dataLayer.putDouble("int", 100.0, Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_Double() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putDouble("double", 100.1)
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putDouble("double", 100.1, Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_Boolean() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putBoolean("boolean", true)
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putBoolean("boolean", true, Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_StringArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("stringArray", JavaOnlyArray().apply {
                pushString("string_1")
                pushString("string_2")
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putStringArray("stringArray", arrayOf("string_1", "string_2"), Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_IntArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("intArray", JavaOnlyArray().apply {
                pushInt(1)
                pushInt(2)
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        // ReactNative represents ints as doubles.
        verify {
            dataLayer.putDoubleArray("intArray", arrayOf(1.0, 2.0), Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_DoubleArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("doubleArray", JavaOnlyArray().apply {
                pushDouble(1.1)
                pushDouble(2.2)
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putDoubleArray("doubleArray", arrayOf(1.1, 2.2), Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_BooleanArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("booleanArray", JavaOnlyArray().apply {
                pushBoolean(true)
                pushBoolean(false)
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putBooleanArray("booleanArray", arrayOf(true, false), Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_ObjectArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("objectArray", JavaOnlyArray().apply {
                pushMap(JavaOnlyMap().apply {
                    putString("string_1", "value_1")
                    putInt("count", 10)
                })
                pushMap(JavaOnlyMap().apply {
                    putString("string_2", "value_2")
                    putInt("count", 11)
                })
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putString("objectArray", match {
                JSONArray(it).let {
                    it.getJSONObject(0).getString("string_1") == "value_1"
                            && it.getJSONObject(0).getInt("count") == 10
                            && it.getJSONObject(1).getString("string_2") == "value_2"
                            && it.getJSONObject(1).getInt("count") == 11
                }
            }, Expiry.FOREVER)
        }
    }

    @Test
    fun addToDataLayer_MixedArray() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putArray("mixedArray", JavaOnlyArray().apply {
                pushMap(JavaOnlyMap().apply {
                    putString("string_1", "value_1")
                    putInt("count", 10)
                })
                pushString("string_2")
                pushInt(11)
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putString("mixedArray", match {
                JSONArray(it).let {
                    it.getJSONObject(0).getString("string_1") == "value_1"
                    it.getJSONObject(0).getInt("count") == 10
                    it.getString(1) == "string_2"
                    it.getInt(2) == 11
                }
            }, Expiry.FOREVER)
        }

    }

    @Test
    fun addToDataLayer_Object() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val map = JavaOnlyMap().apply {
            putMap("map", JavaOnlyMap().apply {
                putString("string_1", "value_1")
                putInt("count", 10)
            })
        }
        tealiumReact.addToDataLayer(map, foreverExpiry)

        verify {
            dataLayer.putJsonObject("map", match {
                it.getString("string_1") == "value_1"
                        && it.getInt("count") == 10
            }, Expiry.FOREVER)
        }
    }

    @Test
    fun getFromDataLayer_String() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns "value"

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke("value")
        }
    }

    @Test
    fun getFromDataLayer_Int() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns 100

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(100)
        }
    }

    @Test
    fun getFromDataLayer_Double() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns 100.1

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(100.1)
        }
    }

    @Test
    fun getFromDataLayer_Boolean() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns true

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(true)
        }
    }

    @Test
    fun getFromDataLayer_StringArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns arrayOf("string_1", "string_2")

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.toArrayList().contains("string_1")
                        && it.toArrayList().contains("string_2")
            })
        }
    }

    @Test
    fun getFromDataLayer_IntArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns arrayOf(1, 2)

        tealiumReact.getFromDataLayer("test", callback)

        // ReactNative represents ints as Doubles
        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.toArrayList().contains(1.0)
                        && it.toArrayList().contains(2.0)
            })
        }
    }

    @Test
    fun getFromDataLayer_DoubleArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns arrayOf(100.1, 100.2)

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.toArrayList().contains(100.1)
                        && it.toArrayList().contains(100.2)
            })
        }
    }

    @Test
    fun getFromDataLayer_BooleanArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns arrayOf(true, false)

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.toArrayList().contains(true)
                        && it.toArrayList().contains(false)
            })
        }
    }

    @Test
    fun getFromDataLayer_ObjectArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns """
            [{"string_1":"value_1","count":10},{"string_2":"value_2","count":11}]
        """.trimIndent()

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.getType(0) == ReadableType.Map
                        && it.getType(1) == ReadableType.Map
                        && it.getMap(0)?.getString("string_1") == "value_1"
                        && it.getMap(0)?.getInt("count") == 10
                        && it.getMap(1)?.getString("string_2") == "value_2"
                        && it.getMap(1)?.getInt("count") == 11
            })
        }
    }

    @Test
    fun getFromDataLayer_MixedArray() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns """
            [{"string_1":"value_1","count":10},"string_2",10]
        """.trimIndent()

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableArray
                        && it.getType(0) == ReadableType.Map
                        && it.getType(1) == ReadableType.String
                        && it.getType(2) == ReadableType.Number
                        && it.getMap(0)?.getString("string_1") == "value_1"
                        && it.getMap(0)?.getInt("count") == 10
                        && it.getString(1) == "string_2"
                        && it.getInt(2) == 10
            })
        }
    }

    @Test
    fun getFromDataLayer_Object() {
        val callback: Callback = mockk(relaxed = true)
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer
        every { dataLayer.get("test") } returns JSONObject("""
            {"string_1":"value_1","count":10}
        """.trimIndent())

        tealiumReact.getFromDataLayer("test", callback)

        verify {
            callback.invoke(match {
                it is ReadableMap
                        && it.getString("string_1") == "value_1"
                        && it.getInt("count") == 10
            })
        }
    }

    @Test
    fun removeFromDataLayer_CallsRemove_ForEachKey() {
        val dataLayer: DataLayer = mockk(relaxed = true)
        every { mockTealium.dataLayer } returns dataLayer

        val keys = JavaOnlyArray().apply {
            pushString("key1")
            pushString("key2")
        }
        tealiumReact.removeFromDataLayer(keys)

        verify {
            dataLayer.remove("key1")
            dataLayer.remove("key2")
        }
    }

    @Test
    fun setConsentStatus_AlwaysSetsStatus() {
        val consentManager: ConsentManager = mockk(relaxed = true)
        every { mockTealium.consentManager } returns consentManager

        tealiumReact.setConsentStatus("consented")
        tealiumReact.setConsentStatus("notConsented")

        verify {
            consentManager.userConsentStatus = ConsentStatus.CONSENTED
            consentManager.userConsentStatus = ConsentStatus.NOT_CONSENTED
        }

        tealiumReact.setConsentStatus("unknown")
        tealiumReact.setConsentStatus("invalid") // sets to unknown
        verify(exactly = 2) {
            consentManager.userConsentStatus = ConsentStatus.UNKNOWN
        }
    }

    @Test
    fun setConsentCategories_SetsCategoryList() {
        val consentManager: ConsentManager = mockk(relaxed = true)
        every { mockTealium.consentManager } returns consentManager

        val categories = JavaOnlyArray().apply {
            pushString("affiliates")
            pushString("analytics")
        }

        tealiumReact.setConsentCategories(categories)

        verify {
            consentManager.userConsentCategories = match {
                it.contains(ConsentCategory.ANALYTICS)
                        && it.contains(ConsentCategory.AFFILIATES)
            }
        }
    }

    @Test
    fun getConsentStatus_CallsCallback() {
        val consentManager: ConsentManager = mockk(relaxed = true)
        every { mockTealium.consentManager } returns consentManager
        every { consentManager.userConsentStatus } returns ConsentStatus.UNKNOWN

        tealiumReact.getConsentStatus(mockCallback)

        verify {
            mockCallback.invoke("unknown")
        }
    }

    @Test
    fun getConsentCategories_CallsCallback() {
        val consentManager: ConsentManager = mockk(relaxed = true)
        every { mockTealium.consentManager } returns consentManager
        every { consentManager.userConsentCategories } returns setOf(ConsentCategory.AFFILIATES, ConsentCategory.ANALYTICS)

        tealiumReact.getConsentCategories(mockCallback)

        verify {
            mockCallback.invoke(match {
                it is ReadableArray
                        && it.toArrayList().contains("affiliates")
                        && it.toArrayList().contains("analytics")
            })
        }
    }

    @Test
    fun addRemoteCommand_AddsRemoteCommandListener() {
        val remoteCommandDispatcher: RemoteCommandDispatcher = mockk(relaxed = true)
        every { mockTealium.remoteCommands } returns remoteCommandDispatcher
        every { remoteCommandDispatcher.add(any()) } just Runs

        tealiumReact.addRemoteCommand("id")

        verify {
            remoteCommandDispatcher.add(match {
                it.commandName == "id"
                        && it is RemoteCommandListener
            })
        }
    }

    @Test
    fun removeRemoteCommand_RemovesById() {
        val remoteCommandDispatcher: RemoteCommandDispatcher = mockk(relaxed = true)
        every { mockTealium.remoteCommands } returns remoteCommandDispatcher
        every { remoteCommandDispatcher.remove(any()) } just Runs

        tealiumReact.removeRemoteCommand("id")

        verify {
            remoteCommandDispatcher.remove("id")
        }
    }

    @Test
    fun joinTrace_JoinsProvidedTraceId() {
        every { mockTealium.joinTrace(any()) } just Runs

        tealiumReact.joinTrace("id")

        verify {
            mockTealium.joinTrace("id")
        }
    }

    @Test
    fun leaveTrace_LeavesTrace() {
        every { mockTealium.leaveTrace() } just Runs

        tealiumReact.leaveTrace()

        verify {
            mockTealium.leaveTrace()
        }
    }

    @Test
    fun getVisitorId_CallsCallback() {
        every { mockTealium.visitorId } returns "visitor123"

        tealiumReact.getVisitorId(mockCallback)

        verify {
            mockCallback.invoke("visitor123")
        }
    }

    @Test
    fun getSessionId_CallsCallback() {
        every { mockTealium.session.id } returns 12345

        tealiumReact.getSessionId(mockCallback)

        verify {
            mockCallback.invoke("12345")
        }
    }

    private class TestRemoteCommandFactory(
            override val name: String = "factory_command"
    ) : RemoteCommandFactory {

        override fun create(): RemoteCommand {
            return object : RemoteCommand(name, "") {
                override fun onInvoke(p0: Response?) {
                    // do nothing
                }
            }
        }
    }
}