import { NativeEventEmitter, NativeModules } from 'react-native';
import { Expiry, Dispatchers, EventListenerNames, RemoteCommand } from './common';
const { TealiumWrapper, TealiumReactNative } = NativeModules;

export default class Tealium {

    static emitter = new NativeEventEmitter(TealiumReactNative);
    static emitterCallbacks = {};
    static emitterSubscriptions = [];

    static initialize(config, callback) {
        if (config.remoteCommands) {
            config.remoteCommands.forEach((remoteCommand) => {
                if (remoteCommand.callback) {
                    this.emitterCallbacks[remoteCommand.id] = remoteCommand.callback
                }
            });
        }
        TealiumWrapper.initialize(config, callback || (response => {}));
        TealiumWrapper.addToDataLayer({'plugin_name': 'Tealium-ReactNative', 'plugin_version': '2.2.0'}, Expiry.forever);
        if (config["dispatchers"].includes(Dispatchers.RemoteCommands)) {
            this.setRemoteCommandListener();
        }
    }

    static track(dispatch) {
        TealiumWrapper.track(dispatch);
    }

    static terminateInstance() {
        TealiumWrapper.terminateInstance();
    }

    static addData(data, expiry) {
        TealiumWrapper.addToDataLayer(data, expiry);
    }

    static getData(key, callback) {
        TealiumWrapper.getFromDataLayer(key, callback);
    }

    static removeData(keys) {
        TealiumWrapper.removeFromDataLayer(keys);
    }

    static getConsentStatus(callback) {
        TealiumWrapper.getConsentStatus(callback);
    }

    static setConsentStatus(consentStatus) {
        TealiumWrapper.setConsentStatus(consentStatus);
    }

    static getConsentCategories(callback) {
        TealiumWrapper.getConsentCategories(callback);
    }

    static setConsentCategories(consentCategories) {
        TealiumWrapper.setConsentCategories(consentCategories);
    }

    static joinTrace(id) {
        TealiumWrapper.joinTrace(id);
    }

    static leaveTrace() {
        TealiumWrapper.leaveTrace();
    }

    static getVisitorId(callback) {
        TealiumWrapper.getVisitorId(callback);
    }

    static getSessionId(callback) {
        TealiumWrapper.getSessionId(callback);
    }

    static setVisitorServiceListener(callback) {
        const visitor = this.emitter.addListener(EventListenerNames.visitor, profile => {
            callback(profile);
        });
        this.emitterSubscriptions.push(visitor);
    }

    static setConsentExpiryListener(callback) {
        const consent = this.emitter.addListener(EventListenerNames.consentExpired, () => {
            callback();
        });
        this.emitterSubscriptions.push(consent);
    }

    static addRemoteCommand(id, callback) {
        TealiumWrapper.addRemoteCommand(id);
        this.emitterCallbacks[id] = callback;
    }

    static removeRemoteCommand(id) {
        TealiumWrapper.removeRemoteCommand(id);
        delete this.emitterCallbacks[id];
    }

    static setRemoteCommandListener() {
        var remoteCommand = this.emitter.addListener(EventListenerNames.remoteCommand,
            payload => {
            	let callback = this.emitterCallbacks[payload["command_id"]]
                if (payload["command_id"] && callback) {
                    callback(payload);
                }
            }
        );
        this.emitterSubscriptions.push(remoteCommand);
    }

    static removeListeners() {
        this.emitterSubscriptions.forEach(subscription => {
            subscription.remove();
        });
    }
}