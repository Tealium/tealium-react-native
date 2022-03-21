package com.tealium.react

import android.app.Application
import com.facebook.react.bridge.*
import com.tealium.collectdispatcher.CollectDispatcher
import com.tealium.collectdispatcher.overrideCollectBatchUrl
import com.tealium.collectdispatcher.overrideCollectDomain
import com.tealium.collectdispatcher.overrideCollectUrl
import com.tealium.core.Collectors
import com.tealium.core.DispatcherFactory
import com.tealium.core.Environment
import com.tealium.core.ModuleFactory
import com.tealium.core.collection.AppCollector
import com.tealium.core.collection.ConnectivityCollector
import com.tealium.core.collection.DeviceCollector
import com.tealium.core.collection.TimeCollector
import com.tealium.core.consent.ConsentExpiry
import com.tealium.core.consent.ConsentPolicy
import com.tealium.core.persistence.Expiry
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.lifecycle.Lifecycle
import com.tealium.remotecommanddispatcher.RemoteCommandDispatcher
import com.tealium.tagmanagementdispatcher.TagManagementDispatcher
import com.tealium.visitorservice.VisitorProfile
import com.tealium.visitorservice.VisitorService
import io.mockk.*
import io.mockk.impl.annotations.MockK
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 28])
class ExtensionsTests {

    @MockK
    lateinit var mockApp: Application

    @MockK
    lateinit var mockFile: File

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(Arguments::class)
        every { Arguments.createMap() } returns JavaOnlyMap()
        every { Arguments.createArray() } returns JavaOnlyArray()

