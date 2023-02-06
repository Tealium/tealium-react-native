export enum AuthState {
    undefined = -1,
    unknown = 0,
    authenticated = 1,
    loggedOut = 2
}

export interface TealiumAdobeVisitorConfig {
    adobeVisitorOrgId : string;
    adobeVisitorExistingEcid ?: string;
    adobeVisitorRetries ?: number;
    adobeVisitorAuthState ?: AuthState;
    adobeVisitorDataProviderId ?: string;
    adobeVisitorCustomVisitorId ?: string;
}

export class TealiumAdobeVisitorCommon {}