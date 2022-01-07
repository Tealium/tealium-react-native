////
////  TealiumReactNativeLocation.swift
////  tealium-react-native-location
////
////  Created by James Keith on 07/01/2022.
////
//
import Foundation
import tealium_react_native
////import tealium_react_native
//
@objc(TealiumReactLocation)
class TealiumReactLocation: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "TealiumReactLocation"
    }

    @objc
    static func requiresMainQueueSetup() -> Bool {
      return false
    }

    override init() {
        super.init()

    }

    @objc(configure:)
    public func configure(_ config: [String: Any]) {
//        Tealium
    }
}
//
//
////class TealiumReactLocationModule: NSObject, OptionalModule {
////
////}
