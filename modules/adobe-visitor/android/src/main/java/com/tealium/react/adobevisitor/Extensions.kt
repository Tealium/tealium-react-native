package com.tealium.react.adobevisitor

import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.tealium.adobe.api.AdobeVisitor

fun AdobeVisitor.toReadableMap(): ReadableMap {
    val map = Arguments.createMap()
    map.putString("experienceCloudId", this.experienceCloudId)
    map.putInt("idSyncTtl", this.idSyncTTL)
    map.putInt("region", this.region)
    map.putString("blob", this.blob)
    map.putString("nextRefresh", this.nextRefresh.time.toString()) // convert to...?
    return map
}