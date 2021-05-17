import { NativeModules } from 'react-native';

const { TealiumReactBraze } = NativeModules;

export default class BrazeRemoteCommand {
    static name = "BrazeRemoteCommand"

    static setSessionHandlingEnabled(enabled) {
        TealiumReactBraze.setSessionHandlingEnabled(enabled)
    }

    static setSessionHandlingBlacklist(classList) {
        TealiumReactBraze.setSessionHandlingBlacklist(classList)
    }

    static setRegisterInAppMessenger(enabled) {
        TealiumReactBraze.setRegisterInAppMessenger(enabled)
    }

    static setInAppMessengerBlacklist(classList) {
        TealiumReactBraze.setInAppMessengerBlacklist(classList)
    }
}; 
