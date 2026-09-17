function toggleAuthView(signedIn) {
    const authScreen = document.querySelector('#authScreen');
    const appShell = document.querySelector('#appShell');
    const showApp = window.TaskAuth.shouldShowApp(signedIn);

    authScreen.classList.toggle('is-hidden', showApp);
    appShell.classList.toggle('is-hidden', !showApp);
}

function showAuthTab(tab) {
    const signIn = document.querySelector('#sign-in');
    const signUp = document.querySelector('#sign-up');
    const signInBtn = document.querySelector('#showSignIn');
    const signUpBtn = document.querySelector('#showSignUp');
    const showSignIn = tab === 'sign-in';

    signIn.classList.toggle('is-hidden', !showSignIn);
    signUp.classList.toggle('is-hidden', showSignIn);
    signInBtn.classList.toggle('is-active', showSignIn);
    signUpBtn.classList.toggle('is-active', !showSignIn);
}

async function renderClerkSession() {
    const signedIn = Boolean(window.Clerk && window.Clerk.isSignedIn);
    toggleAuthView(signedIn);

    if (!signedIn) {
        window.Clerk.mountSignIn(document.querySelector('#sign-in'));
        window.Clerk.mountSignUp(document.querySelector('#sign-up'));
        return;
    }

    window.Clerk.mountUserButton(document.querySelector('#user-button'));
    window.startAuthenticatedApp(async () => window.Clerk.session.getToken());
}

window.addEventListener('load', async function () {
    document.querySelector('#showSignIn').addEventListener('click', () => showAuthTab('sign-in'));
    document.querySelector('#showSignUp').addEventListener('click', () => showAuthTab('sign-up'));

    await window.Clerk.load({
        ui: { ClerkUI: window.__internal_ClerkUICtor }
    });

    window.Clerk.addListener(({ user }) => {
        if (user) {
            renderClerkSession();
        } else {
            window.__taskAppStarted = false;
            toggleAuthView(false);
            showAuthTab('sign-in');
            window.Clerk.mountSignIn(document.querySelector('#sign-in'));
            window.Clerk.mountSignUp(document.querySelector('#sign-up'));
        }
    });

    await renderClerkSession();
});
