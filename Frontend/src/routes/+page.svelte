<script lang="ts">
    import {accountData, refresh} from "../lib/account";
    import {wantsToLogin, wantsToRegister} from "../lib/store.js";
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
    let unsubscribeLogin = wantsToLogin.subscribe((u) => (tryingLogin = u));
    let unsubscribeRegister = wantsToRegister.subscribe((u) => (tryingRegister = u));

    onMount(refresh);
    onDestroy(unsubscribe)
</script>

{#if user_value}
    {#if user_value.admin}
        <AdminPanel user_value/>
    {:else}
        <Panel user_value/>
    {/if}
{:else if tryingLogin}
    <Login/>
{:else if tryingRegister}
    <Register/>
{:else}
    <Homepage/>
{/if}