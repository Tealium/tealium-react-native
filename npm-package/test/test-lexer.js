import test from 'ava';
import { lex } from '../scripts/gradle-build-file-parser';

test('A single "{" lexes as a start block', t => {
    const line = '{';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('A single "}" lexes as an end block', t => {
    const line = '}';
    const expectedResult = {
        token: 'end block',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('start block can be indented', t => {
    const line = '    {';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('end block can be indented', t => {
    const line = '    }';
    const expectedResult = {
        token: 'end block',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('A start block can have a label', t => {
    const line = 'allprojects {';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: 'allprojects',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('There may be no space between a label and the "{" of a start block', t => {
    const line = 'allprojects{';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: 'allprojects',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('A start block with a label can be indented', t => {
    const line = '    allprojects {';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 4,
        label: 'allprojects',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"/*" lexes as start comment', t => {
    const line = '/*';
    const expectedResult = {
        token: 'start comment',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('lines with start of a block comment can be indented', t => {
    const line = '    /*';
    const expectedResult = {
        token: 'start comment',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"*/" lexes as end comment', t => {
    const line = '*/';
    const expectedResult = {
        token: 'end comment',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('lines with end of a block comment can be indented', t => {
    const line = '    */';
    const expectedResult = {
        token: 'end comment',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('otherwise legitimate lines can be commented out in start of block comment', t => {
    const line = '/* allprojects {';
    const expectedResult = {
        token: 'start comment',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('labeled start block followed by line comment should still lex', t => {
    const line = '    repositories { // comment';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 4,
        label: 'repositories',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('unlabeled start block followed by line comment should still lex', t => {
    const line = '    { // comment';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('end block followed by line comment should still lex', t => {
    const line = '    } // comment';
    const expectedResult = {
        token: 'end block',
        text: line,
        indentation: 4,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('line comments are just content (would be start block)', t => {
    const line = '//allprojects {';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('line comments are just content (would be end block)', t => {
    const line = '//    }';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('start blocks can contain extra junk after "{"', t => {
    const line = 'applicationVariants.all { variant ->';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: 'applicationVariants.all',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('labels are not always single word (conditional looking syntax)', t => {
    const line = 'if (a != b) {';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: 'if (a != b)',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('labels are not always single word (constructor looking syntax)', t => {
    const line = 'task copyDownloadableDepsToLibs(type: Copy) {';
    const expectedResult = {
        token: 'start block',
        text: line,
        indentation: 0,
        label: 'task copyDownloadableDepsToLibs(type: Copy)',
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('empty lines should lex as "other" with no indentation', t => {
    const line = '';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: null,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('lines with only whitespace should lex as "other" with no indentation', t => {
    const line = '    ';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: null,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('applying the android plugin is ubiquitous', t => {
    const line = 'apply plugin: "com.android.application"';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"mavenLocal()" is a common line; should lex as "other"', t => {
    const line = 'mavenLocal()';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"mavenLocal()" is a common line; should lex as "other" (indented)', t => {
    const line = '        mavenLocal()';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 8,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"jcenter()" is a common line; should lex as "other"', t => {
    const line = 'jcenter()';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 0,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});

test('"jcenter()" is a common line; should lex as "other" (indented)', t => {
    const line = '        jcenter()';
    const expectedResult = {
        token: 'other',
        text: line,
        indentation: 8,
        label: null,
    };
    const actualResult = lex(line);

    t.deepEqual(actualResult, expectedResult);
});
