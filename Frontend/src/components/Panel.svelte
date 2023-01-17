<script lang="ts">
    import {accountData} from "$lib/account";
    import {baseApiPath} from "$lib/config.js";
    import {getToken} from "$lib/store.js.js";

    let user_value;
    accountData.subscribe((u) => (user_value = u));

    let news;
    let error;
    let message;

    fetch(baseApiPath + '/api/news/get', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({token: getToken()})
    }).then(result => {
        if (result.ok) {
            return result.json()
        } else {
            throw error;
        }
    }).then(resultJson => {
        if (resultJson.success) {
            news = resultJson.data;
            error = false;
            return;
        }

        error = true;
        message = resultJson.message;
    }).catch(() => {
        error = true;
        message = 'Server Error!'
    })
</script>

<!-- Start: Hero Banner -->
<section class="py-4 py-xl-5">
    <div class="container h-100">
        <div class="row h-100">
            <div class="col-md-10 col-xl-8 text-center d-flex d-sm-flex d-md-flex justify-content-center align-items-center mx-auto justify-content-md-start align-items-md-center justify-content-xl-center">
                <div>
                    <h2 class="text-uppercase fw-bold mb-3">Dashboard</h2>
                    <p class="mb-4">Welcome back {user_value.username}</p>
                </div>
            </div>
        </div>
    </div>
</section>
<!-- End: Hero Banner -->
<!-- Start: Articles Cards -->
<div class="container py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>News</h2>
            <p class="w-lg-50">Below you will find the newest News around this project which inclue but are not limit to
                changelogs and achievments!</p>
        </div>
    </div>
    <div class="row gy-4 row-cols-1 row-cols-md-2 row-cols-xl-3">
        {#if news}
            {#each news as nw}
                <div class="col">
                    <div class="card" style="background: var(--default-contrast-color);"><img alt="newsthumbnail"
                                                                                              class="card-img-top w-100 d-block fit-cover"
                                                                                              style="height: 200px;"
                                                                                              src="{nw.thumbnail}">
                        <div class="card-body p-4">
                            <p class="text-primary card-text mb-0">Changelog</p>
                            <h4 class="card-title">{nw.title}<br></h4>
                            <p class="card-text">{nw.content}</p>
                            <div class="d-flex"><img alt="newswriter"
                                                     class="rounded-circle flex-shrink-0 me-3 fit-cover"
                                                     width="50" height="50" src="%sveltekit.assets%/img/presti.png">
                                <div>
                                    <p class="fw-bold mb-0">Presti</p>
                                    <p class="text-muted mb-0">Administrator</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            {/each}
        {/if}
    </div>
</div>
<!-- End: Articles Cards -->