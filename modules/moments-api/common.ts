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

export class TealiumMomentsApiCommon {}