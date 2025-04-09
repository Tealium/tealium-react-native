import Foundation
import TealiumSwift
import TealiumAppsFlyer
import tealium_react_native

class AppsFlyerRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "AppsFlyerRemoteCommand"
    
    func create() -> RemoteCommand {
        return AppsFlyerRemoteCommand()
    }
}