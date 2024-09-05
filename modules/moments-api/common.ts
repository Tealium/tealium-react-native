import { Double } from "react-native/Libraries/Types/CodegenTypes";

export enum MomentsApiRegion {
    Germany = 'germany',
    UsEast = 'us_east',
    Sydney = 'sydney',
    Oregon = 'oregon',
    Tokyo = 'tokyo',
    HongKong = 'hong_kong',
}

export interface MomentsApiConfig {
    momentsApiRegion : string;
    momentsApiReferrer ?: string;
}

export interface EngineResponse {
    audiences ?: string[];
    badges ?: string[];
    strings ?: { [key: string]: string};
    booleans ?: { [key: string]: boolean};
    dates ?: { [key: string]: number};
    numbers ?: { [key: string]: number};
}

export class TealiumMomentsApiCommon {}