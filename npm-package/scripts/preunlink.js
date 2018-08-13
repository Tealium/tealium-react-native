const {
    lex,
    parse,
    toLines,
    addTealiumRepo,
    removeTealiumRepo,
} = require('./gradle-build-file-parser');
const fs = require('fs');
const path = require('path');

const filePath = path.join('android', 'build.gradle');
const existingFileContents = fs.readFileSync(filePath, 'utf8');
const existingLines = existingFileContents.split('\n');
const existingTokens = existingLines.map(lex);
const existingAst = parse(existingTokens);
if (existingAst) {
    const newAst = removeTealiumRepo(existingAst);
    if (newAst) {
        const newLines = toLines(newAst);
        const newFileContents = newLines.join('\n');
        fs.writeFileSync(filePath, newFileContents);
    }
}

