export interface AttributionConfig {
    searchAdsEnabled?: boolean;
    skAdAttributionEnabled?: boolean;
    skAdConversionKeys?: { [key: string]: string };
}

export class TealiumInstallReferrerAttributionCommon{}