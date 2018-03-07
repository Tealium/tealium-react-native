# Recreate the Tealium Sample App

## Create the Project
If you don't have it already, install the create-react-native-app command line tool.
```
npm install -g create-react-native-app
```

Create a new project.
```
create-react-native-app TealiumSampleApp
cd TealiumSampleApp
```

## Eject
Because we're working with Tealium's native sdks for Android and iOS, we'll need to eject so that we can manage native dependencies. You'll be asked several questions. First you'll be asked which kind of project you want to create. Make sure to answer "React Native: I'd like a regular React Native project". Then you'll be asked how you want your app to appear on the homescreen. "Tealium Sample App" seems descriptive. Finally you'll be asked what your Android Studio and Xcode projects should be called. Again, the default "TealiumSampleApp" is a good choice.

```
npm run eject
```

## Configure the native part of things
There are two high level steps for integrating Tealium's native SDKs for Android and iOS into an existing React Native project. They are to include the native sdks, and to copy the bridging logic. Including the native sdks into a project works just like any non React Native project. There is nothing specific to React Native when adding the native sdks, and Tealium's general documentation on including the sdks into any Android or iOS project are relevant.

The bridging logic is the important glue that exposes the functionality of Tealium's native sdks as a single JavaScript api to your React Native application accross both platforms. We've provided the bridging logic in our [GitHub repo](https://github.com/Tealium/tealium-react-native) for each platform which is easily copied into a React Native project.

### Android
#### Install Tealium's Native SDK for Android
To integrate Tealium into your Android application, you'll need to add the Tealium maven repo. 

In your project root `build.gradle`, add the Tealium Maven URL:
```
allprojects {
    repositories {
        jcenter()

        maven {
            url "http://maven.tealiumiq.com/android/releases/"
        }
    }
}
```

Next, you'll need to add the Tealium core library dependency to your project module `build.gradle` file. If you'll be tracking lifecycle events, you'll also want to add the Tealium lifecycle dependency here.
```
dependencies {
    compile 'com.tealium:library:5.4.0'
    compile 'com.tealium:lifecycle:1.1' 	//<--- Add if tracking lifecycle events
}
```

#### Copy bridging logic
From the Android folder, add `TealiumModule.java` and `TealiumPackage.java` into your project files. Additionally, you'll want to include TealiumPackage into the `getPackages()` method of your `MainApplication.java` file:
```java
@Override
protected List<ReactPackage> getPackages() {
    return Arrays.<ReactPackage>asList(
            new MainReactPackage(),
            new TealiumPackage() 	//<--- Add TealiumPackage 
    );
}
```

### iOS
#### Install Tealium's Native SDK for iOS
This section gives an overview of installing the sdk to an iOS project in the context of React Native. However, the technical details are the same for any iOS project, and you should reference [Adding Tealium to Your iOS App](https://community.tealiumiq.com/t5/Tealium-for-iOS/Adding-Tealium-to-Your-iOS-App/ta-p/16327) for complete details.

Depending on if you need lifecycle tracking or not, you'll need to include either 1 or 2 binaries. They are the core sdk and the lifecycles module. The lifecycle module is only needed if you want to track lifecycles. There are two versions of each, depending on if you want the ability to run the app on a simulator, or if you want to publish the app to the App Store (Apple doesn't allow simulator logic in published apps). Because the sample app is for developer experimentation and not meant for publishing on the App Store, I picked "TealiumIOS.framework" and "TealiumIOSLifecycle.framework" (suitable for a simulator but not for publishing). When working on your own app with intent to publish to the App Store, make sure to use "TealiumIOSDevicesOnly.framework" and "TealiumIOSLifecycleDevicesOnly.framework".

Finally, you may manually install the sdks, or you may use CocoaPods. Because `create-react-native-app` doesn't create a Podfile, and because the sample app is meant to be a mvp for the sake of understanding process, I used the manual method. It's up to you to determine which strategy makes most sense for your app, however for complex production apps with many dependencies CocoaPods is probably the better choice.

#### Copy bridging logic from the tealium-react-native GitHub repo
In the [GitHub repo](https://github.com/Tealium/tealium-react-native/ios), there are two files. They are "TealiumModule.m" and "TealiumModule.h". These provide the bridging between Tealium's iOS native sdk and your React Native app written in JavaScript. Copy these two files into your app's main project. It should "just work" assuming your project has correctly included any React Native dependencies and Tealium's sdk.

## Create an App in JavaScript
In the root folder of the [GitHub repo](https://github.com/Tealium/tealium-react-native), there is a Tealium.js file. This is the file you'll ultimately import into your app for interacting with Tealium. Copy Tealium.js from the repo into your React Native JavaScript project.

Now it's time to fill in App.js with the sample app logic. The sample app is hosted [in the GitHub repo](https://github.com/Tealium/tealium-react-native/TealiumSampleApp). Copy the contents of the "App.js" file in the repository into your local "App.js".