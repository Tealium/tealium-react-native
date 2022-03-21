@file:JvmName("Utils")

package com.tealium.react

import android.app.Application
import android.util.Log
import com.facebook.react.bridge.*
import com.tealium.collectdispatcher.CollectDispatcher
import com.tealium.collectdispatcher.overrideCollectBatchUrl
import com.tealium.collectdispatcher.overrideCollectDomain
import com.tealium.collectdispatcher.overrideCollectUrl
import com.tealium.core.*
import com.tealium.core.collection.AppCollector
import com.tealium.core.collection.ConnectivityCollector
import com.tealium.core.collection.DeviceCollector
import com.tealium.core.collection.TimeCollector
import com.tealium.core.consent.*
import com.tealium.core.persistence.Expiry
import com.tealium.dispatcher.Dispatch
import com.tealium.dispatcher.TealiumEvent
import com.tealium.dispatcher.TealiumView
import com.tealium.lifecycle.Lifecycle
import com.tealium.lifecycle.isAutoTrackingEnabled
import com.tealium.remotecommanddispatcher.RemoteCommandDispatcher
import com.tealium.tagmanagementdispatcher.TagManagementDispatcher
import com.tealium.tagmanagementdispatcher.overrideTagManagementUrl
import com.tealium.visitorservice.VisitorProfile
import com.tealium.visitorservice.VisitorService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

private fun missingRequiredProperty(name: String) {
    Log.d(BuildConfig.TAG, "Missing required property: $name")
}

fun ReadableMap.toTealiumConfig(application: Application): TealiumConfig? {
    val account = getString(KEY_CONFIG_ACCOUNT);
    val profile = getString(KEY_CONFIG_PROFILE);
    val environmentString = safeGetString(KEY_CONFIG_ENV);

    if (account.isNullOrBlank()) {
        missingRequiredProperty(KEY_CONFIG_ACCOUNT)
        return null
    }
    if (profile.isNullOrBlank()) {
        missingRequiredProperty(KEY_CONFIG_PROFILE)
        return null
    }

    val environment = try {
        Environment.valueOf(environmentString?.toUpperCase(Locale.ROOT) ?: "PROD")
    } catch (iax: IllegalArgumentException) {
        missingRequiredProperty(KEY_CONFIG_ENV)
        Environment.PROD
    }

    val collectors = safeGetArray(KEY_CONFIG_COLLECTORS)?.toCollectorFactories()
    // Swift has Timing enabled by default.
    collectors?.add(TimeCollector)

    val modules = Arguments.createArray().let { moduleArray ->
        // Visitor Service passed as boolean
        safeGetBoolean(KEY_VISITOR_SERVICE_ENABLED)?.let { vsEnabled ->
            if (vsEnabled) {
                moduleArray.pushString(MODULES_VISITOR_SERVICE)
            }
        }

        // Lifecycle currently passed in collectors
        safeGetArray(KEY_CONFIG_COLLECTORS)?.let {
            if (it.toArrayList().contains(MODULES_LIFECYCLE)) {
                moduleArray.pushString(MODULES_LIFECYCLE)
            }
        }

        // not currently in use; leaving in for possible future extensions
        safeGetArray(KEY_CONFIG_MODULES)?.let { modules ->
            for (i in 0 until modules.size()) {
                moduleArray.pushString(modules.getString(i))
            }
        }
        moduleArray as ReadableArray
    }.toModuleFactories()
    val dispatchers = safeGetArray(KEY_CONFIG_DISPATCHERS)?.toDispatcherFactories()

    val config = TealiumConfig(application, account, profile, environment,
            collectors = collectors ?: Collectors.core,
            modules = modules ?: mutableSetOf(),
            dispatchers = dispatchers ?: mutableSetOf())


    config.apply {
        // Data Source Id
        safeGetString(KEY_CONFIG_DATA_SOURCE)?.let {
            dataSourceId = it
        }

        // Existing visitor id
        safeGetString(KEY_CONFIG_CUSTOM_VISITOR_ID)?.let {
            existingVisitorId = it
        }

        // Collect Settings
        safeGetString(KEY_COLLECT_OVERRIDE_URL)?.let {
            overrideCollectUrl = it
        }
        safeGetString(KEY_COLLECT_OVERRIDE_BATCH_URL)?.let {
            overrideCollectBatchUrl = it
        }
        safeGetString(KEY_COLLECT_OVERRIDE_DOMAIN)?.let {
            overrideCollectDomain = it
        }

        // Library Settings
        safeGetBoolean(KEY_SETTINGS_USE_REMOTE)?.let {
            useRemoteLibrarySettings = it
        }

        safeGetString(KEY_SETTINGS_OVERRIDE_URL)?.let {
            overrideLibrarySettingsUrl = it
        }

        // Tag Management
        safeGetString(KEY_TAG_MANAGEMENT_OVERRIDE_URL)?.let {
            overrideTagManagementUrl = it
        }

        // Deep Links
        safeGetBoolean(KEY_QR_TRACE_ENABLED)?.let {
            qrTraceEnabled = it
        }
        safeGetBoolean(KEY_DEEPLINK_TRACKING_ENABLED)?.let {
            deepLinkTrackingEnabled = it
        }

        // Log Level
        safeGetString(KEY_LOG_LEVEL)?.let {
            Logger.logLevel = LogLevel.fromString(it)
        }

        // Consent
        safeGetBoolean(KEY_CONSENT_LOGGING_ENABLED)?.let {
            consentManagerLoggingEnabled = it
        }
        safeGetString(KEY_CONSENT_LOGGING_URL)?.let {
            consentManagerLoggingUrl = it
        }

        safeGetMap(KEY_CONSENT_EXPIRY)?.let { map ->
            map.getDouble(KEY_CONSENT_EXPIRY_TIME).let { time ->
                map.getString(KEY_CONSENT_EXPIRY_UNIT)?.let { unit ->
                    consentExpiry = consentExpiryFromValues(time.toLong(), unit)
                }
            }
        }

        safeGetString(KEY_CONSENT_POLICY)?.let {
            consentManagerEnabled = true
            consentManagerPolicy = consentPolicyFromString(it)
        }

        // Lifecycle
        safeGetBoolean(KEY_LIFECYCLE_AUTO_TRACKING_ENABLED)?.let {
            isAutoTrackingEnabled = it
        }
    }

    return config
}

