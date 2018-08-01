import test from 'ava';
import { parse } from '../scripts/gradle-build-file-parser';

test('parsing empty list of tokens yields empty AST', t => {
    const tokens = [];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parsing list of single token yields AST with single content', t => {
    const tokens = [
        {
            token: 'other',
            label: null,
            indentation: 0,
            text: 'content',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'other',
                    label: null,
                    indentation: 0,
                    text: 'content',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parsing list of a start block and end block tokens yields AST with single empty block', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'block',
                label: 'allprojects',
                contents: [],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse content before content', t => {
    const tokens = [
        {
            token: 'other',
            label: null,
            indentation: 0,
            text: 'content a',
        },
        {
            token: 'other',
            label: null,
            indentation: 0,
            text: 'content b',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'other',
                    label: null,
                    indentation: 0,
                    text: 'content a',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'other',
                    label: null,
                    indentation: 0,
                    text: 'content b',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse content before empty block', t => {
    const tokens = [
        {
            token: 'other',
            label: null,
            indentation: 0,
            text: 'content',
        },
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'other',
                    label: null,
                    indentation: 0,
                    text: 'content',
                },
            },
            {
                type: 'block',
                label: 'allprojects',
                contents: [],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse empty block before content', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
        {
            token: 'other',
            label: null,
            indentation: 0,
            text: 'content',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'block',
                label: 'allprojects',
                contents: [],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'other',
                    label: null,
                    indentation: 0,
                    text: 'content',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse empty block before empty block', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
        {
            token: 'start block',
            label: 'repositories',
            indentation: 0,
            text: 'repositories {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'block',
                label: 'allprojects',
                contents: [],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
            {
                type: 'block',
                label: 'repositories',
                contents: [],
                startToken: {
                    token: 'start block',
                    label: 'repositories',
                    indentation: 0,
                    text: 'repositories {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse nested content inside block', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'other',
            label: null,
            indentation: 4,
            text: '    content',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'block',
                label: 'allprojects',
                contents: [
                    {
                        type: 'content',
                        token: {
                            token: 'other',
                            label: null,
                            indentation: 4,
                            text: '    content',
                        },
                    },
                ],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('parse nested block inside block', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'start block',
            label: 'repositories',
            indentation: 4,
            text: '    repositories {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 4,
            text: '    }',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'block',
                label: 'allprojects',
                contents: [
                    {
                        type: 'block',
                        label: 'repositories',
                        contents: [],
                        startToken: {
                            token: 'start block',
                            label: 'repositories',
                            indentation: 4,
                            text: '    repositories {',
                        },
                        endToken: {
                            token: 'end block',
                            label: null,
                            indentation: 4,
                            text: '    }',
                        },
                    },
                ],
                startToken: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('comments parse', t => {
    const tokens = [
        {
            token: 'start comment',
            label: null,
            indentation: 0,
            text: '/*',
        },
        {
            token: 'end comment',
            label: null,
            indentation: 0,
            text: '*/',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'start comment',
                    label: null,
                    indentation: 0,
                    text: '/*',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end comment',
                    label: null,
                    indentation: 0,
                    text: '*/',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('tokens within comments are just content', t => {
    const tokens = [
        {
            token: 'start comment',
            label: null,
            indentation: 0,
            text: '/*',
        },
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
        {
            token: 'end comment',
            label: null,
            indentation: 0,
            text: '*/',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'start comment',
                    label: null,
                    indentation: 0,
                    text: '/*',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end comment',
                    label: null,
                    indentation: 0,
                    text: '*/',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('mismatched block tokens within comments are okay (single start)', t => {
    const tokens = [
        {
            token: 'start comment',
            label: null,
            indentation: 0,
            text: '/*',
        },
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end comment',
            label: null,
            indentation: 0,
            text: '*/',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'start comment',
                    label: null,
                    indentation: 0,
                    text: '/*',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'start block',
                    label: 'allprojects',
                    indentation: 0,
                    text: 'allprojects {',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end comment',
                    label: null,
                    indentation: 0,
                    text: '*/',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('mismatched block tokens within comments are okay (single end)', t => {
    const tokens = [
        {
            token: 'start comment',
            label: null,
            indentation: 0,
            text: '/*',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
        {
            token: 'end comment',
            label: null,
            indentation: 0,
            text: '*/',
        },
    ];

    const expectedResult = {
        type: 'block',
        label: null,
        contents: [
            {
                type: 'content',
                token: {
                    token: 'start comment',
                    label: null,
                    indentation: 0,
                    text: '/*',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end block',
                    label: null,
                    indentation: 0,
                    text: '}',
                },
            },
            {
                type: 'content',
                token: {
                    token: 'end comment',
                    label: null,
                    indentation: 0,
                    text: '*/',
                },
            },
        ],
        startToken: null,
        endToken: null,
    };

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('end block without start block is an error', t => {
    const tokens = [
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = null;

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('more end block tokens than start block tokens is an error', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = null;

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('start block without end block is an error', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
    ];

    const expectedResult = null;

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});

test('more start block tokens than end block tokens is an error', t => {
    const tokens = [
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'start block',
            label: 'allprojects',
            indentation: 0,
            text: 'allprojects {',
        },
        {
            token: 'end block',
            label: null,
            indentation: 0,
            text: '}',
        },
    ];

    const expectedResult = null;

    const actualResult = parse(tokens);

    t.deepEqual(actualResult, expectedResult);
});
