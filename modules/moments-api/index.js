import { NativeModules } from 'react-native';
const { TealiumReactMomentsApi } = NativeModules;

export default class TealiumMomentsApi {

    static configure(config) {
        TealiumReactMomentsApi.configure(config)
    }

    static fetchEngineResponse(engineId, callback) {
        TealiumReactMomentsApi.fetchEngineResponse(engineId, callback)
    }    
    
}