fun ReadableMap.safeGetString(key: String): String? {
    return if (hasValue(key, ReadableType.String)) getString(key) else null
}

fun ReadableMap.safeGetBoolean(key: String): Boolean? {
    return if (hasValue(key, ReadableType.Boolean)) getBoolean(key) else null
}

fun ReadableMap.safeGetInt(key: String): Int? {
    return if (hasValue(key, ReadableType.Number)) getInt(key) else null
}

fun ReadableMap.safeGetDouble(key: String): Double? {
    return if (hasValue(key, ReadableType.Number)) getDouble(key) else null
}

fun ReadableMap.safeGetArray(key: String): ReadableArray? {
    return if (hasValue(key, ReadableType.Array)) getArray(key) else null
}

fun ReadableMap.safeGetMap(key: String): ReadableMap? {
    return if (hasValue(key, ReadableType.Map)) getMap(key) else null
}

/**
 * Checks that a valid, non-null, value of a given type exists at the given key
 */
private fun ReadableMap.hasValue(key: String, type: ReadableType) : Boolean {
    return this.hasKey(key) && !this.isNull(key) && this.getType(key) == type
}

fun consentPolicyFromString(name: String): ConsentPolicy? {
    return try {
        ConsentPolicy.valueOf(name.toUpperCase(Locale.ROOT))
    } catch (iax: IllegalArgumentException) {
        null
    }
}

