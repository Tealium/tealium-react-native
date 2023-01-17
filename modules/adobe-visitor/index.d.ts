import { TealiumAdobeVisitorConfig, TealiumAdobeVisitorCommon, AuthState} from './common';

declare module 'tealium-react-native-adobe-visitor' {
	export default TealiumAdobeVisitor;
	class TealiumAdobeVisitor extends TealiumAdobeVisitorCommon {

        /**
         * Configres the Tealium Adobe Visitor module
         * @param config configuration properties for Adocbe Visitor module
         */
        public static configure(config: TealiumAdobeVisitorConfig): void;

        /**
         * Sets an existing ECID for a known visitor
         */
        public static linkExistingEcidToKnownIdentifier(knownId: String, adobeDataProviderId: String, authState: AuthState, callback:(response: any) => void): void;

        /**
         * Resets current visitor
         */
        public static resetVisitor(): void;

        /**
         * Decorates url with ECID visitor data
         */
        public static decorateUrl(url: String, callback:(response: String) => void): void;

    }
}