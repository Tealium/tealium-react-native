import { AdjustConfig } from './common';
declare module 'tealium-react-adjust' {
    export default AdjustRemoteCommand;
    class AdjustRemoteCommand {
        public static name: String
        public static initialize(config?: AdjustConfig)
    }
}