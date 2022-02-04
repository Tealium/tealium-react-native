//
//  OptionalModule.swift
//  tealium-react-native
//
//  Created by James Keith on 22/12/2021.
//

import Foundation
import TealiumSwift

public protocol OptionalModule {
    func configure(config: TealiumConfig) -> Void
}
