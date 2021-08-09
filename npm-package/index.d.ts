import { ConsentCategories, ConsentStatus, Expiry, TealiumCommon, TealiumConfig, TealiumDispatch } from './common';

declare module 'tealium-react-native' {
	export default Tealium;
	class Tealium extends TealiumCommon {
		/**
		 * Retrieves the Tealium Visitor ID
		 */
		public static getVisitorId(callback: (response: string) => void): void;

		/**
		 * Retrieves the Tealium Session ID
		 */
		 public static getSessionId(callback: (response: string) => void): void;

		/**
		 * Initializes the Tealium SDK
		 * @param config Config options to change SDK behavior
		 * @param callback Optional callback executed once the underlying Tealium instance is ready.
		 */
		public static initialize(config: TealiumConfig, callback?: (response: boolean) => void): void;

		/**
		 * Tracks an event/view
		 * @param dispatch A `TealiumEvent` or `TealiumView` object
		 */
		public static track(dispatch: TealiumDispatch): void;

		/**
		 * Sets up a remote command for later execution
		 * @param id The ID used to invoke the remote command
		 * @param callback The callback to execute once the remote command returns a payload
		 */
		public static addRemoteCommand(id: string, callback: (payload: Record<string, unknown>) => void): void;

		/**
		 * Removes a remote command
		 * @param id The ID used to invoke the remote command
		 */
		public static removeRemoteCommand(id: string): void;

		/**
		 * Adds data to the data layer
		 * @param data A dictionary containing the key-value pairs to be added to the data layer
		 * @param expiry When the data should expire. Choose `Expiry.session` if unsure.
		 */
		public static addData(data: Object, expiry: Expiry): void;

		/**
		 * Retrieves the value for the specified key from the data layer
		 * @param key A string key from the data layer
		 */
		public static getData(key: string, callback: (response: any) => void): void;

		/**
		 * Removes data from the data layer
		 * @param keys The keys of the data to be removed
		 */
		public static removeData(keys: string[]): void;

		/**
		 * Retrieves the user's consent status
		 */
		public static getConsentStatus(callback: (response: ConsentStatus) => void): void;

		/**
		 * Sets the user's consent status
		 * @param consentStatus The user's consent status
		 */
		public static setConsentStatus(consentStatus: ConsentStatus): void;

		/**
		 * Retrieves the user's consented categories
		 */
		public static getConsentCategories(callback: (response: ConsentCategories[]) => void): void;

		/**
		 * Sets the user's chosen consent categories
		 * @param consentCategories Array of categories the user has opted in to
		 */
		public static setConsentCategories(consentCategories: ConsentCategories[]): void;

		/**
		 * Joins a trace session with the specified Trace ID
		 * @param id Trace ID
		 */
		public static joinTrace(id: string): void;

		/**
		 * Leaves a trace session
		 */
		public static leaveTrace(): void;

		/**
		 * Sets a listener to be called when the AudienceStream visitor profile is updated
		 * @param callback Callback function to be called when the vistior profile is updated
		 */
		public static setVisitorServiceListener(callback: (response: any) => void): void;

		/**
		 * Sets a listener to be called when the the user's saved consent status has expired
		 * @param callback Callback function to be called when the consent status has expired
		 */
		public static setConsentExpiryListener(callback: () => void): void;

		/**
		 * Terminates the current Tealium instance
		 */
		public static terminateInstance(): void;

		/**
		 * Removes all EventEmitter listeners
		 */
		public static removeListeners(): void;
	}
}