// Based off of https://docs.tealium.com/platforms/react-native/functions/

declare module 'tealium-react-native' {
  export type TealiumData = { [key: string]: string | string[] };

  /** 0 - Unknown, 1 - Consented, 2 - Not Consented, 3 - Disabled (Objective-C Only) */
  export type TealiumUserConsentStatus = 0 | 1 | 2 | 3;

  export type remoteCommandEmitter = { NativeEventEmitter };

  export default Tealium;
  class Tealium {
    /**
     * Initialize Tealium before calling any other method.
     * @param account Tealium account name
     * @param profile Tealium profile name (default: “main”)
     * @param environment Tealium environment
     * @param iosDataSource A data source key from UDH eg. “abc123”
     * @param androidDataSource A data source key from UDH eg. “def456”
     */
    static initialize(
      account: string,
      profile = string,
      environment: string,
      iosDataSource?: string | null,
      androidDataSource?: string | null,
    ): void;

    /**
     * Initialize Tealium with Consent Management before calling any other method.
     * @param account Tealium account name
     * @param profile Tealium profile name (default: “main”)
     * @param environment Tealium environment
     * @param iosDataSource A data source key from UDH eg. “abc123”
     * @param androidDataSource A data source key from UDH eg. “def456”
     */
    static initializeWithConsentManager(
      account: string,
      profile = string,
      environment: string,
      iosDataSource?: string | null,
      androidDataSource?: string | null,
    ): void;

    /**
     * Initialize Tealium with Consent Management before calling any other method.
     * @param account Tealium account name
     * @param profile Tealium profile name (default: “main”)
     * @param environment Tealium environment
     * @param iosDataSource A data source key from UDH eg. “abc123”
     * @param androidDataSource A data source key from UDH eg. “def456”
     * @param instance Name of Tealium instance (e.g. can be any String)
     * @param enableLifeCycle To enable life cycle tracking (default: true)
     * @param overridePublishSettingsUrl String representing the publish settings URL if overriding, otherwise null (default: null)
     * @param overrideTagManagementUrl String representing the tag management URL if overriding, otherwise null (default: null)
     * @param enableCollectEndpoint True sends data to the Collect endpoint (default: true)
     * @param enableConsentManager True enables Consent Management
     * @param overrideCollectDispatchURL String representing the HTTP endpoint to send all event data (default: null)
     */
    static initializeCustom(
      account: string,
      profile = string,
      environment: string,
      iosDataSource?: string | null,
      androidDataSource?: string | null,
      instance: string,
      enableLifeCycle = boolean,
      overridePublishSettingsUrl: string | null,
      overrideTagManagementUrl: string | null,
      enableCollectEndpoint = boolean,
      enableConsentManager = boolean,
      overrideCollectDispatchURL: string | null
    ): void;

    /**
     * Track an event, where the title is a string and the data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * @param eventName Name of event (becomes the event_name attribute in UDH)
     * @param data JSON object of key value pairs
     */
    static trackEvent(eventName: string, data: TealiumData): void;

    /**
     * Track an event, where the title is a string and the data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param eventName Name of event (becomes the event_name attribute in UDH)
     * @param data JSON object of key value pairs
     */
    static trackEventForInstanceName(
      instanceName: string,
      eventName: string,
      data: TealiumData,
    ): void;

    /**
     * Track a view, where the title is a string and the data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * @param screenName Name of event (becomes the screen_title attribute in UDH)
     * @param data JSON object of key value pairs
     */
    static trackView(screenName: string, data: TealiumData): void;

    /**
     * Track a view, where the title is a string and the data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param screenName Name of event (becomes the screen_title attribute in UDH)
     * @param data JSON object of key value pairs
     */
    static trackViewForInstanceName(
      instanceName: string,
      screenName: string,
      data: TealiumData,
    ): void;

    /**
     * Set volatile data to be sent with each subsequent event or view until the app is terminated,
     * where data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * @param data JSON object of key value pairs
     */
    static setVolatileData(data: TealiumData): void;

    /**
     * Set volatile data to be sent with each subsequent event or view until the app is terminated,
     * where data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param data JSON object of key value pairs
     */
    static setVolatileDataForInstanceName(
      instanceName: string,
      data: TealiumData,
    ): void;

    /**
     * Gets the value for the key passed in.
     * @param key Key name
     * @param callback A callback with a single parameter representing the value
     */
    static getVolatileData(
      key: string,
      callback: (value: string | string[]) => {},
    ): void;

    /**
     * Gets the value for the key passed in for the instance name. This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param key Key name
     * @param callback A callback with a single parameter representing the value
     */
    static getVolatileDataForInstanceName(
      instanceName: string,
      key: string,
      callback: (value: string | string[]) => {},
    ): void;

    /**
     * Set persistent data to be sent with each subsequent event or view, even between app restarts,
     * where data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * @param data JSON object of key value pairs
     */
    static setPersistentData(data: TealiumData): void;

    /**
     * Set persistent data to be sent with each subsequent event or view, even between app restarts,
     * where data is a JSON object of key-value pairs where keys are strings and the values are either a string or array of strings.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param data JSON object of key value pairs
     */
    static setPersistentDataForInstanceName(
      instanceName: string,
      data: TealiumData,
    ): void;

    /**
     * Remove volatile data that has been previously set using Tealium.setVolatileData() by taking an array of key names.
     * @param keys Array of key names
     */
    static removeVolatileData(keys: string[]): void;

    /**
     * setVolatileData() by taking an array of key names. This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param keys Array of key names
     */
    static removeVolatileDataForInstanceName(
      instanceName: string,
      keys: string[],
    ): void;

