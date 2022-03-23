//
//  KochavaRemoteCommandWrapper.swift
//  tealium-react-firebase
//
//  Created by James Keith on 11/03/2021.
//

import Foundation
import TealiumSwift
import TealiumFirebase
import tealium_react_native_swift

class FirebaseRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "FirebaseRemoteCommand"
    
    // Example Customisable
    var configurableSetting: Bool = false
    
    func create() -> RemoteCommand {
        // could pass `configurableSetting` here if required
        return FirebaseRemoteCommand()
    }
}
