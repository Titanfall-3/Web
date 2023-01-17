<script lang="ts">
    import {accountData, refresh, logout} from "$lib/account";
    import {wantsToLogin, wantsToRegister} from "$lib/store.js";
    import Homepage from "../components/Homepage.svelte";
    import Panel from "../components/Panel.svelte";
    import AdminPanel from "../components/AdminPanel.svelte";
    import Login from "../components/Login.svelte";
    import Register from "../components/Register.svelte";
    import {onDestroy, onMount} from "svelte";

    let user_value;
    let tryingLogin;
    let tryingRegister;


    let unsubscribe = accountData.subscribe((u) => (user_value = u));
    let unsubscribeLogin = wantsToLogin.subscribe((u) => (tryingLogin = u))
    let unsubscribeRegister = wantsToRegister.subscribe((u) => (tryingRegister = u));

    onMount(refresh);
    onDestroy(() => {
        unsubscribe();
        unsubscribeLogin();
        unsubscribeRegister();
    })
</script>
<!-- Start: Navbar Right Links (Dark) -->
<nav class="navbar navbar-dark navbar-expand-md bg-dark py-3" style="background: var(--default-contrast-color);">
    <div class="container"><a class="navbar-brand d-flex align-items-center" href="#toggle-nav"><span
            style="font-size: 30px;color: var(--text-color);">Titanfall 3</span></a>
        <button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-5"><span
                class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navcol-5">
            <ul class="navbar-nav ms-auto">

                {#if user_value}
                    <li class="nav-item"><a class="nav-link active"
                                            on:click|preventDefault={logout}
                                            href="#logout" style="color: var(--text-color);">Logout</a></li>
                {:else if tryingLogin}
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = false);}}
                                            href="#home" style="color: var(--button-color);">Home</a></li>
                    <li class="nav-item"><a class="nav-link active"
                                            on:click|preventDefault={() => {wantsToRegister.update((u) => u = false); wantsToLogin.update((u) => u = true);}}
                                            href="#login" style="color: var(--text-color);">Login</a></li>
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = true);}}
                                            href="#register" style="color: var(--button-color);">Register</a></li>
                {:else if tryingRegister}
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = false);}}
                                            href="#home" style="color: var(--button-color);">Home</a></li>
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToRegister.update((u) => u = false); wantsToLogin.update((u) => u = true);}}
                                            href="#login" style="color: var(--button-color);">Login</a></li>
                    <li class="nav-item"><a class="nav-link active"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = true);}}
                                            href="#register" style="color: var(--text-color);">Register</a></li>
                {:else}
                    <li class="nav-item"><a class="nav-link active"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = false);}}
                                            href="#home" style="color: var(--text-color);">Home</a></li>
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToRegister.update((u) => u = false); wantsToLogin.update((u) => u = true);}}
                                            href="#login" style="color: var(--button-color);">Login</a></li>
                    <li class="nav-item"><a class="nav-link"
                                            on:click|preventDefault={() => {wantsToLogin.update((u) => u = false); wantsToRegister.update((u) => u = true);}}
                                            href="#register" style="color: var(--button-color);">Register</a></li>
                {/if}
            </ul>
        </div>
    </div>
</nav>
<!-- End: Navbar Right Links (Dark) -->
{#if user_value}
    {#if user_value.admin}
        <AdminPanel/>
    {:else}
        <Panel/>
    {/if}
{:else if tryingLogin}
    <Login/>
{:else if tryingRegister}
    <Register/>
{:else}
    <Homepage/>
{/if}