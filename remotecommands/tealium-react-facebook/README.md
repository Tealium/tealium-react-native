# tealium-react-facebook 

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-facebook`

### Mostly automatic installation

For iOS only,
`$ cd ios && pod install && cd ..`

Setup the Facebook dependencies according to the standard guides for [iOS](https://www.facebook.com/docs/developer_guide/platform_integration_guides/ios/initial_sdk_setup/completing_integration/) and [Android](https://www.facebook.com/docs/developer_guide/platform_integration_guides/android/initial_sdk_setup/android_sdk_integration/#basic-integration) to get all required config into you project.

## Usage
```javascript
import FacebookRemoteCommand from 'tealium-react-facebook';

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: FacebookRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "facebook.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/facebook.json"
    }]
}
```
