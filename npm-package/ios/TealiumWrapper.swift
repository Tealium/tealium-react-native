//
//  TealiumWrapper.swift
//  TealiumReactNative
//
//  Created by Christina S on 12/11/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import tealium_react_native

@objc(TealiumWrapper)
class TealiumWrapper: NSObject {
    
    @objc
    static func requiresMainQueueSetup() -> Bool {
      return false
    }
    
    @objc(initialize:callback:)
    public func initialize(_ config: [String: Any], callback: @escaping RCTResponseSenderBlock) {
        TealiumReactNative.initialize(config) { result in
            if result {
                callback([result])
            }
        }
    }
    
    @objc(track:)
    public func track(_ dispatch: [String: Any]) {
        TealiumReactNative.track(dispatch)
    }
    
    @objc(terminateInstance)
    public func terminateInstance() {
        TealiumReactNative.terminateInstance()
    }

    @objc(addToDataLayer:expiry:)
    public func addToDataLayer(_ data: NSDictionary?, expiry: String) {
        TealiumReactNative.addToDataLayer(data: data, expiry: expiry)
    }

    @objc(removeFromDataLayer:)
    public func removeFromDataLayer(_ keys: [String]) {
        TealiumReactNative.removeFromDataLayer(keys: keys)
    }
    
    @objc(getFromDataLayer:callback:)
    public func getFromDataLayer(_ key: String, callback: RCTResponseSenderBlock) {
        guard let item = TealiumReactNative.getFromDataLayer(key: key) else {
            return
        }
        callback([item])
    }

    @objc(deleteFromDataLayer:)
    public func deleteFromDataLayer(_ keys: [String]) {
        TealiumReactNative.deleteFromDataLayer(keys: keys)
    }

    @objc(addRemoteCommand:)
    public func addRemoteCommand(_ id: String) {
        TealiumReactNative.addRemoteCommand(id: id)
    }
    
    @objc(removeRemoteCommand:)
    public func removeRemoteCommand(_ id: String) {
        TealiumReactNative.removeRemoteCommand(id: id)
    }
    
    @objc(getConsentStatus:)
    public func getConsentStatus(_ callback: RCTResponseSenderBlock) {
        callback([TealiumReactNative.consentStatus])
    }
    
    @objc(setConsentStatus:)
    public func setConsentStatus(_ status: String) {
        TealiumReactNative.setConsentStatus(status: status)
    }
    
    @objc(getConsentCategories:)
    public func getConsentCategories(_ callback: RCTResponseSenderBlock) {
        callback([TealiumReactNative.consentCategories])
    }
    
    @objc(setConsentCategories:)
    public func setConsentCategories(_ categories: [String]) {
        TealiumReactNative.setConsentCategories(categories: categories)
    }

    @objc(joinTrace:)
    public func joinTrace(_ id: String) {
        TealiumReactNative.joinTrace(id: id)
    }

    @objc(leaveTrace)
    public func leaveTrace() {
        TealiumReactNative.leaveTrace()
    }

    @objc(getVisitorId:)
    public func getVisitorId(_ callback: RCTResponseSenderBlock) {
        guard let visitorId = TealiumReactNative.visitorId else {
            return
        }
        callback([visitorId])
    }
    
    @objc(getSessionId:)
    public func getSessionId(_ callback: RCTResponseSenderBlock) {
        guard let sessionId = TealiumReactNative.sessionId else {
            return
        }
        callback([sessionId])
    }
}
