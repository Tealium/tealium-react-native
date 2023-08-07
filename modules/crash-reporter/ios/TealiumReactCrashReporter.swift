import Foundation
import tealium_react_native_swift
import TealiumSwift
import TealiumCrashModule

@objc(TealiumReactCrashReporter)
class TealiumReactCrashReporter: NSObject, RCTBridgeModule {
    static func moduleName() -> String! {
        return "TealiumReactCrashReporter"
    }
    let module = TealiumReactCrashReporterModule()

    @objc
    static func requiresMainQueueSetup() -> Bool {
      return false
    }

    override init() {
        super.init()
        TealiumReactNative.registerOptionalModule(module)
    }

    @objc(initialize)
    public func initialize() {
      TealiumReactNative.registerOptionalModule(module)
    }
}

class TealiumReactCrashReporterModule: NSObject, OptionalModule {
  func configure(config: TealiumConfig) {
    config.collectors?.append(Collectors.Crash)
  }
}