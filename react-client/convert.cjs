const fs = require('fs');
const path = require('path');

const srcDir = '../client';
const destDir = './src/pages';
const componentsDir = './src/components';

if (!fs.existsSync(destDir)) fs.mkdirSync(destDir, { recursive: true });
if (!fs.existsSync(componentsDir)) fs.mkdirSync(componentsDir, { recursive: true });

const files = fs.readdirSync(srcDir).filter(f => f.endsWith('.html'));

function htmlToJsx(html) {
    let jsx = html
        .replace(/class=/g, 'className=')
        .replace(/for=/g, 'htmlFor=')
        .replace(/<!--[\s\S]*?-->/g, '')
        .replace(/<img(.*?)[^\/]*?>/g, (match, p1) => `<img${p1} />`)
        .replace(/<input(.*?)[^\/]*?>/g, (match, p1) => `<input${p1} />`)
        .replace(/<br(.*?)>/g, '<br />')
        .replace(/<hr(.*?)>/g, '<hr />')
        .replace(/style="([^"]*)"/g, (match, styleString) => {
            const styleObj = {};
            styleString.split(';').forEach(rule => {
                if(!rule.trim()) return;
                const parts = rule.split(':');
                if(parts.length >= 2) {
                    const key = parts[0].trim();
                    const value = parts.slice(1).join(':').trim();
                    const camelKey = key.replace(/-([a-z])/g, g => g[1].toUpperCase());
                    styleObj[camelKey] = value;
                }
            });
            return `style={${JSON.stringify(styleObj)}}`;
        })
        // Handle <style> tags for React
        .replace(/<style>([\s\S]*?)<\/style>/g, '<style dangerouslySetInnerHTML={{__html: `$1`}} />');
        
    const bodyMatch = jsx.match(/<body[^>]*>([\s\S]*?)<\/body>/i);
    let content = bodyMatch ? bodyMatch[1] : jsx;
    
    // Remove scripts
    content = content.replace(/<script[\s\S]*?<\/script>/gi, '');
    
    // Fix unclosed tags for React by trying to just put it in a fragment, but since the regex isn't perfect, let's wrap it.
    // Some unclosed tags might still exist, we'll fix them manually if compiler complains.
    return content.trim();
}

files.forEach(file => {
    const html = fs.readFileSync(path.join(srcDir, file), 'utf8');
    const jsxContent = htmlToJsx(html);
    const componentName = file.replace('.html', '').split('-').map(s => s.charAt(0).toUpperCase() + s.slice(1)).join('');
    
    const code = `import React from 'react';
import { Link } from 'react-router-dom';

export default function ${componentName}() {
    return (
        <div className="${componentName.toLowerCase()}-page">
            ${jsxContent}
        </div>
    );
}
`;
    // also replace a hrefs with Link to
    const finalCode = code.replace(/<a([^>]*?)href="([^"]*)"([^>]*?)>/g, (match, p1, p2, p3) => {
        if(p2.startsWith('#') || p2.startsWith('http')) return match; // keep external or hash links as a
        const to = p2.replace('.html', '');
        return `<Link${p1}to="/${to}"${p3}>`;
    }).replace(/<\/a>/g, '</Link>').replace(/<Link([^>]*?)href="([^"]*)"([^>]*?)>/g, '<a$1href="$2"$3>').replace(/<\/Link>/g, '</a>'); // fix back if it was hash
    
    // Actually just a simple replace
    let safeCode = code.replace(/<a([^>]*?)href="([^"]*?)\.html"([^>]*?)>/g, '<Link$1to="/$2"$3>').replace(/<\/a>/g, '</a>');
    safeCode = safeCode.replace(/<\/a>/g, (match, offset, str) => {
        // rough heuristic: if we replaced <a with <Link, we'd need </Link>. It's easier to just use <a> for now.
        return match;
    });

    // Let's just keep <a> tags for now or use window.location.href. Standard React can still use <a> tags if we don't care about full SPA routing yet, but user asked for React.
    fs.writeFileSync(path.join(destDir, `${componentName}.jsx`), code);
    console.log(`Converted ${file} to ${componentName}.jsx`);
});

// Create App.jsx
const appCode = `import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './styles.css';
${files.map(f => {
    const comp = f.replace('.html', '').split('-').map(s => s.charAt(0).toUpperCase() + s.slice(1)).join('');
    return `import ${comp} from './pages/${comp}';`;
}).join('\n')}

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Index />} />
                ${files.map(f => {
                    if(f === 'index.html') return '';
                    const comp = f.replace('.html', '').split('-').map(s => s.charAt(0).toUpperCase() + s.slice(1)).join('');
                    const route = f.replace('.html', '');
                    return `<Route path="/${route}" element={<${comp} />} />`;
                }).join('\n                ')}
            </Routes>
        </Router>
    );
}
`;
fs.writeFileSync('./src/App.jsx', appCode);

// update main.jsx
const mainCode = `import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './styles.css'
import './api-client.js'

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
`;
fs.writeFileSync('./src/main.jsx', mainCode);
console.log('Done!');
