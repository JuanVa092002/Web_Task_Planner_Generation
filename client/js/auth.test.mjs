import { createRequire } from 'node:module';
import assert from 'node:assert/strict';
import test from 'node:test';

const require = createRequire(import.meta.url);
const auth = require('./auth.js');

test('authorizationHeaders adds Bearer token', () => {
    assert.deepEqual(auth.authorizationHeaders('abc.def'), {
        Authorization: 'Bearer abc.def',
        'Content-Type': 'application/json'
    });
});

test('authorizationHeaders rejects missing token', () => {
    assert.throws(() => auth.authorizationHeaders(''), /UNAUTHENTICATED/);
    assert.throws(() => auth.authorizationHeaders(null), /UNAUTHENTICATED/);
});

test('shouldShowApp is true only when signed in', () => {
    assert.equal(auth.shouldShowApp(true), true);
    assert.equal(auth.shouldShowApp(false), false);
    assert.equal(auth.shouldShowApp(undefined), false);
});

test('clerkFrontendApi decodes publishable key domain', () => {
    const key = 'pk_test_ZXhjaXRpbmctc2VhaG9yc2UtNTk3MS5jbGVyay5hY2NvdW50cy5kZXYk';
    assert.equal(auth.clerkFrontendApi(key), 'exciting-seahorse-5971.clerk.accounts.dev');
});

test('clerkScriptUrls point to Clerk CDN', () => {
    const key = 'pk_test_ZXhjaXRpbmctc2VhaG9yc2UtNTk3MS5jbGVyay5hY2NvdW50cy5kZXYk';
    const urls = auth.clerkScriptUrls(key);
    assert.match(urls.ui, /exciting-seahorse-5971\.clerk\.accounts\.dev\/npm\/@clerk\/ui@1/);
    assert.match(urls.clerkJs, /@clerk\/clerk-js@6\/dist\/clerk\.browser\.js/);
});
