import Foundation
import React
import tealium_react_native_swift

@objc(TealiumReactAppsFlyer)
class TealiumReactAppsflyer: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactAppsFlyer"
    }
    let factory = AppsFlyerRemoteCommandWrapper()
    
    // no longer needed.
    weak var bridge: RCTBridge?
    
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

