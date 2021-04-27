//
//  TealiumReactExtensions.swift
//  TealiumReactNative
//
//  Created by Christina S on 1/4/21.
//  Copyright © 2021 Facebook. All rights reserved.
//

import Foundation
import TealiumSwift

extension TealiumReactNative {
    
    public static func tealiumConfig(from dictionary: [String: Any]) -> TealiumConfig? {
        guard let account = dictionary[.account] as? String,
              let profile = dictionary[.profile] as? String,
              let environment = dictionary[.environment] as? String else {
            return nil
        }
        
        let localConfig = TealiumConfig(account: account,
                                        profile: profile,
                                        environment: environment,
                                        dataSource: dictionary[.dataSource] as? String)
        
        if let policyString = dictionary[.consentPolicy] as? String,
           let policy = consentPolicyFrom(policyString) {
            localConfig.consentPolicy = policy
            localConfig.consentLoggingEnabled =  dictionary[.consentLoggingEnabled] as? Bool ?? true
            localConfig.onConsentExpiration = {
                EventEmitter.shared
                    .dispatch(name: TealiumReactConstants.Events.consent.rawValue,
                              body: nil)

            }
        }
        
        if let consentExpiry = dictionary[.consentExpiry] as? [String: Any],
            let time = consentExpiry[.time] as? Int,
            let unit = consentExpiry[.unit] as? String {
            var unitType = TimeUnit.days

            switch unit.lowercased() {
            case TealiumReactConstants.minutes:
                    unitType = .minutes
            case TealiumReactConstants.hours:
                    unitType = .hours
            case TealiumReactConstants.months:
                    unitType = .months
                default:
                    break
            }
            localConfig.consentExpiry = (time: time, unit: unitType)
        }
        
        if let customVisitorId = dictionary[.customVisitorId] as? String {
            localConfig.existingVisitorId = customVisitorId
        }
        
        if let lifecycleAutoTrackingEnabled = dictionary[.lifecycleAutoTrackingEnabled] as? Bool {
            localConfig.lifecycleAutoTrackingEnabled = lifecycleAutoTrackingEnabled
        }
        
        var configDispatchers = [Dispatcher.Type]()
        var configCollectors = [Collector.Type]()
        
        if let dispatchers = dictionary[.dispatchers] as? [String] {
            if dispatchers.contains(TealiumReactConstants.tagManagement) {
                configDispatchers.append(Dispatchers.TagManagement)
            }
            
            if dispatchers.contains(TealiumReactConstants.collect) {
                configDispatchers.append(Dispatchers.Collect)
            }
            
            if dispatchers.contains(TealiumReactConstants.remoteCommands) {
                configDispatchers.append(Dispatchers.RemoteCommands)
                localConfig.remoteAPIEnabled = true
            }
        }
        
        if let collectors = dictionary[.collectors] as? [String] {
            if collectors.contains(TealiumReactConstants.appData) {
                configCollectors.append(Collectors.AppData)
            }
            
            if collectors.contains(TealiumReactConstants.connectivity) {
                configCollectors.append(Collectors.Connectivity)
            }
            
            if collectors.contains(TealiumReactConstants.deviceData) {
                configCollectors.append(Collectors.Device)
            }
            
            if collectors.contains(TealiumReactConstants.lifecycle) {
                configCollectors.append(Collectors.Lifecycle)
            }
        }
        
        if let useRemoteLibrarySettings = dictionary[.useRemoteLibrarySettings] as? Bool {
            localConfig.shouldUseRemotePublishSettings = useRemoteLibrarySettings
        }
        
        if let logLevel = dictionary[.logLevel] as? String {
            localConfig.logLevel = logLevelFrom(logLevel)
        }
        
        if let overrideCollectURL = dictionary[.overrideCollectURL] as? String {
            localConfig.overrideCollectURL = overrideCollectURL
        }
        
        if let overrideTagManagementURL = dictionary[.overrideTagManagementURL] as? String {
            localConfig.tagManagementOverrideURL = overrideTagManagementURL
        }
        
        if let overrideCollectBatchURL = dictionary[.overrideCollectBatchURL] as? String {
            localConfig.overrideCollectBatchURL = overrideCollectBatchURL
        }
        
        if let overrideLibrarySettingsURL = dictionary[.overrideLibrarySettingsURL] as? String {
            localConfig.publishSettingsURL = overrideLibrarySettingsURL
        }
        
        localConfig.qrTraceEnabled = dictionary[.qrTraceEnabled] as? Bool ?? true
        localConfig.deepLinkTrackingEnabled = dictionary[.deepLinkTrackingEnabled] as? Bool ?? true
        localConfig.lifecycleAutoTrackingEnabled = dictionary[.lifecycleAutotrackingEnabled] as? Bool ?? true
        
        if dictionary[.visitorServiceEnabled] as? Bool == true {
            configCollectors.append(Collectors.VisitorService)
            localConfig.visitorServiceDelegate = visitorServiceDelegate
        }
        
        localConfig.memoryReportingEnabled = dictionary[.memoryReportingEnabled] as? Bool ?? true
        localConfig.collectors = configCollectors
        localConfig.dispatchers = configDispatchers
        
        return localConfig
    }
    
