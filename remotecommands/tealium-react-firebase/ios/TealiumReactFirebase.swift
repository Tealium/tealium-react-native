//
//  TealiumReactFirebase.swift
//  tealium-react-firebase
//
//  Created by James Keith on 11/03/2021.
//

import Foundation
import React
import tealium_react_native_swift

@objc(TealiumReactFirebase)
class TealiumReactFirebase: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactFirebase"
    }
    let factory = FirebaseRemoteCommandWrapper()
    
    // no longer needed.
    // weak var bridge: RCTBridge?
    
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
