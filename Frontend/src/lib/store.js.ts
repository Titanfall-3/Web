import { writable } from 'svelte/store'

import cookie from "cookie"

export let wantsToLogin = writable('')
export let wantsToRegister = writable('')

export function getToken() {
    return cookie.parse(document.cookie).token
}

export let requesting = writable(false)