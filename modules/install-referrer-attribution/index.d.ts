import { AttributionConfig, TealiumInstallReferrerAttributionCommon } from "./common";

declare module 'tealium-react-native-install-referrer-attribution'{
    export default TealiumInstallReferrerAttribution;
    class TealiumInstallReferrerAttribution extends TealiumInstallReferrerAttributionCommon {

        /**
         * Configures Tealium Install Referrer Attrubtion module
         * @param config iOS only. Configuration properties for Attribution modile
         */
        public static configure(config?: AttributionConfig): void;

        /**
         * Android only. Clears values and removes ad information from
         * the data layer related to Ad Identifier module in Tealium
         * Android library.
         */
        public static removeAdInfo(): void;

        /**
         * Android only. Clears values and removes app set ID from 
         * the data layer related to Ad Identifier modules in Tealium
         * Android library.
         */
        public static removeAppSetIdInfo(): void;
    }
}