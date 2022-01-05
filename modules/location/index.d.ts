import { LocationData, TealiumLocationConfig, TealiumLocationCommon, Accuracy } from './common';

declare module 'tealium-react-native-location' {
	export default TealiumLocation;
	class TealiumLocation extends TealiumLocationCommon {
		
		/**
		 * Configures the Tealium Location module
		 * @param config configuration properties for Location module
		 */
		public static configure(config: TealiumLocationConfig): void;

		/**
		 * Sets the accuracy of the Location tracking feature
		 * @param accuracy High or Low accuracy
		 */
		public static setAccuracy(accuracy: Accuracy): void;

		/**
		 * Sets the Url to use when fetching the Gecofence configuration file
		 * @param url Url pointing to a file containing geofence configuration
		 */
		public static setGeofenceUrl(url: String): void;

		/**
		 * Sets the location path to use when fetching the Gecofence configuration file
		 * @param path local path pointing to a file containing geofence configuration
		 */
		public static setGeofenceFile(path: String): void;

		/**
		 * Fetches the last known location, if there is one.
		 * @param callback function to receive the last known location if available, else null
		 */
		public static lastLocation(callback: (response: LocationData) => void): void;
		
		/**
		 * Starts Location Tracking
		 */
		public static startLocationTracking(): void;

		/** 
		 * Stops Location Tracking
		 */
		public static stopLocationTracking(): void;
	}
}