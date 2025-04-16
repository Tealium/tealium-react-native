//
//  FirebaseRemoteCommandWrapper.swift
//  tealium-react-firebase
//
//  Created by James Keith on 11/03/2021.
//

import Foundation
import TealiumSwift
import TealiumFirebase
import tealium_react_native

class FirebaseRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "FirebaseRemoteCommand"
    
    func create() -> RemoteCommand {
        return FirebaseRemoteCommand()
    }
}
