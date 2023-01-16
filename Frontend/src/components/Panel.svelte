<script>
    import { accountData } from "../lib/account";
    import AdminPanel from "./AdminPanel.svelte";
</script>

{#if accountData.isAdmin}
<section>
    <AdminPanel />
</section>
{:else}
<section id="user" class="center">
    <h1>User Area</h1>
    <div class="rounded-rect" style="display: flex; justify-content: center; align-items: center; background-color: #333;">
        <h1>Welcome {accountData.username}</h1>
        <p>There isn't much you can do sadly, as this page is only there for verification!<br>But you can check out the newest Changelogs below!</p>
    </div>
</section>
{/if}