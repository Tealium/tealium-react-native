import { NativeModules } from 'react-native';
const { TealiumReactAttribution } = NativeModules;

export default class TealiumAttribution {

    /**
     * iOS only. Configures the Tealium Attrubtion module in Tealium
     * Swift library.
     * @param config configuration properties for Attribution modile
     */
    static configure(config) {
        if (config === null || config === undefined) {
            TealiumReactAttribution.configure({})
        } else {
            TealiumReactAttribution.configure(config)
        }
    }

    /**
     * Android only. Clears values and removes ad information from
     * the data layer related to Ad Identifier module in Tealium
     * Android library.
     */
    static removeAdInfo() {
        if (Platform.OS === 'android') {
            TealiumReactAttribution.removeAdInfo()
        }
    }
    
    /**
     * Android only. Clears values and removes app set ID from 
     * the data layer related to Ad Identifier modules in Tealium
     * Android library.
     */
    static removeAppSetIdInfo() {
        if (Platform.OS === 'android') {
            TealiumReactAttribution.removeAppSetIdInfo()
        }
    }
}