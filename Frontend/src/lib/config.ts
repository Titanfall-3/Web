export const baseApiPath = 'http://localhost:8444'
import cookie from "cookie"

export function getToken() {
    return cookie.parse(document.cookie).token
}