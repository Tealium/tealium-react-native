module.exports = {
    project: {
      ios: {},
      android: {}, 
    },
    commands: [require('node ./node_modules/tealium-react-native/scripts/postlink.js'),
               require('node ./node_modules/tealium-react-native/scripts/preunlink.js')]
};