# tealium-react-firebase

## Getting started

To install the package into your React Native app:
`$ yarn add tealium-react-firebase`

### Mostly automatic installation

For iOS only,
`$ cd ios && pod install && cd ..`

Setup the Firebase dependencies according to the standard guides for [iOS](https://firebase.google.com/docs/ios/setup) and [Android](https://firebase.google.com/docs/android/setup) to get all required config into you project.

## Usage
```javascript
import FirebaseRemoteCommand from 'tealium-react-firebase';

let config = TealiumConfig {
    // ...
    remoteCommands: [{
        id: FirebaseRemoteCommand.name,
        // Optional - path to local JSON mappings
        // path: "firebase.json"
        // Optional - path to remote JSON mappings
        // url: "https://some.domain.com/firebase.json"
    }]
}
```
