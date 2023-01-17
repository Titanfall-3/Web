import { getToken } from "./store.js";
import { baseApiPath } from "./config";
import { writable } from "svelte/store";

export let accountData = writable<JSON>()

export function refreshAccount() {
    fetch(baseApiPath + '/api/account/refresh', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ token: getToken()})
    }).then(result => {
        if(result.ok) {
            return result.json()
        } else {
            throw "Not suitable responds."
        }
    }).then(resultJson => {
        if (resultJson.success) {
            accountData.set(resultJson.user);
            return;
        }
    }).catch(() => {
        return
    });
}