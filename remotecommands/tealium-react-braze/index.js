import { NativeModules } from 'react-native';

const { TealiumReactBraze } = NativeModules;

export default class BrazeRemoteCommand {
    static name = "BrazeRemoteCommand";

    static initialize() {
        if (Platform.OS == 'ios') {
            TealiumReactBraze.initialize();
        }
    }
};
