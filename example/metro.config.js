/**
 * Metro configuration for React Native
 * https://metrobundler.dev/docs/configuration
 *
 * @format
 */
const path = require('path');

const extraNodeModules = {
  'tealium-react-native': path.resolve(__dirname, '..', 'npm-package'),
  'tealium-react-native-adobe-visitor': path.resolve(__dirname, '..', 'modules', 'adobe-visitor'),
  'tealium-react-native-attribution': path.resolve(__dirname, '..', 'modules', 'attribution'),
  'tealium-react-native-crash-reporter': path.resolve(__dirname, '..', 'modules', 'crash-reporter'),
  'tealium-react-native-location': path.resolve(__dirname, '..', 'modules', 'location'),
  'tealium-react-native-moments-api': path.resolve(__dirname, '..', 'modules', 'moments-api'),
  'tealium-react-adjust': path.resolve(__dirname, '..', 'remotecommands', 'tealium-react-adjust'),
  'tealium-react-appsflyer': path.resolve(__dirname, '..', 'remotecommands', 'tealium-react-appsflyer'),
  'tealium-react-braze': path.resolve(__dirname, '..', 'remotecommands', 'tealium-react-braze'),
  'tealium-react-firebase': path.resolve(__dirname, '..', 'remotecommands', 'tealium-react-firebase'),
};
const watchFolders = [
  path.resolve(__dirname, '..', 'npm-package'),
  path.resolve(__dirname, '..', 'modules'),
  path.resolve(__dirname, '..', 'remotecommands'),
];

module.exports = {
  watchFolders,
  resolver: {
    extraNodeModules,
    nodeModulesPaths: [path.resolve(__dirname, 'node_modules')],
  },
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: true,
      },
    }),
  },
};
