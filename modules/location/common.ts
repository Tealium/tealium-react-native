/**
 * Specifies accuracy level 
 */
 export enum Accuracy {
    high = "high",
    low = "low"
}

/**
 * Holder for Latitude and Longitude coordinates
 */
export interface LocationData {
    lat: string;
    lng: string;
}

/**
 * Applicable to iOS Only
 */
export enum DesiredAccuracy {
    bestForNavigation = "bestForNavigation",
    best = "best",
    nearestTenMeters = "nearestTenMeters",
    nearestHundredMeters = "nearestHundredMeters",
    reduced = "reduced",
    withinOneKilometer = "withinOneKilometer",
    withinThreeKilometers = "withinThreeKilometers"
}

export interface TealiumLocationConfig {
    /**
     * Specifies the URL of the file containing
     * the geofence specifications
     */
    geofenceUrl ?: string;
    
    /**
     * Specifies the local path to the file containing
     * the geofence specifications
     */
    geofenceFile ?: string;
    
    /**
     * Specifies the accuracy to use when tracking location
     */
    accuracy ?: Accuracy | Boolean;

    /**
     * Android only: Specifies the time in ms used to request 
     * location updates.
     */
    interval ?: number;

    /**
     * iOS only: Enables or disables tracking geofence events
     */
    geofenceEnabled ?: string

    /**
     * iOS only: Specifies the distance interval in meters 
     * to use for location updates
     * Should only be used when combined with high accuracy
     */
    updateDistance ?: number

    /**
     * iOS Only: Specifies the extended desired accuracy
     */
    desiredAccuracy ?: DesiredAccuracy

}

export class TealiumLocationCommon {}