    public static func consentPolicyFrom(_ policy: String) -> TealiumConsentPolicy? {
        switch policy.lowercased() {
            case TealiumReactConstants.ccpa:
                return .ccpa
            case TealiumReactConstants.gdpr:
                return .gdpr
            default:
                return nil
        }
    }
    
    public static func expiryFrom(_ expiry: String) -> Expiry {
        switch expiry.lowercased() {
            case TealiumReactConstants.forever:
                return .forever
            case TealiumReactConstants.restart:
                return .untilRestart
            default:
                return .session
        }
    }
    
    public static func dispatchFrom(_ payload: [String: Any]) -> TealiumDispatch? {
        let type = payload[.type] as? String ?? TealiumReactConstants.event
        let dataLayer = payload[.dataLayer] as? [String: Any]
        switch type.lowercased() {
        case TealiumReactConstants.view:
            guard let viewName = payload[.viewName] as? String else {
                return nil
            }
            return TealiumView(viewName, dataLayer: dataLayer)
        default:
            guard let eventName = payload[.eventName] as? String else {
                return nil
            }
            return TealiumEvent(eventName, dataLayer: dataLayer)
        }
    }
    
    public static func logLevelFrom(_ logLevel: String) -> TealiumLogLevel {
        switch logLevel.lowercased() {
        case TealiumReactConstants.dev:
            return .info
        case TealiumReactConstants.qa:
            return .debug
        case TealiumReactConstants.prod:
            return .error
        case TealiumReactConstants.silent:
            return .silent
        default:
            return .error
        }
    }
    
    public static func remoteCommandsFrom(_ commands: [Any]) -> [RemoteCommandProtocol] {
        var remoteCommands = [RemoteCommandProtocol]()
        commands.forEach { commandPayload in
            
            guard let commandPayload = commandPayload as? [String: Any],
                  let id = commandPayload[.id] as? String else {
                return
            }
            
            var command: RemoteCommand?
            if commandPayload[.callback] == nil {
                // no callback look for a factory
                command = remoteCommandFactories[id]?.create()
            } else {
                // callback was provided
                command = remoteCommandFor(id)
            }
                
            guard let remoteCommand = command else {
                return
            }
            
            if let path = commandPayload[.path] as? String {
                remoteCommand.type = .local(file: path, bundle: nil)
            } else if let url = commandPayload[.url] as? String {
                remoteCommand.type = .remote(url: url)
            }
            remoteCommands.append(remoteCommand)
        }
        return remoteCommands
    }
    
    public static func remoteCommandFor(_ id: String) -> RemoteCommand {
        return RemoteCommand(commandId: id, description: nil) { response in
            EventEmitter.shared.dispatch(name: TealiumReactConstants.Events.remoteCommand.rawValue,
                                                 body: response.payload)
        }
    }
}

extension Dictionary where Key: ExpressibleByStringLiteral {
    subscript(key: TealiumReactConstants.Config) -> Value? {
        get {
            return self[key.rawValue as! Key]
        }
    }
    subscript(key: TealiumReactConstants.Dispatch) -> Value? {
        get {
            return self[key.rawValue as! Key]
        }
    }
    subscript(key: TealiumReactConstants.RemoteCommand) -> Value? {
        get {
            return self[key.rawValue as! Key]
        }
    }
}
