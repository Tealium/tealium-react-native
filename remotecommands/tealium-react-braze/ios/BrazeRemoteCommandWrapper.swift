//
//  BrazeRemoteCOmmandWrapper.swift
//  tealium-react-firebase
//
//  Created by Tyler Rister on 03/25/2022.
//

import Foundation
import TealiumSwift
import TealiumBraze
import tealium_react_native_swift

class BrazeRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "BrazeRemoteCommand"

    var configurableSetting: Bool = false
    
    func create() -> RemoteCommand {
        return BrazeRemoteCommand()
    }
}
