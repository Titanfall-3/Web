import { getToken, baseApiPath } from "./config";
import { writable } from "svelte/store";

export const accountData = writable<any>({})

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
            document.cookie = "token=" + resultJson.data + "; SameSite=Strict; Secure; path=/"
            accountData.set(resultJson.user);
            return;
        }
    }).catch(() => {
        return
    });
}