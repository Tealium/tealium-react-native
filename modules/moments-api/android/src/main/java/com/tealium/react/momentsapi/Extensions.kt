package com.tealium.react.momentsapi

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap

fun EngineResponse.toReadableMap(): ReadableMap {
    val map = Arguments.createMap()
    map.putString("attributes", this.strings)
    map.putInt("audiences", this.audiences)
    map.putInt("booleans", this.booleans)
    map.putString("dates", this.dates)
    map.putDouble("badges", this.badges)
    map.putDouble("numbers", this.numbers)
    return map
}