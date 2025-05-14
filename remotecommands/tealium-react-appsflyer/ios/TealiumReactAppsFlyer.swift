import Foundation
import React
import tealium_react_native

@objc(TealiumReactAppsFlyer)
class TealiumReactAppsflyer: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactAppsFlyer"
    }
    let factory = AppsFlyerRemoteCommandWrapper()
    
    @objc
    static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    override init() {
        super.init()
        TealiumReactNative.registerRemoteCommandFactory(factory)
    }
    
    @objc(initialize)
    public func initialize() {
        TealiumReactNative.registerRemoteCommandFactory(factory)
    }
    
}

