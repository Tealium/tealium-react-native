declare module 'tealium-react-braze' {
	export default class BrazeRemoteCommand {
        static name: string

        /**
         * Android Only - enables or disables session handling
         * @param enabled 
         */
        static setSessionHandlingEnabled(enabled: boolean): void
    
        /**
         * Android Only - a list of fully qualified classes to be ignored by session handling
         * @param classList 
         */
        static setSessionHandlingBlacklist(classList: string[]): void
    
        /**
         * Android Only - sets whether or not to register the in-app messenger
         * @param enabled 
         */
        static setRegisterInAppMessenger(enabled: boolean): void
    
        /**
         * * Android Only - a list of fully qualified classes to be ignored by the in-app messenger
         * @param classList 
         */
        static setInAppMessengerBlacklist(classList: string[]): void
    }
}