package com.tealium.react.momentsapi

import com.facebook.react.bridge.WritableMap
import com.tealium.momentsapi.EngineResponse
import com.tealium.momentsapi.MomentsApiRegion
import com.tealium.react.toWritableMap
import org.json.JSONObject

fun EngineResponse.toWritableMap(): WritableMap? {
    return EngineResponse.toFriendlyJson(this).toWritableMap()
}

fun regionFromString(region: String): MomentsApiRegion {
    return when (region.lowercase()) {
        "germany" -> MomentsApiRegion.Germany
        "us_east" -> MomentsApiRegion.UsEast
        "sydney" -> MomentsApiRegion.Sydney
        "oregon" -> MomentsApiRegion.Oregon
        "tokyo" -> MomentsApiRegion.Tokyo
        "hong_kong" -> MomentsApiRegion.HongKong
        else -> MomentsApiRegion.Custom(region)
    }
}

private val engineResponseFriendlyNames = mapOf<String, String>(
    "flags" to "booleans",
    "metrics" to "numbers",
    "properties" to "strings",
)

internal fun EngineResponse.Companion.toFriendlyJson(engineResponse: EngineResponse): JSONObject {
    return toJson(engineResponse).let { engineJson ->
        engineJson.apply {
            // Rename the top level keys
            this.renameAll(engineResponseFriendlyNames)
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