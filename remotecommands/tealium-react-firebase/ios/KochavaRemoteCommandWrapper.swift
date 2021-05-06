//
//  KochavaRemoteCommandWrapper.swift
//  tealium-react-firebase
//
//  Created by James Keith on 11/03/2021.
//

import Foundation
import TealiumSwift
import TealiumKochava
import tealium_react_native

//@objc(KochavaRemoteCommandWrapper)
class KochavaRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "FirebaseRemoteCommand"
    
    // Example Customisable
    var configurableSetting: Bool = false
    
    func create() -> RemoteCommand {
        // could pass `configurableSetting` here if required
        return KochavaRemoteCommand()
    }
}
