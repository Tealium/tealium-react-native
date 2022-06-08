import { NativeModules } from 'react-native';
import { AdjustConfig } from './common';

const { TealiumReactAdjust } = NativeModules;

export default class AdjustRemoteCommand {
    static name = "AdjustRemoteCommand";

    static initialize() {
        if (Platform.OS == 'ios') {
            TealiumReactAdjust.initialize();
        }
    }
};