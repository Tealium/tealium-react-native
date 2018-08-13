function lex(rawLineText) {
    function indentationSize(text) {
        const indentationRegex = /^(\s*)\S+/; // match leading whitespace
        const matchResult = text.match(indentationRegex);

        return (matchResult instanceof Array && typeof matchResult[1] === 'string') ?
            matchResult[1].length : null;
    }

    function readLabel(text) {
        const labelRegex = /^([^{}]*){/;
        const matchResult = text.match(labelRegex);
        let label = null;

        if (
            matchResult instanceof Array &&
            typeof matchResult[1] === 'string' &&
            matchResult[1] !== ''
        ) {
            label = matchResult[1].trim();
        }

        return label;
    }

    function chooseToken(text) {
        // start of block comment contains "/*"
        if (/\/\*/.test(text)) {
            return 'start comment';
        }

        // end of block comment contains "*/"
        if (/\*\//.test(text)) {
            return 'end comment';
        }

        // line comments containing "//" before "{" or "}"
        if (/^[^{}]*\/\//.test(text)) {
            return 'other';
        }

        // start of block has a "{" without preceding "//"
        if (/^[^{}]*{/.test(text)) {
            return 'start block';
        }

        // end of block has a "}" without preceding "//"
        if (/^[^{}]*}/.test(text)) {
            return 'end block';
        }

        return 'other';
    }

    const indentation = indentationSize(rawLineText);
    const trimmedText = rawLineText.trim();
    const token = chooseToken(trimmedText);
    const label = (token === 'start block') ?
        readLabel(trimmedText) : null;

    return {
        token,
        label,
        indentation,
        text: rawLineText,
    };
}

function parse(tokens) {
    const stack = [];
    let curBlock = {
        type: 'block',
        label: null,
        contents: [],
        startToken: null,
        endToken: null,
    };

    let blockComment = false;
    let error = false;

    tokens.forEach((token) => {
        if (!error) {
            if (blockComment) {
                curBlock.contents.push({
                    type: 'content',
                    token,
                });

                if (token.token === 'end comment') {
                    blockComment = false;
                }
            } else {
                switch (token.token) {
                    case 'start block':
                        stack.push(curBlock);
                        curBlock = {
                            type: 'block',
                            label: token.label,
                            contents: [],
                            startToken: token,
                            endToken: null,
                        };
                        break;
                    case 'end block':
                        curBlock.endToken = token;
                        {
                            // an empty stack means the end of
                            // a block that was never started
                            const outerBlock = stack.pop();
                            if (!outerBlock) {
                                error = true;
                            } else {
                                outerBlock.contents.push(curBlock);
                                curBlock = outerBlock;
                            }
                        }
                        break;
                    case 'start comment':
                        blockComment = true;
                        curBlock.contents.push({
                            type: 'content',
                            token,
                        });
                        break;
                    case 'end comment': // case is explicit; default would catch
                    case 'other': // case is explicit; default would catch
                    default:
                        curBlock.contents.push({
                            type: 'content',
                            token,
                        });
                        break;
                }
            }
        }
    });

    // a non-empty stack means there were more starts of blocks
    // than ends of blocks. Mismatch
    if (stack.length !== 0) {
        error = true;
    }

    return error ? null : curBlock;
}

function toLines(ast, lines = []) {
    if (ast.type === 'block') {
        if (ast.startToken != null) {
            lines.push(ast.startToken.text);
        }

        ast.contents.forEach((nestedAst) => {
            toLines(nestedAst, lines);
        });

        if (ast.endToken != null) {
            lines.push(ast.endToken.text);
        }
    } else {
        lines.push(ast.token.text);
    }

    return lines;
}

function addTealiumRepo(ast) {
    let result = null;

    if (ast.type === 'block' && ast.label === null) {
        let allprojectsBlock = null;
        let repositoriesBlock = null;

        ast.contents.forEach(nestedAst => {
            if (nestedAst.type === 'block' &&
                nestedAst.label === 'allprojects'
            ) {
                allprojectsBlock = nestedAst;
            }
        });

        if (allprojectsBlock) {
            allprojectsBlock.contents.forEach(nestedAst => {
                if (nestedAst.type === 'block' &&
                    nestedAst.label === 'repositories'
                ) {
                    repositoriesBlock = nestedAst;
                }
            });
        }

        if (repositoriesBlock) {
            const indentation = repositoriesBlock.startToken.indentation;
            const indent = ' '.repeat(indentation);

            const mavenBlock = {
                type: 'block',
                label: 'maven',
                contents: [
                    {
                        type: 'content',
                        token: {
                            token: 'other',
                            label: null,
                            indentation: indentation * 3,
                            text: `${indent}${indent}${indent}url "http://maven.tealiumiq.com/android/releases/"`,
                        },
                    },
                ],
                startToken: {
                    token: 'start block',
                    label: 'maven',
                    indentation: indentation * 2,
                    text: `${indent}${indent}maven {`,
                },
                endToken: {
                    token: 'end block',
                    label: null,
                    indentation: indentation * 2,
                    text: `${indent}${indent}}`,
                },
            };

            repositoriesBlock.contents.push(mavenBlock);
            result = ast;
        }
    }

    return result;
}

function removeTealiumRepo(ast) {
    let result = null;

    if (ast.type === 'block' && ast.label === null) {
        let allprojectsBlock = null;
        let repositoriesBlock = null;

        ast.contents.forEach(nestedAst => {
            if (nestedAst.type === 'block' &&
                nestedAst.label === 'allprojects'
            ) {
                allprojectsBlock = nestedAst;
            }
        });

        if (allprojectsBlock) {
            allprojectsBlock.contents.forEach(nestedAst => {
                if (nestedAst.type === 'block' &&
                    nestedAst.label === 'repositories'
                ) {
                    repositoriesBlock = nestedAst;
                }
            });
        }

        if (repositoriesBlock) {
            const newContents = repositoriesBlock.contents.filter(nestedAst => {
                return !(
                    nestedAst.type === 'block' &&
                    nestedAst.label === 'maven' &&
                    nestedAst.contents.length === 1 &&
                    nestedAst.contents[0].type === 'content' &&
                    nestedAst.contents[0].token.text.trim() ===
                        'url "http://maven.tealiumiq.com/android/releases/"'
                );
            });

            repositoriesBlock.contents = newContents;
            result = ast;
        }
    }

    return result;
}

module.exports = {
    lex,
    parse,
    toLines,
    addTealiumRepo,
    removeTealiumRepo,
};
