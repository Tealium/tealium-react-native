//
//  TealiumReactBraze.swift
//  tealium-react-braze
//
//  Created by Tyler Rister on 03/25/2022.
//

import Foundation
import React
import tealium_react_native

@objc(TealiumReactBraze)
class TealiumReactBraze: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        "TealiumReactBraze"
    }
    let factory = BrazeRemoteCommandWrapper()
    
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
