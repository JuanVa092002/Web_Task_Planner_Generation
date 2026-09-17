(function (root, factory) {
    const api = factory();
    if (typeof module !== 'undefined' && module.exports) {
        module.exports = api;
    }
    root.TaskAuth = api;
}(typeof globalThis !== 'undefined' ? globalThis : window, function () {
    function authorizationHeaders(token) {
        if (!token) {
            throw new Error('UNAUTHENTICATED');
        }
        return {
            Authorization: 'Bearer ' + token,
            'Content-Type': 'application/json'
        };
    }

    function shouldShowApp(isSignedIn) {
        return isSignedIn === true;
    }

    function clerkFrontendApi(publishableKey) {
        if (!publishableKey || publishableKey.indexOf('pk_') !== 0) {
            throw new Error('INVALID_PUBLISHABLE_KEY');
        }
        const encoded = publishableKey.split('_').slice(2).join('_');
        return BufferFromBase64(encoded).replace(/\$$/, '');
    }

    function clerkScriptUrls(publishableKey) {
        const domain = clerkFrontendApi(publishableKey);
        return {
            ui: 'https://' + domain + '/npm/@clerk/ui@1/dist/ui.browser.js',
            clerkJs: 'https://' + domain + '/npm/@clerk/clerk-js@6/dist/clerk.browser.js'
        };
    }

    function BufferFromBase64(value) {
        if (typeof atob === 'function') {
            return atob(value);
        }
        return Buffer.from(value, 'base64').toString('utf8');
    }

    return {
        authorizationHeaders: authorizationHeaders,
        shouldShowApp: shouldShowApp,
        clerkFrontendApi: clerkFrontendApi,
        clerkScriptUrls: clerkScriptUrls
    };
}));
