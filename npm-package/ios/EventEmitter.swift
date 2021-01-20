//
//  EventEmitter.swift
//  TealiumReactNative
//
//  Created by Christina S on 12/11/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation

public class EventEmitter {

    public static var shared = EventEmitter()
    private static var eventEmitter: TealiumReactNative?

    private init() {}
    
    lazy var allEvents: [String] = {
        TealiumReactConstants.Events.allCases.map { $0.rawValue }
    }()

    func registerEventEmitter(eventEmitter: TealiumReactNative) {
        EventEmitter.eventEmitter = eventEmitter
    }

    func dispatch(name: String, body: Any?) {
        EventEmitter.eventEmitter?.sendEvent(withName: name, body: body)
    }

}
