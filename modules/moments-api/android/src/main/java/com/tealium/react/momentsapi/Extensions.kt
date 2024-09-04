package com.tealium.react.momentsapi

import com.facebook.react.bridge.WritableMap
import com.tealium.momentsapi.EngineResponse
import com.tealium.momentsapi.MomentsApiRegion
import com.tealium.react.toWritableMap


fun EngineResponse.toWritableMap(): WritableMap? {
    return EngineResponse.toJson(this).toWritableMap()
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