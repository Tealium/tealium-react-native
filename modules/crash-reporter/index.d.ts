declare module 'tealium-react-native-crash-reporter' {
    export default TealiumCrashReporter;
    class TealiumCrashReporter {
        public static initialize(truncateStackTrack?: boolean): void;
    }
}