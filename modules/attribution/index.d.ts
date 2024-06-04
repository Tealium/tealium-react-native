import { AttributionConfig, TealiumAttributionCommon } from "./common";

declare module 'tealium-react-native-attribution'{
    export default TealiumAttribution;
    class TealiumAttribution extends TealiumAttributionCommon {

        /**
         * Configures Tealium Attrubtion module.
         * @param config Configuration properties for Attribution modile
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