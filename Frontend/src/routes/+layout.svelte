<script lang="ts">

    // TODO:: use info from storage to access current info.

    import {wantsToLogin, wantsToRegister} from "../lib/store.js";
</script>

<!-- Start: Navbar Right Links (Dark) -->
<nav class="navbar navbar-dark navbar-expand-md bg-dark py-3" style="background: var(--default-contrast-color);">
    <div class="container"><a class="navbar-brand d-flex align-items-center" href="#toggle-nav"><span style="font-size: 30px;color: var(--text-color);">Titanfall 3</span></a><button data-bs-toggle="collapse" class="navbar-toggler" data-bs-target="#navcol-5"><span class="visually-hidden">Toggle navigation</span><span class="navbar-toggler-icon"></span></button>
        <div class="collapse navbar-collapse" id="navcol-5">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link active" on:click|preventDefault={() => {
                    wantsToLogin.update(v => (v) => '');
                    wantsToRegister.update(v => (v) => '');
                }} href="#home" style="color: var(--text-color);">Home</a></li>
                <li class="nav-item"><a class="nav-link" on:click|preventDefault={() => {
                    wantsToLogin.update(v => (v) => 'ye');
                    wantsToRegister.update(v => (v) => '');
                }} href="#login" style="color: var(--button-color);">Login</a></li>
                <li class="nav-item"><a class="nav-link" on:click|preventDefault={() => {
                    wantsToLogin.update(v => (v) => '');
                    wantsToRegister.update(v => (v) => 'ye');
                }} href="#register" style="color: var(--button-color);">Register</a></li>
            </ul>
        </div>
    </div>
</nav>
<!-- End: Navbar Right Links (Dark) -->
<slot/>
<!-- Start: Footer Dark -->
<footer class="text-center bg-dark" style="background: var(--default-contrast-color);">
    <div class="container text-white py-4 py-lg-5">
        <p style="color: var(--text-color);">Thank you for checking out this project!</p>
        <p class="text-muted mb-0">Made by the community for the community</p>
    </div>
</footer>
<!-- End: Footer Dark -->