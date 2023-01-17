import { writable } from "svelte/store";
import {browser} from "$app/environment";
import {baseApiPath} from "$lib/config";
import {getToken} from "$lib/store.js";

let persistedUser = browser && localStorage.getItem('user')
export let accountData = writable(persistedUser ? JSON.parse(persistedUser) : '')

if (browser) {
    accountData.subscribe(u => localStorage.user = JSON.stringify(u))
}

function refresh() {
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
            accountData.set(resultJson.user);
            return;
        }

        console.error(resultJson.message);
    }).catch(() => {
        console.error("Error while refreshing the Session!");
    });
}

function logout() {
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
            accountData.update((u) => u = '');
            return;
        }

        console.error(resultJson.message);
    }).catch(() => {
        console.error("Error while refreshing the Session!");
    });
}

