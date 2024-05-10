import { NativeModules } from 'react-native';
const { TealiumReactInstallReferrerAttribution } = NativeModules;

export default class TealiumInstallReferrerAttribution {

    /**
     * iOS only. Configures the Tealium Attrubtion module in Tealium
     * Swift library.
     * @param config configuration properties for Attribution modile
     */
    static configure(config) {
        if (Platform.OS === 'ios') {
            if (config === null || config === undefined) {
                TealiumReactInstallReferrerAttribution.configure({})
            } else {
                TealiumReactInstallReferrerAttribution.configure(config)
            }
        }
    }

    /**
     * Android only. Clears values and removes ad information from
     * the data layer related to Ad Identifier module in Tealium
     * Android library.
     */
    static removeAdInfo() {
        if (Platform.OS === 'android') {
            TealiumReactInstallReferrerAttribution.removeAdInfo()
        }
    }
    
    /**
     * Android only. Clears values and removes app set ID from 
     * the data layer related to Ad Identifier modules in Tealium
     * Android library.
     */
    static removeAppSetIdInfo() {
        if (Platform.OS === 'android') {
            TealiumReactInstallReferrerAttribution.removeAppSetIdInfo()
        }
    }
}