    /**
     * Remove persistent data that has been previously set using Tealium.setPersistentData() by taking an array of key names.
     * @param keys Array of key names
     */
    static removePersistentData(keys: string[]): void;

    /**
     * Remove persistent data that has been previously set using Tealium.setPersistentData() by taking an array of key names.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param keys Array of key names
     */
    static removePersistentDataForInstanceName(
      instanceName: string,
      keys: string[],
    ): void;

    /**
     * Gets the visitorID of a user. Pass in a callback to use the visitorID.
     * @param callback A callback with a single parameter representing the visitor id
     */
    static getVisitorID(callback: (visitorId: string) => {}): void;

    /**
     * Gets the visitorID of a user. Pass in a callback to use the visitorID.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param callback A callback with a single parameter representing the visitor id
     */
    static getVisitorIDForInstanceName(
      instanceName: string,
      callback: (visitorId: string) => {},
    ): void;

    /**
     * Gets the consent status of a user. Pass in a callback to use the userConsentStatus.
     * @param callback A callback with a single parameter representing the user consent status
     */
    static getUserConsentStatus(
      callback: (userConsentStatus: TealiumUserConsentStatus) => {},
    ): void;

    /**
     * Gets the consent status of a user. Pass in a callback to use the userConsentStatus.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param callback A callback with a single parameter representing the user consent status
     */
    static getUserConsentStatusForInstanceName(
      instanceName: string,
      callback: (userConsentStatus: TealiumUserConsentStatus) => {},
    ): void;

    /**
     * Sets the consent status of a user.
     * @param userConsentStatus The consent status of the user
     */
    static setUserConsentStatus(
      userConsentStatus: TealiumUserConsentStatus,
    ): void;

    /**
     * Sets the consent status of a user.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param userConsentStatus The consent status of the user
     */
    static setUserConsentStatusForInstanceName(
      instanceName: string,
      userConsentStatus: TealiumUserConsentStatus,
    ): void;

    /**
     * Gets the consent categories of a user. Pass in a callback to use the userConsentCategories.
     * @param callback A callback with a single parameter that represents the user consent categories
     */
    static getUserConsentCategories(
      callback: (userConsentCategories: string[]) => {},
    ): void;

    /**
     * Gets the consent categories of a user. Pass in a callback to use the userConsentCategories.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param callback A callback with a single parameter that represents the user consent categories
     */
    static getUserConsentCatgoriesForInstanceName(
      instanceName: string,
      callback: (userConsentCategories: string[]) => {},
    ): void;

    /**
     * Sets the consent categories of a user. Pass in an array of Strings to set the categories.
     * @param userConsentCategories Array of consent categories names
     */
    static setUserConsentCategories(userConsentCategories: string[]): void;

    /**
     * Sets the consent ;categories of a user. Pass in an array of Strings to set the categories.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param userConsentCategories Array of consent categories names
     */
    static setUserConsentCatgoriesForInstanceName(
      instanceName: string,
      userConsentCategories: string[],
    ): void;

    /**
     * Resets the user consent status and categories of a user.
     */
    static resetUserConsentPreferences(): void;

    /**
     * Resets the user consent status and categories of a user.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     */
    static resetUserConsentPreferencesForInstanceName(
      instanceName: string,
    ): void;

    /**
     * Sets consent logging for a user.
     * @param enabled true to enable consent logging, false to disable it
     */
    static setConsentLoggingEnabled(enabled: boolean): void;

    /**
     * Sets consent logging for a user.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param enabled true to enable consent logging, false to disable it
     */
    static setConsentLoggingEnabledForInstanceName(
      instanceName: string,
      enabled: boolean,
    ): void;

    /**
     * Checks if consent logging is enabled for a user. Pass in a callback to use the value of consent logging.
     * @param callback A callback with a single parameter representing enabled status
     */
    static isConsentLoggingEnabled(callback: (enabled: boolean) => {}): void;

    /**
     * Checks if consent logging is enabled for a user. Pass in a callback to use the value of consent logging.
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param callback A callback with a single parameter representing enabled status
     */
    static isConsentLoggingEnabledForInstanceName(
      instanceName: string,
      callback: (enabled: boolean) => {},
    ): void;

    /**
     * Adds a remote command to the remote command manager. 
     * 
     * @param commandID Name of the Remote Command (if using TiQ, this is whatever is in the tag config)
     * @param description A description of the remote command
     * @param callback Callback to execute with the Remote Command payload
     */
    static addRemoteCommand(
      commandID: string,
      description: string,
      callback: (payload: object) => void
    ): void;

    /**
     * Adds a remote command to the remote command manager. 
     * 
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param commandID Name of the Remote Command (if using TiQ, this is whatever is in the tag config)
     * @param description A description of the remote command
     * @param callback Callback to execute with the Remote Command payload
     */
    static addRemoteCommandForInstanceName(
      instanceName: string,
      commandID: string,
      description: string,
      callback: (payload: object) => void
    ): void;


    /**
     * Removes a remote command to the remote command manager. 
     * @param commandID Name of the Remote Command (if using TiQ, this is whatever is in the tag config)
     */
    static removeRemoteCommand(
      commandID: string,
    ): void;

    /**
     * Removes a remote command to the remote command manager. 
     * This method should be used if you have multiple instances of Tealium in your app.
     * @param instanceName Name of the Tealium instance
     * @param commandID Name of the Remote Command (if using TiQ, this is whatever is in the tag config)
     */
    static removeRemoteCommandForInstanceName(
      instanceName: string,
      commandID: string,
    ): void;
  }
}