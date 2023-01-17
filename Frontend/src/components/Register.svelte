<script lang="ts">
    import {baseApiPath} from "$lib/config";
    import {accountData} from "$lib/account";

    let username: string, password: string, email: string, invite: string;
    let error;
    let message;

    function register() {
        fetch(baseApiPath + '/api/account/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: username,
                password: password,
                email: email,
                invite: invite,
                captchaKey: 'test'
            })
        }).then(result => {
            if (result.ok) {
                return result.json()
            } else {
                throw error;
            }
        }).then(resultJson => {
            if (resultJson.success) {
                document.cookie = "token=" + resultJson.data + "; SameSite=Strict; path=/"
                accountData.update(v => v = resultJson.user)
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
<!-- Start: Login Form Basic -->
<section class="position-relative py-4 py-xl-5">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md-8 col-xl-6 text-center mx-auto">
                <h2>Register</h2>
                <p class="w-lg-50">You will need to register to access the User Dashboard to see new information about upcoming play-test and more!</p>
            </div>
        </div>
        <div class="row d-flex justify-content-center">
            <div class="col-md-6 col-xl-4">
                <div class="card mb-5" style="background: var(--default-contrast-color);">
                    <div class="card-body d-flex flex-column align-items-center">
                        <div class="bs-icon-xl bs-icon-circle bs-icon-primary bs-icon my-4"><svg xmlns="http://www.w3.org/2000/svg" width="1em" height="1em" fill="currentColor" viewBox="0 0 16 16" class="bi bi-person">
                            <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z"></path>
                        </svg></div>
                        <form class="text-center" method="post">
                            <div class="mb-3"><input class="form-control" bind:value={username} type="text" name="username" placeholder="Username"></div>
                            <div class="mb-3"><input class="form-control" bind:value={email} type="email" name="email" placeholder="Email"></div>
                            <div class="mb-3"><input class="form-control" bind:value={password} type="password" name="password" placeholder="Password"></div>
                            <div class="mb-3"><input class="form-control" bind:value={invite} type="password" name="invite" placeholder="Invite"></div>
                            <div class="mb-3"><button class="btn btn-primary d-block w-100" type="submit" on:click|preventDefault={register}>Register</button></div>
                            <p class="text-muted" style="color: var(--text-color);">You have a account? <a href="#login">Login here</a></p>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- End: Login Form Basic -->