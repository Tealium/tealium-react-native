import Foundation
import TealiumSwift
import TealiumFacebook
import tealium_react_native

class FacebookRemoteCommandWrapper: RemoteCommandFactory {
    var name: String = "FacebookRemoteCommand"

    func create() -> RemoteCommand {
        return FacebookRemoteCommand()
    }
}
