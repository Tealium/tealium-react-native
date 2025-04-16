//
//  AdjustRemoteCommandWrapper.swift
//  TealiumReactAdjust
//
//

import Foundation
import TealiumSwift
import TealiumAdjust
import tealium_react_native

class AdjustRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "AdjustRemoteCommand"

    func create() -> RemoteCommand {
        return AdjustRemoteCommand()
    }
}
