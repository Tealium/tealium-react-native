export interface AdjustConfig {
    appToken: string;
    environment: AdjustEnvironemnt;
    allowSuppressLogLevel: boolean;
}

export enum AdjustEnvironemnt {
    sandbox = 'sandbox',
    production = 'production'
}