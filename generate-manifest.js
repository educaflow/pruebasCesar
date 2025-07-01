// generate-assets-manifest.js
const fs = require("fs");
const path = require("path");

const jsDir = path.join(__dirname, "src/main/webapp/js");
const cssDir = path.join(__dirname, "src/main/webapp/css");
const outputPath = path.join(__dirname, "src/main/webapp/manifest2.json");

function listFiles(dir, ext) {
    return fs.existsSync(dir)
        ? fs.readdirSync(dir).filter(file => file.endsWith(ext))
        : [];
}

const manifest = {
    scripts: listFiles(jsDir, ".js"),
    styles: listFiles(cssDir, ".css"),
};

fs.writeFileSync(outputPath, JSON.stringify(manifest, null, 2));
console.log(`✔️  Manifest written to ${outputPath}`);
