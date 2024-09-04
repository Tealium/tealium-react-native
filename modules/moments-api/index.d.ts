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
         * Retrieves the engine response for the given engine ID. 
         * @param engineId The ID of the engine to fetch the response for.
         * @param callback The callback to recieve the engine response.
         */
        public static fetchEngineResponse(engineId: String, callback:(response: String) => void): void;
    }
}