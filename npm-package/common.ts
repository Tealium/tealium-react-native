export enum EventListenerNames {
    visitor = 'TealiumReactNative.VisitorServiceEvent',
    remoteCommand = 'TealiumReactNative.RemoteCommandEvent',
    consentExpired = 'TealiumReactNative.ConsentExpiredEvent',
    visitorId = 'TealiumReactNative.VisitorIdUpdatedEvent'
}

export enum Collectors {
    AppData = 'AppData',
    Connectivity = 'Connectivity',
    DeviceData = 'DeviceData',
    Lifecycle = 'Lifecycle',
}

export enum Dispatchers {
    Collect = 'Collect',
    TagManagement = 'TagManagement',
    RemoteCommands = 'RemoteCommands',
}

export enum Expiry {
    forever = 'forever',
    untilRestart = 'untilRestart',
    session = 'session',
}

export enum ConsentPolicy {
    ccpa = 'ccpa',
    gdpr = 'gdpr',
}

export interface TealiumDispatch {
    dataLayer: Object;
    type: string;
}

export class TealiumView implements TealiumDispatch {
    public type: string = 'view';
    constructor(public viewName: string, public dataLayer: Object) {}
}

export class TealiumEvent implements TealiumDispatch {
    public type: string = 'event';
    constructor(public eventName: string, public dataLayer: Object) {}
}

export enum ConsentStatus {
    consented = 'consented',
    notConsented = 'notConsented',
    unknown = 'unknown',
}

export enum LogLevel {
    dev = 'dev',
    qa = 'qa',
    prod = 'prod',
    silent = 'silent',
}

export enum TealiumEnvironment {
    dev = 'dev',
    qa = 'qa',
    prod = 'prod',
}

export enum ConsentCategories {
    analytics = 'analytics',
    affiliates = 'affiliates',
    displayAds = 'display_ads',
    email = 'email',
    personalization = 'personalization',
    search = 'search',
    social = 'social',
    bigData = 'big_data',
    mobile = 'mobile',
    engagement = 'engagement',
    monitoring = 'monitoring',
    crm = 'crm',
    cdp = 'cdp',
    cookieMatch = 'cookiematch',
    misc = 'misc',
}

export class ConsentExpiry {
    constructor(public time: number, public unit: TimeUnit) {}
}

export enum TimeUnit {
    minutes = 'minutes',
    hours = 'hours',
    months = 'months',
    days = 'days'
}

export interface TealiumConfig {
    account: string;
    profile: string;
    environment: TealiumEnvironment;
    dataSource ? : string;
    collectors: Collectors[];
    dispatchers: Dispatchers[];
    customVisitorId ? : string;
    memoryReportingEnabled ? : boolean;
    overrideCollectURL ? : string;
    overrideCollectBatchURL ? : string;
    overrideCollectDomain ? : string;
    overrideCollectProfile ? : string;
    overrideLibrarySettingsURL ? : string;
    overrideTagManagementURL ? : string;
    deepLinkTrackingEnabled ? : boolean;
    qrTraceEnabled ? : boolean;
    loglevel ? : LogLevel;
    consentLoggingEnabled ? : boolean;
    consentPolicy ? : ConsentPolicy;
    consentExpiry ? : ConsentExpiry;
    batchingEnabled ? : boolean;
    lifecycleAutotrackingEnabled ? : boolean;
    useRemoteLibrarySettings ? : boolean;
    visitorServiceEnabled ? : boolean;
    sessionCountingEnabled ? : boolean;
    remoteCommands ? : RemoteCommand[];
    visitorIdentityKey ? : string;
}

export interface RemoteCommand {
    id: string;
    path?: string;
    url?: string;
    callback?(payload: Object) : void;
}

export interface VisitorProfile {
    audiences?: { [key: string]: string };
    badges?: { [key: string]: boolean };
    dates?: { [key: string]: number };
    booleans?: { [key: string]: boolean };
    arrayOfBooleans?: { [key: string]: boolean[] };
    numbers?: { [key: string]: number};
    arrayOfNumbers?: { [key: string]: number[] };
    tallies?: { [key: string]: { [key: string]: number} };
    strings?: { [key: string]: string};
    arrayOfStrings?: { [key: string]: string[]};
    setsOfStrings?: { [key: string]: string[]};
    currentVisit?: CurrentVisit;
}

export interface CurrentVisit {
    dates?: { [key: string]: number};
    booleans?: { [key: string]: boolean};
    arrayOfBooleans?: { [key: string]: boolean[] };
    numbers?: { [key: string]: number};
    arrayOfNumbers?: { [key: string]: number[] };
    tallies?: { [key: string]: { [key: string]: number} };
    strings?: { [key: string]: string};
    arrayOfStrings?: { [key: string]: string[]};
    setsOfStrings?: { [key: string]: string[]};
}

export class TealiumCommon {}
