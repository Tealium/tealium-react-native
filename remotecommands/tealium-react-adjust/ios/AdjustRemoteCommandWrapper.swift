//
//  AdjustRemoteCommandWrapper.swift
//  TealiumReactAdjust
//
//

import Foundation
import TealiumSwift
import TealiumAdjust
import tealium_react_native_swift

class AdjustRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "AdjustRemoteCommand"

    func create() -> RemoteCommand {
        return AdjustRemoteCommand()
    }
}
