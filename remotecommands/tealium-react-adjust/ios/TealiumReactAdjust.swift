import Foundation
import React
import tealium_react_native

@objc(TealiumReactAdjust)
class TealiumReactAdjust: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactAdjust"
    }
    let factory = AdjustRemoteCommandWrapper()
    
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
