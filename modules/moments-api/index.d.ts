import { MomentsApiConfig, TealiumMomentsApiCommon} from './common';

declare module 'tealium-react-native-moments-api' {
	export default TealiumMomentsApi;
	class TealiumMomentsApi extends TealiumMomentsApiCommon {

        /**
         * Configures the Tealium Moments API module
         * @param config configuration properties for Moments API module
         */
        public static configure(config: MomentsApiConfig): void;

        /**
         * Retrieves URL parameters to be manually appended to a URL for special URL schemes (e.g. Angular)
         */
        public static fetchEngineResponse(engineId: String, callback:(response: String) => void): void;
    }
}