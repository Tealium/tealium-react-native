module.exports = {
  project: {
    ios: {},
    android: {},
  },
  commands: [
    { name: 'postlink', func: () => require('./scripts/postlink.js') },
    { name: 'preunlink', func: () => require('./scripts/preunlink.js') },
  ],
};
