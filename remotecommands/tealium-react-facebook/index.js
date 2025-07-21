import { NativeModules, Platform } from 'react-native';

const { TealiumReactFacebook } = NativeModules;

export default class FacebookRemoteCommand {
    static name = "FacebookRemoteCommand";

    static initialize() {
        if (Platform.OS == 'ios') {
            TealiumReactFacebook.initialize();
        }
    }
};
