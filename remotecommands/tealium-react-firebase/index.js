import { NativeModules } from 'react-native';

const { TealiumReactFirebase } = NativeModules;

export default class FirebaseRemoteCommand {
    static name = "FirebaseRemoteCommand";

    static initialize() {
        if (Platform.OS == 'ios') {
            TealiumReactFirebase.initialize();
        }
    }
};
