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
        TealiumReactLocation.setGeofenceUrl(url)
    }

    /**
     * Sets the location path to use when fetching the Gecofence configuration file
     * @param path local path pointing to a file containing geofence configuration
     */
    static setGeofenceFile(path) {
        TealiumReactLocation.setGeofenceFile(path)
    }

    /**
     * Android Only: Specifies the time in ms used to request 
     * location updates.
     * @param interval time in ms to request location updates
     */
    static setInterval(interval) {
        if (Platform.OS === 'android') {
            TealiumReactLocation.setInterval(interval)
        }
    }

    /**
     * iOS only: Enables or disables tracking geofence events
     * @param enabled true for enabled, else false
     */
    static setGeofenceTrackingEnabled(enabled) {
        if (Platform.OS === 'ios') {
            TealiumReactLocation.setGeofenceTrackingEnabled(enabled)
        }
    }

    /**
     * iOS only: Specifies the distance interval in meters 
     * to use for location updates
     * Should only be used when combined with high accuracy
     * @param distance distance in meters to receive location updates
     */
    static setUpdateDistance(distance) {
        if (Platform.OS === 'ios') {
            TealiumReactLocation.setUpdateDistance(distance)
        }
    }

    /**
     * iOS Only: Specifies the extended desired accuracy
     * @param accuracy extended accuracy value 
    */ 
    static setDesiredAccuracy(accuracy) {
        if (Platform.OS === 'ios') {
            TealiumReactLocation.setDesiredAccuracy(accuracy)
        }
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