import { NativeModules } from 'react-native';

const { TealiumReactLocation } = NativeModules;

export default class TealiumLocation {

    /**
     * Configures the Tealium Location module
     * @param config configuration properties for Location module
     */
    static configure(config) {
        TealiumReactLocation.configure(config)
    }

    /**
     * Sets the accuracy of the Location tracking feature
     * @param accuracy High or Low accuracy
     */
    static setAccuracy(accuracy) {
        if (typeof accuracy === "boolean") {
            TealiumReactLocation.setAccuracyBoolean(accuracy)
        } else if (typeof accuracy === "string") {
            TealiumReactLocation.setAccuracyString(accuracy)
        }
    }

    /**
     * Sets the Url to use when fetching the Gecofence configuration file
     * @param url Url pointing to a file containing geofence configuration
     */
    static setGeofenceUrl(url) {
        TealiumReactLocation.setGeofenceUrl(path)
    }

    /**
     * Sets the location path to use when fetching the Gecofence configuration file
     * @param path local path pointing to a file containing geofence configuration
     */
    static setGeofenceFile(path) {
        TealiumReactLocation.setGeofenceFile(path)
    }

    /**
     * Fetches the last known location, if there is one.
     * @param callback function to receive the last known location if available, else null
     */
    static lastLocation(callback) {
        TealiumReactLocation.lastLocation(callback)
    }

    /**
     * Starts Location Tracking
     */
    static startLocationTracking() {
        TealiumReactLocation.startLocationTracking()
    }

    /** 
     * Stops Location Tracking
     */
    static stopLocationTracking() {
        TealiumReactLocation.stopLocationTracking()
    }
}