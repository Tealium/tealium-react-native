import test from 'ava';
import { lex, parse, toLines, addTealiumRepo } from '../scripts/gradle-build-file-parser';

test('add Tealium repo to default build.gradle', t => {
    const buildFile =
`
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
    }
}
`;

    const expectedResult =
`
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven {
            // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
            url "$rootDir/../node_modules/react-native/android"
        }
        maven {
            url "http://maven.tealiumiq.com/android/releases/"
        }
    }
}
`;

    const lines = buildFile.split('\n');
    const tokens = lines.map(lex);
    const ast = parse(tokens);
    addTealiumRepo(ast);
    const actualResult = toLines(ast).join('\n');
    t.is(actualResult, expectedResult);
});

test('adding the repo yeilds null when repositories is not present', t => {
    const buildFile =
`
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
}
`;

    const lines = buildFile.split('\n');
    const tokens = lines.map(lex);
    const ast = parse(tokens);
    const expectedResult = null;
    const actualResult = addTealiumRepo(ast);
    t.is(actualResult, expectedResult);
});

test('adding the repo yeilds null when allprojects is not present', t => {
    const buildFile =
`
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

repositories {
    mavenLocal()
    jcenter()
    maven {
        // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
        url "$rootDir/../node_modules/react-native/android"
    }
}
`;

    const lines = buildFile.split('\n');
    const tokens = lines.map(lex);
    const ast = parse(tokens);
    const expectedResult = null;
    const actualResult = addTealiumRepo(ast);
    t.is(actualResult, expectedResult);
});

test('adding the repo yeilds null when repositories is not nested in allprojects', t => {
    const buildFile =
`
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
}

repositories {
    mavenLocal()
    jcenter()
    maven {
        // All of React Native (JS, Obj-C sources, Android binaries) is installed from npm
        url "$rootDir/../node_modules/react-native/android"
    }
}
`;

    const lines = buildFile.split('\n');
    const tokens = lines.map(lex);
    const ast = parse(tokens);
    const expectedResult = null;
    const actualResult = addTealiumRepo(ast);
    t.is(actualResult, expectedResult);
});