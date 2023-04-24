import { NativeModules } from 'react-native';

const { TealiumReactAppsFlyer } = NativeModules;

export default class AppsFlyerRemoteCommand {
    static name = "AppsFlyerRemoteCommand";

    static initialize(devKey) {
        if (Platform.OS == 'ios') {
            TealiumReactAppsFlyer.initialize();
        } else {
            TealiumReactAppsFlyer.initialize(devKey);
        }
        
    }
};