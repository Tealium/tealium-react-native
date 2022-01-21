////
////  TealiumReactNativeLocation.swift
////  tealium-react-native-location
////
////  Created by James Keith on 07/01/2022.
////

import Foundation
import tealium_react_native_swift
import TealiumSwift

@objc(TealiumReactLocation)
class TealiumReactLocation: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "TealiumReactLocation"
    }
    let module = TealiumReactLocationModule()
    
    private let KEY_GEOFENCE_URL = "geofenceUrl"
    private let KEY_GEOFENCE_FILE = "geofenceFile"
    private let KEY_ACCURACY = "accuracy"
    private let KEY_INTERVAL = "interval"
    private let KEY_LATITUDE = "lat"
    private let KEY_LONGITUDE = "lng"

    @objc
    static func requiresMainQueueSetup() -> Bool {
      return false
    }

    override init() {
        super.init()
        TealiumReactNative.registerOptionalModule(module)
    }

    @objc(configure:)
    public func configure(_ config: [String: Any]) {
        if let accuracy = config[KEY_ACCURACY] as? String {
            setAccuracyString(accuracy)
        }
        if let accuracy = config[KEY_ACCURACY] as? Bool {
            setAccuracyBoolean(accuracy)
        }
        
        if let url = config[KEY_GEOFENCE_URL] as? String {
            setGeofenceUrl(url)
        }
        
        if let path = config[KEY_GEOFENCE_FILE] as? String {
            setGeofenceFile(path)
        }
    }
    
    @objc(setAccuracyBoolean:)
    public func setAccuracyBoolean(_ isHighAccuracy: Bool) {
        module.setAccuracyBoolean(isHighAccuracy)
    }
    
    @objc(setAccuracyString:)
    public func setAccuracyString(_ isHighAccuracy: String) {
        module.setAccuracyString(isHighAccuracy)
    }
    
    @objc(setGeofenceUrl:)
    public func setGeofenceUrl(_ url: String) {
        module.setGeofenceUrl(url)
    }
    
    @objc(setGeofenceFile:)
    public func setGeofenceFile(_ path: String) {
        module.setGeofenceFile(path)
    }
    
    
    @objc(lastLocation:)
    public func lastLocation(_ callback: RCTResponseSenderBlock) {
        var newData = [String: Any]()
        
        if let location = TealiumReactNative.instance?.location?.lastLocation,
           location.coordinate.latitude != 0.0 && location.coordinate.longitude != 0.0 {
            newData = [KEY_LATITUDE: "\(location.coordinate.latitude)",
                       KEY_LONGITUDE: "\(location.coordinate.longitude)"]
        }
        
        callback([newData])
    }
    
    @objc(startLocationTracking)
    public func startLocationTracking() {
        TealiumReactNative.instance?.location?.startLocationUpdates()
    }
    
    @objc(stopLocationTracking)
    public func stopLocationTracking() {
        TealiumReactNative.instance?.location?.stopLocationUpdates()
    }
}


class TealiumReactLocationModule: NSObject, OptionalModule {
    private var highAccuracy = false
    private var geofenceUrl: String? = nil
    private var geofenceFile: String? = nil
    
    func configure(config: TealiumConfig) {
        config.collectors?.append(Collectors.Location)
        
        config.useHighAccuracy = highAccuracy
        
        if let geofenceUrl = geofenceUrl {
            config.geofenceUrl = geofenceUrl
        }
        
        if let geofenceFile = geofenceFile {
            config.geofenceFileName = geofenceFile
        }
    }
    
    public func setAccuracyBoolean(_ isHighAccuracy: Bool) {
        highAccuracy = isHighAccuracy
    }
    
    public func setAccuracyString(_ isHighAccuracy: String) {
        highAccuracy = isHighAccuracy == "high"
    }
    
    
    public func setGeofenceUrl(_ url: String) {
        geofenceUrl = url
    }
    
    public func setGeofenceFile(_ path: String) {
        geofenceFile = path
    }
}
