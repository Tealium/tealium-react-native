import { NativeModules } from 'react-native';

const { TealiumReactAdjust } = NativeModules;

export default class AdjustRemoteCommand {
    static name = "AdjustRemoteCommand";

    static initialize(config) {
        if (Platform.OS == 'ios') {
            TealiumReactAdjust.initialize();
        } else {
            TealiumReactAdjust.initialize(config);
        }
    }
};