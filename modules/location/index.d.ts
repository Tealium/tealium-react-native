import { LocationData, TealiumLocationConfig, TealiumLocationCommon, Accuracy, DesiredAccuracy } from './common';

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
		 * Android Only: Specifies the time in ms used to request 
     	 * location updates.
		 * @param interval time in ms to request location updates
		 */
		public static setInterval(interval: Number): void;

		/**
		 * iOS only: Enables or disables tracking geofence events
		 * @param enabled true for enabled, else false
		 */
		public static setGeofenceTrackingEnabled(enabled: Boolean): void;

		/**
		 * iOS only: Specifies the distance interval in meters 
		 * to use for location updates
		 * Should only be used when combined with high accuracy
		 * @param distance distance in meters to receive location updates
		 */
		public static setUpdateDistance(distance: Number): void;

		/**
     	 * iOS Only: Specifies the extended desired accuracy
		 * @param accuracy extended accuracy value 
     	*/ 
		public static setDesiredAccuracy(accuracy: DesiredAccuracy): void;

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