import { NativeModules, Platform } from 'react-native';
const { TealiumReactMomentsApi } = NativeModules;

export default class TealiumMomentsApi {

    static configure(config) {
        TealiumReactMomentsApi.configure(config)
    }

    static fetchEngineResponse(engineId, callback) {
        TealiumReactMomentsApi.fetchEngineResponse(engineId, response => {
            if (Platform.OS == 'android' && typeof response === 'object') {
                if (response.dates) {
                    response.dates = Object.fromEntries(
                        Object.entries(response.dates).map(([key, value]) => [
                            key,
                            Number(value)
                        ])
                    );
                }
            } 
                
            callback(response);
        })
    }    
    
}