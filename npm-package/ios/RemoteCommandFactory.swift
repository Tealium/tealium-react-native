//
//  RemoteCommandFactory.swift
//  tealium-react-native
//
//  Created by James Keith on 08/04/2021.
//

import Foundation
import TealiumSwift

public protocol RemoteCommandFactory {
    var name: String { get }
    func create() -> RemoteCommand
}
