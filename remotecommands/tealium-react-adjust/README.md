# tealium-react-adjust

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-adjust`

### Mostly automatic installation

For iOS only,
`$ cd ios && pod install && cd ..`

Setup the Adjust dependencies according to the standard guides for [iOS](https://help.adjust.com/en/article/get-started-ios-sdk) and [Android](https://help.adjust.com/en/article/get-started-android-sdk) to get all required config into you project.

## Usage
```javascript
import AdjustRemoteCommand from 'tealium-react-adjust';

AdjustRemoteCommand.initialize();

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: AdjustRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "adjust.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/adjust.json"
    }]
}
```
