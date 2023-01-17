import {writable} from "svelte/store";
import {browser} from "$app/environment";
import {baseApiPath} from "$lib/config";
import {getToken} from "$lib/store.js";

let persistedUser = browser && localStorage.getItem('user')
export let accountData = writable(persistedUser ? JSON.parse(persistedUser) : '')

if (browser) {
    accountData.subscribe(u => localStorage.user = JSON.stringify(u))
}

export function refresh() {
    if (getToken() === undefined) {
        localStorage.removeItem('user');
        return;
    }
    fetch(baseApiPath + '/api/account/refresh', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({token: getToken()})
    }).then(result => {
        if (result.ok) {
            return result.json()
        } else {
            throw "Not suitable responds."
        }
    }).then(resultJson => {
        if (resultJson.success) {
            accountData.update(v => v = resultJson.user);
            document.cookie = "token=" + resultJson.message + "; SameSite=Strict; path=/"
            return;
        }

        accountData.update((u) => u = '');
        console.error(resultJson.message);
    }).catch(() => {
        console.error("Error while refreshing the Session!");
    });
}

export function logout() {
    fetch(baseApiPath + '/api/account/invalidate', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({token: getToken()})
    }).then(result => {
        if (result.ok) {
            return result.json()
        } else {
            throw "Not suitable responds."
        }
    }).then(resultJson => {
        if (resultJson.success) {
            document.cookie = "token=0; SameSite=Strict; path=/";
            accountData.update((u) => u = '');
            return;
        }

        console.error(resultJson.message);
    }).catch(() => {
        console.error("Error while logging out the Session!");
    });
}

