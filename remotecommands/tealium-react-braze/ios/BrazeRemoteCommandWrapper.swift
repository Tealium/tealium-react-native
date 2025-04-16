//
//  BrazeRemoteCommandWrapper.swift
//  tealium-react-braze
//
//  Created by Tyler Rister on 03/25/2022.
//

import Foundation
import TealiumSwift
import TealiumBraze
import tealium_react_native

class BrazeRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "BrazeRemoteCommand"

    func create() -> RemoteCommand {
        return BrazeRemoteCommand()
    }
}
