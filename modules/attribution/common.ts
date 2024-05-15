export interface AttributionConfig {
    androidInstallReferrerEnabled?: boolean;
    androidAdIdentifierEnabled?: boolean;
    iosSearchAdsEnabled?: boolean;
    iosSkAdAttributionEnabled?: boolean;
    iosSkAdConversionKeys?: { [key: string]: string };
}

export class TealiumAttributionCommon{}