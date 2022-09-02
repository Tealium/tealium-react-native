module.exports = {
  root: true,
  extends: '@react-native-community',
  settings: {
        ...
        'import/resolver': {
            node: {
                extensions: ['.js', '.jsx', '.ts', '.tsx'],
                moduleDirectory: ['node_modules', 'src/'],
            },
        },
    }
};