fun consentExpiryFromValues(time: Long, unit: String): ConsentExpiry? {
    if (time <= 0) return null

    val count: Long = if (unit == "months") {
        // No TimeUnit.MONTHS, so needs conversion to days.
        val cal = Calendar.getInstance()
        val today = cal.timeInMillis
        cal.add(Calendar.MONTH, time.toInt())
        (cal.timeInMillis - today) / (1000 * 60 * 60 * 24)
    } else { time }
    return timeUnitFromString(unit)?.let { ConsentExpiry(count, it) }
}

fun timeUnitFromString(unit: String): TimeUnit? {
    return when(unit) {
        "minutes" -> TimeUnit.MINUTES
        "hours" -> TimeUnit.HOURS
        "days" -> TimeUnit.DAYS
        "months" -> TimeUnit.DAYS
        else -> null
    }
}

fun ReadableArray.toCollectorFactories(): MutableSet<CollectorFactory>? {
    return toArrayList().mapNotNull { collectorFactoryFromString(it.toString()) }.toMutableSet()
}

fun collectorFactoryFromString(name: String): CollectorFactory? {
    return when (name) {
        COLLECTORS_APP -> AppCollector
        COLLECTORS_CONNECTIVITY -> ConnectivityCollector
        COLLECTORS_DEVICE -> DeviceCollector
        COLLECTORS_TIME -> TimeCollector
        else -> null
    }
}

fun ReadableArray.toModuleFactories(): MutableSet<ModuleFactory>? {
    return toArrayList().mapNotNull { moduleFactoryFromString(it.toString()) }.toMutableSet()
}

fun moduleFactoryFromString(name: String): ModuleFactory? {
    return when (name) {
        MODULES_LIFECYCLE -> Lifecycle
        MODULES_VISITOR_SERVICE -> VisitorService
        else -> null
    }
}

fun ReadableArray.toDispatcherFactories(): MutableSet<DispatcherFactory>? {
    return toArrayList().map { dispatcherFactoryFromString(it.toString()) }.filterNotNull().toMutableSet()
}

fun dispatcherFactoryFromString(name: String): DispatcherFactory? {
    return when (name) {
        DISPATCHERS_COLLECT -> CollectDispatcher
        DISPATCHERS_TAG_MANAGEMENT -> TagManagementDispatcher
        DISPATCHERS_REMOTE_COMMANDS -> RemoteCommandDispatcher
        else -> null
    }
}

fun expiryFromString(name: String) = when (name.toLowerCase(Locale.ROOT)) {
    "forever" -> Expiry.FOREVER
    "untilrestart" -> Expiry.UNTIL_RESTART
    else -> Expiry.SESSION
}

fun dispatchFromMap(map: ReadableMap): Dispatch {
    val eventType = map.getString(KEY_TRACK_EVENT_TYPE) ?: DispatchType.EVENT

    return when (eventType.toLowerCase(Locale.ROOT)) {
        DispatchType.VIEW -> TealiumView(map.getString(KEY_TRACK_VIEW_NAME)
                ?: DispatchType.VIEW,
                map.safeGetMap(KEY_TRACK_DATALAYER)?.toHashMap())
        else -> TealiumEvent(map.getString(KEY_TRACK_EVENT_NAME)
                ?: DispatchType.EVENT,
                map.safeGetMap(KEY_TRACK_DATALAYER)?.toHashMap())
    }
}

fun ReadableArray.isSingleType(): Boolean {
    if (this.size() == 0) return true

    val type = getDynamic(0).type
    for (i in 0 until size()) {
        if (getType(i) != type) return false
    }
    return true
}

inline fun <reified T : Any> ReadableArray.toTyped(): Array<T> {
    if (this.size() == 0) return arrayOf()

    return this.toArrayList().mapNotNull { it as? T }.toTypedArray()
}

@Throws(JSONException::class)
fun JSONObject.toWritableMap(): WritableMap? {
    val map = Arguments.createMap()
    val iterator = keys()
    while (iterator.hasNext()) {
        val key = iterator.next()
        val value = this[key]
        if (value is JSONObject) {
            map.putMap(key, value.toWritableMap())
        } else if (value is JSONArray) {
            map.putArray(key, value.toWritableArray())
        } else if (value is Boolean) {
            map.putBoolean(key, value)
        } else if (value is Int) {
            map.putInt(key, value)
        } else if (value is Double) {
            map.putDouble(key, value)
        } else if (value is String) {
            map.putString(key, value)
        } else {
            map.putString(key, value.toString())
        }
    }
    return map
}

