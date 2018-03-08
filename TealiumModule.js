import { NativeModules } from 'react-native';

const { TealiumModule } = NativeModules;

export default class Tealium {
  static initialize(
    account, profile, environment, iosDatasource,
    androidDatasource, instanceName = 'MAIN', isLifecycleEnabled = true,
  ) {
    TealiumModule.initialize(
      account, profile, environment, iosDatasource, androidDatasource,
      instanceName, isLifecycleEnabled,
    );
  }

  static trackEvent(stringTitle, data) {
    TealiumModule.trackEvent(stringTitle, data);
  }

  static trackView(stringTitle, data) {
    TealiumModule.trackView(stringTitle, data);
  }

  static setVolatileData(data) {
    TealiumModule.setVolatileData(data);
  }

  static setPersistentData(data) {
    TealiumModule.setPersistentData(data);
  }

  static removeVolatileData(keys) {
    TealiumModule.removeVolatileData(keys);
  }

  static removePersistentData(keys) {
    TealiumModule.removePersistentData(keys);
  }
}
