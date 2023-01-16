<script lang="ts">
    import { baseApiPath } from "../lib/config";
    import { accountData, refreshAccount } from "../lib/account";
    import { isLoggedIn } from "../lib/store.js";
    import Panel from "../components/Panel.svelte";
    import { onMount } from "svelte";

    onMount(() => {
        refreshAccount()
    })

    let tryingLogin = true;
    export let siteKey = "9dc06c51-f075-4f7a-9335-9346a4c7280a";
    let error;
    let message;

    let username: string, password: string, email: string, invite: string;

    function login() {
        fetch(baseApiPath + '/api/account/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password, captchaKey: 'test'})
        }).then(result => {
            if(result.ok) {
                return result.json()
            } else {
                throw error;
            }
        }).then(resultJson => {
            if (resultJson.success) {
                document.cookie = "token=" + resultJson.data + "; SameSite=Strict; Secure; path=/"
                accountData.set(resultJson.user)
                error = false;
                return;
            }

            error = true;
            message = resultJson.message;
        }).catch(() => {
            error = true;
            message = 'Server Error!'
        })
    }

    function register() {
        fetch(baseApiPath + '/api/account/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: password, email: email, invite: invite, captchaKey: 'test'})
        }).then(result => {
            if(result.ok) {
                return result.json()
            } else {
                throw error;
            }
        }).then(resultJson => {
            if (resultJson.success) {
                document.cookie = "token=" + resultJson.data + "; SameSite=Strict; Secure; path=/"
                accountData.set(resultJson.user)
                error = false;
                return;
            }

            error = true;
            message = resultJson.message;
        }).catch(() => {
            error = true;
            message = 'Server Error!'
        })
    }
</script>

{#if error}
    <section id="error" class="center">
        <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
            <p style="color: red">{message}</p>
        </div>
    </section>
{/if}

{#if tryingLogin && !isLoggedIn(accountData)}
<section id="login" class="login-prompt">
    <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
        <form>
            <h1>Login</h1>
            <i class="fas fa-user"><input type="text" on:input={() => error = false} bind:value={username} placeholder="Username" /></i>
            <i class="fas fa-lock"><input type="password" on:input={() => error = false} bind:value={password} placeholder="Password" /></i>
            <div class="h-captcha" data-sitekey="{siteKey}" data-theme="dark"></div>
            <button type="submit" on:click|preventDefault={login}>Sign In</button>
            <br />
            <p>No account? <a on:click|preventDefault={() => tryingLogin = false} href="#register">Register here</a></p>
            <br />
        </form>
    </div>
</section>
{:else if !tryingLogin && !isLoggedIn(accountData)}
<section id="register" class="register-prompt">
    <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
        <form>
            <h1>Register</h1>
            <i class="fas fa-user"><input type="text" on:input={() => error = false} bind:value={username} placeholder="Username" /></i>
            <i class="fas fa-envelope"><input type="text" on:input={() => error = false} bind:value={email} placeholder="Email" /></i>
            <i class="fas fa-lock"><input type="password" on:input={() => error = false} bind:value={password} placeholder="Password" /></i>
            <i class="fas fa-envelope-open-text"><input type="password" on:input={() => error = false} bind:value={invite} placeholder="Your Invite" /></i>
            <div class="h-captcha" data-sitekey="{siteKey}" data-theme="dark"></div>
            <button type="submit" on:click|preventDefault={register}>Sign Up</button>
            <br />
            <p>You have a account? <a on:click|preventDefault={() => tryingLogin = true} href="#login">Login here</a></p>
            <br />
        </form>
    </div>
</section>
{:else if isLoggedIn(accountData)}
    <Panel />
{/if}