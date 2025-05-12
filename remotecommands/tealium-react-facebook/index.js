import { NativeModules } from 'react-native';

const { TealiumReactFacebook } = NativeModules;

export default class FacebookRemoteCommand {
    static name = "FacebookRemoteCommand";

    static initialize() {
        TealiumReactFacebook.initialize();
    }
};
