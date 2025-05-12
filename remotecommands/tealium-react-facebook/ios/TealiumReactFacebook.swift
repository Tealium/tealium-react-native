import Foundation
import React
import tealium_react_native

@objc(TealiumReactFacebook)
class TealiumReactFacebook: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactFacebook"
    }
    let factory = FacebookRemoteCommandWrapper()
    
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
