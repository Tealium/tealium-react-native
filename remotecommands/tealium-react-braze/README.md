# tealium-react-braze

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-braze`

### Mostly automatic installation

For iOS only,
`$ cd ios && pod install && cd ..`

Setup the Braze dependencies according to the standard guides for [iOS](https://www.braze.com/docs/developer_guide/platform_integration_guides/ios/initial_sdk_setup/completing_integration/) and [Android](https://www.braze.com/docs/developer_guide/platform_integration_guides/android/initial_sdk_setup/android_sdk_integration/#basic-integration) to get all required config into you project.

## Usage
```javascript
import BrazeRemoteCommand from 'tealium-react-braze';

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: BrazeRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "braze.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/braze.json"
    }]
}
```
