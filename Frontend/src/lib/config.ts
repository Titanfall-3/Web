export const baseApiPath = 'http://10.8.0.1:8444'
import cookie from "cookie"

export function getToken() {
    return cookie.parse(document.cookie).token
}