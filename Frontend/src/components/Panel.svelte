<script>
    import {accountData} from "../lib/account";
    import AdminPanel from "./AdminPanel.svelte";

    let user_value;
    accountData.subscribe((u) => (user_value = u));
</script>

{#if user_value.admin}
    <section>
        <AdminPanel {user_value}/>
    </section>
{:else}
    <section id="user" class="center">
        <h1>User Area</h1>
        <div class="rounded-rect" style="background-color: #333;">
            <br>
            <h1 style="text-align: center">Welcome<br>{user_value.username}</h1>
            <br>
        </div>
        <br>
        <div class="rounded-rect" style="background-color: #333;">
            <br>
            <p style="text-align: center">There isn't much you can do sadly, as this page is only there for
                verification!<br>But you can check out the newest Changelogs below!</p>
            <br>
        </div>
    </section>
{/if}