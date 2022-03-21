//
//  TealiumReactNative.swift
//  TealiumReactNative
//
//  Created by Christina S on 12/10/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import TealiumSwift
import tealium_react_native

@objc(TealiumReactNative)
public class TealiumReactNative: RCTEventEmitter {
    
    static var tealium: Tealium?
    private static var config: TealiumConfig?
    
    static var visitorServiceDelegate: VisitorServiceDelegate = VisitorDelegate()
    static var consentExpiryCallback: (([Any]) -> Void)?
    static var remoteCommandFactories = [String: RemoteCommandFactory]()
    static var optionalModules = [OptionalModule]()

    @objc
    public static var consentStatus: String {
        get {
            tealium?.consentManager?.userConsentStatus.rawValue ?? "unknown"
        }
    }
    
    @objc
    public static var consentCategories: [String] {
        get {
            var converted = [String]()
            tealium?.consentManager?.userConsentCategories?.forEach {
                converted.append($0.rawValue)
            }
            return converted
        }
    }
    
    @objc
    public static var visitorId: String? {
        get {
            tealium?.visitorId
        }
    }
    
    @objc
    public static var sessionId: String? {
        get {
            tealium?.dataLayer.sessionId
        }
    }
    
    public static var instance: Tealium? {
        get {
            tealium
        }
    }

    override init() {
        super.init()
        EventEmitter.shared.registerEventEmitter(eventEmitter: self)
    }
    
    public static func registerRemoteCommandFactory(_ factory: RemoteCommandFactory) {
        remoteCommandFactories[factory.name] = factory
    }

    public static func registerOptionalModule(_ module: OptionalModule) {
        optionalModules.append(module)
    }
    
    @objc
    public override static func requiresMainQueueSetup() -> Bool {
        return false
    }
    
    @objc
    public override func supportedEvents() -> [String] {
        return EventEmitter.shared.allEvents
    }
    
    @objc
    public static func initialize(_ config: [String: Any], _ completion: @escaping (Bool) -> Void) {
        guard let localConfig = tealiumConfig(from: config) else {
            return completion(false)
        }
        
        optionalModules.forEach { module in
            module.configure(config: localConfig)
        }
        
        TealiumReactNative.config = localConfig.copy
        tealium = Tealium(config: localConfig) { _ in
            if let remoteCommands = self.tealium?.remoteCommands,
               let remoteCommandsArray = config[.remoteCommands] as? [Any] {
                
                let commands = remoteCommandsFrom(remoteCommandsArray)
                commands.forEach {
                    remoteCommands.add($0)
                }
            }
        
            completion(true)
        }
    }

    @objc
    public static func track(_ dispatch: [String: Any]) {
        guard let track = dispatchFrom(dispatch) else {
            return
        }
        tealium?.track(track)
    }
    
    @objc
    public static func terminateInstance() {
        guard let config = config else {
            return
        }
        TealiumInstanceManager.shared.removeInstance(config: config)
        tealium = nil
    }

    @objc
    public static func addToDataLayer(data: NSDictionary?, expiry: String) {
        guard let data = data as? [String: Any] else {
            return
        }
        tealium?.dataLayer.add(data: data, expiry: expiryFrom(expiry))
    }

    @objc
    public static func removeFromDataLayer(keys: [String]) {
        tealium?.dataLayer.delete(for: keys)
    }
    
    @objc
    public static func getFromDataLayer(key: String) -> Any? {
        tealium?.dataLayer.all[key]
    }

    @objc
    public static func deleteFromDataLayer(keys: [String]) {
        tealium?.dataLayer.delete(for: keys)
    }

    @objc
    public static func addRemoteCommand(id: String) {
        let remoteCommand = self.remoteCommandFor(id)
        tealium?.remoteCommands?.add(remoteCommand)
    }

    @objc
    public static func removeRemoteCommand(id: String) {
        tealium?.remoteCommands?.remove(commandWithId: id)
    }
    
    @objc
    public static func setConsentStatus(status: String) {
        if status == TealiumReactConstants.consented {
            tealium?.consentManager?.userConsentStatus = .consented
        } else {
            tealium?.consentManager?.userConsentStatus = .notConsented
        }
    }
        
    @objc
    public static func setConsentCategories(categories: [String]) {
        tealium?.consentManager?.userConsentCategories = TealiumConsentCategories.consentCategoriesStringArrayToEnum(categories)
    }
    
    @objc
    public static func joinTrace(id: String) {
        tealium?.joinTrace(id: id)
    }
        
    @objc
    public static func leaveTrace() {
        tealium?.leaveTrace()
    }
    
}

class VisitorDelegate: VisitorServiceDelegate {
    public func didUpdate(visitorProfile: TealiumVisitorProfile) {
        EventEmitter.shared.dispatch(name: TealiumReactConstants.Events.visitorService.rawValue,
                                     body: convert(visitorProfile))
    }
    
    private func convert(_ visitorProfile: TealiumVisitorProfile) -> [String: Any] {
        typealias Visitor = TealiumReactConstants.Visitor
        let visit: [String: Any?] = [
            Visitor.dates: visitorProfile.currentVisit?.dates,
            Visitor.booleans: visitorProfile.currentVisit?.booleans,
            Visitor.arraysOfBooleans: visitorProfile.currentVisit?.arraysOfBooleans,
            Visitor.numbers: visitorProfile.currentVisit?.numbers,
            Visitor.arraysOfNumbers: visitorProfile.currentVisit?.arraysOfNumbers,
            Visitor.tallies: visitorProfile.currentVisit?.tallies,
            Visitor.strings: visitorProfile.currentVisit?.strings,
            Visitor.arraysOfStrings: visitorProfile.currentVisit?.arraysOfStrings,
            // Sets cannot be serialized to JSON, so convert to array first
            Visitor.setsOfStrings: visitorProfile.currentVisit?.setsOfStrings.map({ (stringSet) -> [String: [String]] in
                var newValue = [String: [String]]()
                stringSet.forEach {
                    newValue[$0.key] = Array($0.value)
                }
                return newValue
            })
        ]
        let visitor: [String: Any?] = [
            Visitor.audiences: visitorProfile.audiences,
            Visitor.badges: visitorProfile.badges,
            Visitor.dates: visitorProfile.dates,
            Visitor.booleans: visitorProfile.booleans,
            Visitor.arraysOfBooleans: visitorProfile.arraysOfBooleans,
            Visitor.numbers: visitorProfile.numbers,
            Visitor.arraysOfNumbers: visitorProfile.arraysOfNumbers,
            Visitor.tallies: visitorProfile.tallies,
            Visitor.strings: visitorProfile.strings,
            Visitor.arraysOfStrings: visitorProfile.arraysOfStrings,
            // Sets cannot be serialized to JSON, so convert to array first
            Visitor.setsOfStrings: visitorProfile.setsOfStrings.map({ (stringSet) -> [String: [String]] in
                var newValue = [String: [String]]()
                stringSet.forEach {
                    newValue[$0.key] = Array($0.value)
                }
                return newValue
            }),
            Visitor.currentVisit: visit.compactMapValues { $0 }
        ]
        return visitor.compactMapValues({$0})
    }
    
}
