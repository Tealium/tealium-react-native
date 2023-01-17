import { NativeEventEmitter, NativeModules } from 'react-native';
import { AdobeVisitorEventListenerNames } from './common';
const { TealiumReactAdobeVisitor, TealiumReactNativeAdobeVisitor } = NativeModules;

const ios = "ios"
const android = "android"

export default class TealiumAdobeVisitor {
    static emitter = new NativeEventEmitter(TealiumReactNativeAdobeVisitor);
    static emitterSubscriptions = [];

    static configure(config) {
        TealiumReactAdobeVisitor.configure(config)
    }

    static linkExistingEcidToKnownIdentifier(knownId, adobeDataProviderId, authState, callback) {
        TealiumReactAdobeVisitor.linkEcidToKnownIdentifier(knownId, adobeDataProviderId, authState)
        const response = this.emitter.addListener(AdobeVisitorEventListenerNames.responseListener, data => {
            callback(data)
        })
        this.emitterSubscriptions.push(response)
    }

    static resetVisitor() {
        TealiumReactAdobeVisitor.resetVisitor()
    }

    static decorateUrl(url, callback) {
        TealiumReactAdobeVisitor.decorateUrl(url)
        const urlDecorator = this.emitter.addListener(AdobeVisitorEventListenerNames.decoratedUrl, url => {
            callback(url)
        })
        this.emitterSubscriptions.push(urlDecorator)
    }

    static removeListeners() {
        this.emitterSubscriptions.forEach(subscription => {
            subscription.remove();
        });
    }
}