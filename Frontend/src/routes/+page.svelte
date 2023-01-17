<script lang="ts">
    import {accountData, refresh} from "../lib/account";
    import Homepage from "../components/Homepage.svelte";
    import Panel from "../components/Panel.svelte";
    import AdminPanel from "../components/AdminPanel.svelte";
    import Login from "../components/Login.svelte";
    import Register from "../components/Register.svelte";
    import {onDestroy, onMount} from "svelte";

    let user_value;
    accountData.subscribe((u) => (user_value = u));

    let unsubscribe = accountData.subscribe((u) => (user_value = u));

    onMount(refresh)

    let tryingLogin = false;
    let tryingRegister = false;

    onDestroy(unsubscribe)
</script>

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