import { NativeModules, Platform } from 'react-native';

const { TealiumReactCrashReporter } = NativeModules;

export default class TealiumCrashReporter {

    /**
     * Initializes the Tealium Crash Reporter module
     * @param truncateStackTrack Android only. Option to truncate the Crash Reporter's stack trace
     */
    static initialize(truncateStackTrack) {
        if (Platform.OS == 'ios') {
            TealiumReactCrashReporter.initialize();
        } else {
            if(truncateStackTrack == undefined) {
                TealiumReactCrashReporter.initialize(false);
            } else {
                TealiumReactCrashReporter.initialize(truncateStackTrack);
            }
        }
    }
}