@Throws(JSONException::class)
fun JSONArray.toWritableArray(): WritableArray? {
    val array = Arguments.createArray()
    for (i in 0 until length()) {
        val value = this[i]
        if (value is JSONObject) {
            array.pushMap(value.toWritableMap())
        } else if (value is JSONArray) {
            array.pushArray(value.toWritableArray())
        } else if (value is Boolean) {
            array.pushBoolean(value)
        } else if (value is Int) {
            array.pushInt(value)
        } else if (value is Double) {
            array.pushDouble(value)
        } else if (value is String) {
            array.pushString(value)
        } else {
            array.pushString(value.toString())
        }
    }
    return array
}

@Throws(JSONException::class)
fun ReadableMap.toJSONObject(): JSONObject {
    val map = JSONObject()
    val iterator = entryIterator
    while (iterator.hasNext()) {
        val entry = iterator.next()
        val type = this.getType(entry.key)
        if (type == ReadableType.Map) {
            (entry.value as? ReadableMap)?.let {
                map.put(entry.key, it.toJSONObject())
            }
        } else if (type == ReadableType.Array) {
            (entry.value as? ReadableArray)?.let {
                map.put(entry.key, it.toJSONArray())
            }
        } else if (type == ReadableType.Boolean) {
            (entry.value as? Boolean)?.let {
                map.put(entry.key, it)
            }
        } else if (type == ReadableType.Number) {
            (entry.value as? Double)?.let {
                map.put(entry.key, it)
            }
        } else if (type == ReadableType.String) {
            (entry.value as? String)?.let {
                map.put(entry.key, it)
            }
        }
    }
    return map
}

@Throws(JSONException::class)
fun ReadableArray.toJSONArray(): JSONArray {
    val array = JSONArray()
    for (i in 0 until size()) {
        val type = this.getType(i)
        if (type == ReadableType.Map) {
            this.getMap(i)?.let {
                array.put(it.toJSONObject())
            }
        } else if (type == ReadableType.Array) {
            this.getArray(i)?.let {
                array.put(it.toJSONArray())
            }
        } else if (type == ReadableType.Boolean) {
            array.put(this.getBoolean(i))
        } else if (type == ReadableType.Number) {
            array.put(this.getDouble(i))
        } else if (type == ReadableType.String) {
            array.put(this.getString(i))
        }
    }
    return array
}

private val visitorProfileFriendlyNames = mapOf<String, String>(
        "flags" to "booleans",
        "flag_lists" to "arraysOfBooleans",
        "metrics" to "numbers",
        "metric_lists" to "arraysOfNumbers",
        "metric_sets" to "tallies",
        "properties" to "strings",
        "property_lists" to "arraysOfStrings",
        "property_sets" to "setsOfStrings",
        "current_visit" to "currentVisit"
)

internal fun VisitorProfile.Companion.toFriendlyJson(visitorProfile: VisitorProfile): JSONObject {
    return toJson(visitorProfile).let { visitorJson ->
        visitorJson.apply {
            // Rename the top level keys
            this.renameAll(visitorProfileFriendlyNames)

            this.optJSONObject("currentVisit")?.let { currentVisitJson ->
                // Rename the same keys in current Visit
                currentVisitJson.renameAll(visitorProfileFriendlyNames)
                this.put("currentVisit", currentVisitJson)
            }
        }
    }
}

internal fun JSONObject.renameAll(names: Map<String, String>) {
    names.entries.forEach { entry ->
        this.rename(entry.key, entry.value)
    }
}

internal fun JSONObject.rename(oldKey: String, newKey: String) {
    this.opt(oldKey)?.let {
        this.put(newKey, it)
        this.remove(oldKey)
    }
}