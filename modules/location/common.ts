export enum Accuracy {
    high = "high",
    low = "low"
}

export interface LocationData {
    lat: string;
    lng: string;
}

// TODO: theres a couple of more specific iOS config options
//       they could be added to this config object and ignored by the Android 
//       platform, or they could be split into a separate interface for iOS, 
//       and could accept a union type on the `configure` method.
export interface TealiumLocationConfig {
    geofenceUrl: string;
    geofenceFile: string;
    accuracy: Accuracy | Boolean;
    interval: number;
}

export class TealiumLocationCommon {}