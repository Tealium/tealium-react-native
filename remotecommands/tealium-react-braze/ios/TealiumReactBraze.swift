//
//  TealiumReactBraze.swift
//  tealium-react-firebase
//
//  Created by Tyler Rister on 03/25/2022.
//

import Foundation
import React
import tealium_react_native_swift

@objc(TealiumReactBraze)
class TealiumReactBraze: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactBraze"
    }
    let factory = BrazeRemoteCommandWrapper()
    
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
    
    @objc(configure:)
    public func configure(_ options: [String: Any]) {
        if let configurable = options["configurable"] as? Bool {
            factory.configurableSetting = configurable
        }
    }
    
    
}
