<script lang="ts">
    import { baseApiPath } from "../lib/config";

    // TODO:: add check for session.
    let tryingLogin = true;
    let tryingRegister = false;
    export let siteKey = "9dc06c51-f075-4f7a-9335-9346a4c7280a";
    let error;
    let message;

    let username: string, password: string, email: string, invite: string;

    function login() {
        fetch(baseApiPath + '/api/account/login', {
            method: 'post',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username: username, password: username})
        }).then(result => {
            if(result.ok) {
                return result.json()
            } else {
                throw error;
            }
        }).then(resultJson => {
            if (resultJson.success) {
                document.cookie = "token=" + resultJson.data + "; SameSite=Strict; Secure; path=/"
                location.assign("/panel")
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
{/if}

{#if tryingLogin}
<section id="login" class="login-prompt">
    <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
        <form>
            <h1>Login</h1>
            <i class="fas fa-user"><input type="text" bind:value={username} placeholder="Username" /></i>
            <i class="fas fa-lock"><input type="password" bind:value={password} placeholder="Password" /></i>
            <div class="h-captcha" data-sitekey="{siteKey}"></div>
            <button type="submit" on:click={login}>Sign In</button>
        </form>
    </div>
</section>
{:else if tryingRegister}
<section id="register" class="register-prompt">
    <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
        <form>
            <h1>Register</h1>
            <i class="fas fa-user"><input type="text" bind:value={username} placeholder="Username" /></i>
            <i class="fas fa-envelope"><input type="text" bind:value={email} placeholder="Email" /></i>
            <i class="fas fa-lock"><input type="password" bind:value={password} placeholder="Password" /></i>
            <i class="fas fa-envelope-open-text"><input type="password" bind:value={invite} placeholder="Your Invite" /></i>
            <div class="h-captcha" data-sitekey="{siteKey}"></div>
            <button type="submit">Sign Up</button>
        </form>
    </div>
</section>
{/if}