        every { mockApp.filesDir } returns mockFile
    }

    @Test
    fun toTealiumConfig_ReturnsNull_WhenMissingAccountOrProfile() {
        var readableMap: WritableMap = JavaOnlyMap()
        assertNull(readableMap.toTealiumConfig(mockk()))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        assertNull(readableMap.toTealiumConfig(mockk()))

        readableMap = JavaOnlyMap()
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")
        assertNull(readableMap.toTealiumConfig(mockk()))
    }

    @Test
    fun toTealiumConfig_ReturnsValidConfig_WhenMinimumVariablesPresent() {
        val readableMap: WritableMap = JavaOnlyMap()
        assertNull(readableMap.toTealiumConfig(mockk()))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")
        val config = readableMap.toTealiumConfig(mockApp)
        assertNotNull(config)
        assertEquals("test-account", config?.accountName)
        assertEquals("test-profile", config?.profileName)
        assertEquals(Environment.PROD, config?.environment)

        assertEquals(Collectors.core, config?.collectors)
        assertEquals(mutableSetOf<DispatcherFactory>(), config?.dispatchers)
        assertEquals(mutableSetOf<ModuleFactory>(), config?.modules)
    }

    @Test
    fun toTealiumConfig_OptionalParams() {
        val readableMap: WritableMap = JavaOnlyMap()
        assertNull(readableMap.toTealiumConfig(mockApp))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")

        // Optional params
        readableMap.putString(KEY_CONFIG_ENV, "dev")
        readableMap.putString(KEY_CONFIG_CUSTOM_VISITOR_ID, "testVisitorId")
        readableMap.putString(KEY_CONFIG_DATA_SOURCE, "dataSource")
        readableMap.putString(KEY_COLLECT_OVERRIDE_DOMAIN, "domain")
        readableMap.putString(KEY_COLLECT_OVERRIDE_URL, "url.domain")
        readableMap.putString(KEY_COLLECT_OVERRIDE_BATCH_URL, "batch.url.domain")
        readableMap.putString(KEY_SETTINGS_OVERRIDE_URL, "settings.domain")
        readableMap.putBoolean(KEY_SETTINGS_USE_REMOTE, true)
        readableMap.putString(KEY_TAG_MANAGEMENT_OVERRIDE_URL, "tags.domain")
        readableMap.putBoolean(KEY_QR_TRACE_ENABLED, true)
        readableMap.putBoolean(KEY_DEEPLINK_TRACKING_ENABLED, true)
        readableMap.putString(KEY_LOG_LEVEL, "dev")
        readableMap.putString(KEY_CONSENT_POLICY, "ccpa")
        readableMap.putString(KEY_CONSENT_LOGGING_URL, "consent.domain")
        readableMap.putBoolean(KEY_CONSENT_LOGGING_ENABLED, true)
        readableMap.putBoolean(KEY_LIFECYCLE_AUTO_TRACKING_ENABLED, true)

        val config = readableMap.toTealiumConfig(mockApp)
        assertEquals("test-account", config?.accountName)
        assertEquals("test-profile", config?.profileName)
        assertEquals(Environment.DEV, config?.environment)

        assertEquals("testVisitorId", config?.existingVisitorId)
        assertEquals("dataSource", config?.dataSourceId)
        assertEquals("domain", config?.overrideCollectDomain)
        assertEquals("url.domain", config?.overrideCollectUrl)
        assertEquals("batch.url.domain", config?.overrideCollectBatchUrl)
        assertEquals("settings.domain", config?.overrideLibrarySettingsUrl)
        assertEquals(true, config?.useRemoteLibrarySettings)

    }

    @Test
    fun toTealiumConfig_DoesNotFail_WhenError() {
        // RN 0.63.3 and below throw when key does not exist.
        val readableMap: WritableMap = spyk(JavaOnlyMap())
        every { readableMap.getString(KEY_CONFIG_DATA_SOURCE) } throws NoSuchKeyException("string")
        every { readableMap.getBoolean(KEY_SETTINGS_USE_REMOTE) } throws NoSuchKeyException("boolean")
        every { readableMap.getMap(KEY_CONSENT_EXPIRY) } throws NoSuchKeyException("boolean")

        assertNull(readableMap.toTealiumConfig(mockApp))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")
        readableMap.putString(KEY_CONFIG_ENV, "dev")

        val config = readableMap.toTealiumConfig(mockApp)
        assertEquals("test-account", config?.accountName)
        assertEquals("test-profile", config?.profileName)
        assertEquals(Environment.DEV, config?.environment)
    }

    @Test
    fun toTealiumConfig_GetsOptionalModules() {
        val readableMap: WritableMap = JavaOnlyMap()

        assertNull(readableMap.toTealiumConfig(mockApp))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")
        readableMap.putString(KEY_CONFIG_ENV, "dev")

        readableMap.putBoolean(KEY_VISITOR_SERVICE_ENABLED, true)
        // Lifecycle passed in Collectors.
        val collectorsArray = JavaOnlyArray().apply { pushString(MODULES_LIFECYCLE) }
        readableMap.putArray(KEY_CONFIG_COLLECTORS, collectorsArray)

        val config = readableMap.toTealiumConfig(mockApp)!!
        assertEquals("test-account", config.accountName)
        assertEquals("test-profile", config.profileName)
        assertEquals(Environment.DEV, config.environment)

        assertTrue(config.modules.contains(VisitorService))
        assertTrue(config.modules.contains(Lifecycle))
    }

    @Test
    fun toTealiumConfig_AddsTimeCollectorByDefault() {
        val readableMap: WritableMap = JavaOnlyMap()

        assertNull(readableMap.toTealiumConfig(mockApp))
        readableMap.putString(KEY_CONFIG_ACCOUNT, "test-account")
        readableMap.putString(KEY_CONFIG_PROFILE, "test-profile")
        readableMap.putString(KEY_CONFIG_ENV, "dev")

        readableMap.putBoolean(KEY_VISITOR_SERVICE_ENABLED, true)
        // Lifecycle passed in Collectors.
        val collectorsArray = JavaOnlyArray().apply { pushString(MODULES_LIFECYCLE) }
        readableMap.putArray(KEY_CONFIG_COLLECTORS, collectorsArray)

        val config = readableMap.toTealiumConfig(mockApp)!!
        assertTrue(config.collectors.contains(TimeCollector))
    }

    @Test
    fun consentPolicyFromString_ReturnsValidPolicyOrNull() {
        assertSame(ConsentPolicy.GDPR, consentPolicyFromString("gdpr"))
        assertSame(ConsentPolicy.GDPR, consentPolicyFromString("Gdpr"))
        assertSame(ConsentPolicy.GDPR, consentPolicyFromString("GDPR"))

        assertSame(ConsentPolicy.CCPA, consentPolicyFromString("ccpa"))
        assertSame(ConsentPolicy.CCPA, consentPolicyFromString("cCpa"))
        assertSame(ConsentPolicy.CCPA, consentPolicyFromString("CCPA"))

        assertNull(consentPolicyFromString(""))
        assertNull(consentPolicyFromString("non-existant-policy"))
    }

    @Test
    fun consentExpiryFromValue_ReturnsValidOrNull() {
        assertEquals(ConsentExpiry(1, TimeUnit.MINUTES), consentExpiryFromValues(1L, "minutes"))
        assertEquals(ConsentExpiry(2, TimeUnit.HOURS), consentExpiryFromValues(2L, "hours"))
        assertEquals(ConsentExpiry(3, TimeUnit.DAYS), consentExpiryFromValues(3L, "days"))

        val monthsExpiry = consentExpiryFromValues(1L, "months")!!
        assertEquals(TimeUnit.DAYS, monthsExpiry.unit)
        assertTrue(monthsExpiry.time >= Calendar.getInstance().getLeastMaximum(Calendar.DAY_OF_MONTH)) // 28
        assertTrue(monthsExpiry.time <= Calendar.getInstance().getMaximum(Calendar.DAY_OF_MONTH)) // 31

        val threeMonthsExpiry = consentExpiryFromValues(3L, "months")!!
        assertEquals(TimeUnit.DAYS, threeMonthsExpiry.unit)
        assertTrue(threeMonthsExpiry.time >= 28 + 31 + 30) // Feb + Mar + Apr
        assertTrue(threeMonthsExpiry.time <= 31 + 31 + 30) // Jul + Aug + Sep

        assertNull(consentExpiryFromValues(-1L, "minutes"))
        assertNull(consentExpiryFromValues(0L, "months"))
        assertNull(consentExpiryFromValues(1L, "invalid"))
    }

    @Test
    fun expiryFromString_ReturnsValidExpirySession() {
        assertSame(Expiry.FOREVER, expiryFromString("forever"))
        assertSame(Expiry.FOREVER, expiryFromString("FOREVER"))
        assertSame(Expiry.FOREVER, expiryFromString("FoReVeR"))

        assertSame(Expiry.SESSION, expiryFromString("session"))
        assertSame(Expiry.SESSION, expiryFromString("SESSION"))
        assertSame(Expiry.SESSION, expiryFromString("SeSSioN"))

        assertSame(Expiry.UNTIL_RESTART, expiryFromString("untilrestart"))
        assertSame(Expiry.UNTIL_RESTART, expiryFromString("UNTILRESTART"))
        assertSame(Expiry.UNTIL_RESTART, expiryFromString("UntilRestart"))

        assertSame(Expiry.SESSION, expiryFromString(""))
        assertSame(Expiry.SESSION, expiryFromString("invalid"))
    }

    @Test
    fun dispatchFromMap_Payload() {
        val map: WritableMap = JavaOnlyMap()
        map.putString(KEY_TRACK_EVENT_TYPE, "event")
        map.putString(KEY_TRACK_EVENT_NAME, "test-event")

        val dataLayer: WritableMap = JavaOnlyMap()
        dataLayer.putString("key", "value")
        map.putMap(KEY_TRACK_DATALAYER, dataLayer)

        val event = dispatchFromMap(map) as TealiumEvent
        assertEquals("test-event", event.eventName)
        assertEquals("value", event.payload()["key"])

        map.putString(KEY_TRACK_EVENT_TYPE, "view")
        map.putString(KEY_TRACK_VIEW_NAME, "test-view")
        val view = dispatchFromMap(map) as TealiumView
        assertEquals("test-view", view.viewName)
        assertEquals("value", view.payload()["key"])

        // Defaults
        map.putString(KEY_TRACK_EVENT_TYPE, "something-else")
        map.putString(KEY_TRACK_EVENT_NAME, null)
        map.putMap(KEY_TRACK_DATALAYER, null)
        val default = dispatchFromMap(map) as TealiumEvent
        assertEquals(DispatchType.EVENT, default.eventName)
        assertFalse(default.payload().containsKey("key"))
    }

    @Test
    fun readableArray_toDispatcherFactories() {
        var readableArray: WritableArray = JavaOnlyArray()
        readableArray.pushString(DISPATCHERS_COLLECT)
        readableArray.pushString(DISPATCHERS_TAG_MANAGEMENT)
        readableArray.pushString(DISPATCHERS_REMOTE_COMMANDS)
        var factories = readableArray.toDispatcherFactories()!!
        assertTrue(factories.contains(CollectDispatcher))
        assertTrue(factories.contains(TagManagementDispatcher))
        assertTrue(factories.contains(RemoteCommandDispatcher))

        readableArray = JavaOnlyArray()
        readableArray.pushString(DISPATCHERS_COLLECT)
        readableArray.pushString("InvalidDispatcher")
        factories = readableArray.toDispatcherFactories()!!
        assertTrue(factories.contains(CollectDispatcher))
        assertFalse(factories.contains(TagManagementDispatcher))
        assertFalse(factories.contains(RemoteCommandDispatcher))
    }

    @Test
    fun readableArray_toCollectorFactories() {
        var readableArray: WritableArray = JavaOnlyArray()
        readableArray.pushString(COLLECTORS_APP)
        readableArray.pushString(COLLECTORS_CONNECTIVITY)
        readableArray.pushString(COLLECTORS_DEVICE)
        readableArray.pushString(COLLECTORS_TIME)
        var factories = readableArray.toCollectorFactories()!!
        assertTrue(factories.contains(AppCollector))
        assertTrue(factories.contains(ConnectivityCollector))
        assertTrue(factories.contains(DeviceCollector))
        assertTrue(factories.contains(TimeCollector))

        readableArray = JavaOnlyArray()
        readableArray.pushString(COLLECTORS_APP)
        readableArray.pushString("InvalidCollector")
        factories = readableArray.toCollectorFactories()!!
        assertTrue(factories.contains(AppCollector))
        assertFalse(factories.contains(ConnectivityCollector))
        assertFalse(factories.contains(DeviceCollector))
        assertFalse(factories.contains(TimeCollector))
    }

    @Test
    fun readableArray_toModuleFactories() {
        var readableArray: WritableArray = JavaOnlyArray()
        readableArray.pushString(MODULES_LIFECYCLE)
        readableArray.pushString(MODULES_VISITOR_SERVICE)
        var factories = readableArray.toModuleFactories()!!
        assertTrue(factories.contains(Lifecycle))
        assertTrue(factories.contains(VisitorService))

        readableArray = JavaOnlyArray()
        readableArray.pushString(MODULES_LIFECYCLE)
        readableArray.pushString("InvalidModule")
        factories = readableArray.toModuleFactories()!!
        assertTrue(factories.contains(Lifecycle))
        assertFalse(factories.contains(VisitorService))
    }

    @Test
    fun readableArray_toTyped_Int() {
        val intArray = JavaOnlyArray().apply {
            pushInt(123)
            pushInt(456)
            pushInt(789)
        }

        // Numbers stored as floating point.
        assertArrayEquals(arrayOf(123.0, 456.0, 789.0), intArray.toTyped())
    }

    @Test
    fun readableArray_toTyped_Boolean() {
        val boolArray = JavaOnlyArray().apply {
            pushBoolean(true)
            pushBoolean(false)
            pushBoolean(true)
        }

        assertArrayEquals(arrayOf(true, false, true), boolArray.toTyped())
    }

    @Test
    fun readableArray_toTyped_String() {
        val intArray = JavaOnlyArray().apply {
            pushString("string_1")
            pushString("string_2")
            pushString("string_3")
        }

        assertArrayEquals(arrayOf("string_1", "string_2", "string_3"), intArray.toTyped())
    }

    @Test
    fun readableArray_toTyped_Mixed() {
        val intArray = JavaOnlyArray().apply {
            pushString("string_1")
            pushInt(123)
            pushBoolean(false)
        }

        assertArrayEquals(arrayOf("string_1", 123.0, false), intArray.toTyped())
    }

    @Test
    fun visitorProfile_toFriendlyJson_VisitorAttributes() {
        val visitorProfile = VisitorProfile.fromJson(JSONObject(validExampleProfileString))
        val visitorJson = VisitorProfile.toFriendlyJson(visitorProfile)

        assertTrue(visitorJson.has("badges"))
        assertEquals(true, visitorJson.getJSONObject("badges").getBoolean("Not Live On Site"))

        assertTrue(visitorJson.has("dates"))
        assertEquals(1610978738000L, visitorJson.getJSONObject("dates").getLong("First visit"))

        assertTrue(visitorJson.has("numbers"))
        assertEquals(1, visitorJson.getJSONObject("numbers").getInt("Total direct visits"))

        assertTrue(visitorJson.has("arraysOfNumbers"))
        assertEquals(43, visitorJson.getJSONObject("arraysOfNumbers").getJSONArray("Imported Counter Values").getInt(0))

        assertTrue(visitorJson.has("tallies"))
        assertEquals(1, visitorJson.getJSONObject("tallies").getJSONObject("Lifetime devices used").getInt("other"))

        assertTrue(visitorJson.has("strings"))
        assertEquals("other", visitorJson.getJSONObject("strings").getString("Lifetime browser types used (favorite)"))

        assertTrue(visitorJson.has("arraysOfStrings"))
        assertEquals("Firefox", visitorJson.getJSONObject("arraysOfStrings").getJSONArray("All Active browser types from visit starts").getString(0))

        assertTrue(visitorJson.has("arraysOfBooleans"))
        assertEquals(true, visitorJson.getJSONObject("arraysOfBooleans").getJSONArray("Direct visit record").getBoolean(0))

        assertTrue(visitorJson.has("currentVisit"))
    }

    @Test
    fun visitorProfile_toFriendlyJson_VisitAttributes() {
        val visitorProfile = VisitorProfile.fromJson(JSONObject(validExampleProfileString))
        val currentVisitJson = VisitorProfile.toFriendlyJson(visitorProfile).optJSONObject("currentVisit")!!

        assertTrue(currentVisitJson.has("dates"))
        assertEquals(1610978738000L, currentVisitJson.getJSONObject("dates").getLong("Visit start"))

        assertTrue(currentVisitJson.has("booleans"))
        assertEquals(true, currentVisitJson.getJSONObject("booleans").getBoolean("Direct visit"))

        assertTrue(currentVisitJson.has("numbers"))
        assertEquals(16, currentVisitJson.getJSONObject("numbers").getInt("Event count"))

        assertTrue(currentVisitJson.has("strings"))
        assertEquals("other", currentVisitJson.getJSONObject("strings").getString("Active browser type"))

        assertTrue(currentVisitJson.has("setsOfStrings"))
        assertEquals("other", currentVisitJson.getJSONObject("setsOfStrings").getJSONArray("Active browser types").getString(0))
        assertEquals("Firefox", currentVisitJson.getJSONObject("setsOfStrings").getJSONArray("Active browser types").getString(1))
    }

    private val validExampleProfileString = """
        {
            "badges": {
                "Not Live On Site": true
            },
            "dates": {
                "First visit": 1610978738000,
                "Last visit": 1610978817000
            },
            "metrics": {
                "Total direct visits": 1,
                "Lifetime visit count": 1,
                "Lifetime event count": 16,
                "Total time spent on site in minutes": 1.3166666666666667,
                "Average visit duration in minutes": 1.3166666666666667,
                "Weeks since first visit": 1,
                "Average visits per week": 1,
                "Counter - Current Value [num]": 42
            },
            "metric_sets": {
                "Lifetime devices used": {
                    "other": 1,
                    "Windows desktop": 1
                }
            },
            "properties": {
                "Lifetime browser types used (favorite)": "other",
                "Lifetime operating systems used (favorite)": "other"
            },
            "_id": "__tealium-solutions_test-example__5034_000000000000000041763025668622936__",
            "property_lists": {
                "All Active browser types from visit starts": [
                    "Firefox"
                ]
            },
            "metric_lists": {
                "Imported Counter Values": [
                    43
                ]
            },
            "flag_lists": {
                "Direct visit record": [
                    true
                ]
            },
            "audiences_joined_at": {
                "Counter at least 40": "2021-01-18T14:06:53.288Z"
            },
            "current_visit": {
                "dates": {
                    "Visit start": 1610978738000,
                    "Visit end": 1610978817000
                },
                "flags": {
                    "Direct visit": true
                },
                "metrics": {
                    "Event count": 16,
                    "Visit duration": 1.3166666666666667,
                    "Weeks since first visit temp": 0
                },
                "properties": {
                    "Active browser type": "other",
                    "Active operating system": "other",
                    "Active device": "other",
                    "Active platform": "browser",
                    "Active browser version": "other"
                },
                "property_sets": {
                    "Active browser types": [
                        "other",
                        "Firefox"
                    ],
                    "Active operating systems": [
                        "other",
                        "Windows"
                    ],
                    "Active devices": [
                        "other",
                        "Windows desktop"
                    ],
                    "Active platforms": [
                        "browser"
                    ],
                    "Active browser versions": [
                        "other",
                        "Firefox"
                    ]
                }
            }
        }""".trimIndent()
}