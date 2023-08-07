import { NativeModules } from 'react-native';
import { AuthState } from './common';
const { TealiumReactAdobeVisitor } = NativeModules;

export default class TealiumAdobeVisitor {

    static configure(config) {
        TealiumReactAdobeVisitor.configure(config)
    }

    static linkEcidToKnownIdentifier(knownId, adobeDataProviderId, authState, callback) {
        if(authState == undefined) {
            TealiumReactAdobeVisitor.linkEcidToKnownIdentifier(knownId, adobeDataProviderId, -1, callback)
        } else {
            TealiumReactAdobeVisitor.linkEcidToKnownIdentifier(knownId, adobeDataProviderId, authState, callback)
        }
    }

    static getAdobeVisitor(callback) {
        TealiumReactAdobeVisitor.getCurrentAdobeVisitor(callback)
    }

    static resetVisitor() {
        TealiumReactAdobeVisitor.resetVisitor()
    }

    static decorateUrl(url, callback) {
        TealiumReactAdobeVisitor.decorateUrl(url, callback)
    }

    static getUrlParameters(callback) {
        TealiumReactAdobeVisitor.getUrlParameters(callback)
    }
}