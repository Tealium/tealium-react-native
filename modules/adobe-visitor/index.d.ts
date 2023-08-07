import { TealiumAdobeVisitorConfig, TealiumAdobeVisitorCommon, AuthState} from './common';

declare module 'tealium-react-native-adobe-visitor' {
	export default TealiumAdobeVisitor;
	class TealiumAdobeVisitor extends TealiumAdobeVisitorCommon {

        /**
         * Configures the Tealium Adobe Visitor module
         * @param config configuration properties for Adobe Visitor module
         */
        public static configure(config: TealiumAdobeVisitorConfig): void;

        /**
         * Sets an existing ECID for a known visitor
         */
        public static linkEcidToKnownIdentifier(knownId: String, adobeDataProviderId: String, authState?: AuthState | number, callback?:(response: any) => void): void;

        /**
         * Fetch Adobe Visitor
         */
        public static getAdobeVisitor(callback:(response: String) => void): void;

        /**
         * Resets current visitor
         */
        public static resetVisitor(): void;

        /**
         * Decorates url with ECID visitor data
         */
        public static decorateUrl(url: String, callback:(response: String) => void): void;

        /**
         * Retrieves URL parameters to be manually appended to a URL for special URL schemes (e.g. Angular)
         */
        public static getUrlParameters(callback:(response: any) => void): void;
    }
}