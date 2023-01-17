<script>
    export let user_value;
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
<!-- Start: Testimonials -->
<div class="container py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>Upcomming Playtests</h2>
        </div>
    </div>
    <div class="row gy-4 row-cols-1 row-cols-sm-2 row-cols-lg-3">
        <div class="col"></div>
        <div class="col">
            <div>
                <p class="border rounded border-0 p-4" style="background: var(--default-contrast-color);">TDM Multiplayer<br>3PM CET</p>
                <div class="d-flex"><img alt="playtestleader" class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="%sveltekit.assets%/img/presti.png">
                    <div>
                        <p class="fw-bold text-primary mb-0">Presti</p>
                        <p class="text-muted mb-0">Playtest-leader for this playtest</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="col"></div>
    </div>
</div>
<!-- End: Testimonials -->
<!-- Start: Articles Cards -->
<div class="container py-4 py-xl-5">
    <div class="row mb-5">
        <div class="col-md-8 col-xl-6 text-center mx-auto">
            <h2>News</h2>
            <p class="w-lg-50">Below you will find the newest News around this project which inclue but are not limit to changelogs and achievments!</p>
        </div>
    </div>
    <div class="row gy-4 row-cols-1 row-cols-md-2 row-cols-xl-3">
        <div class="col">
            <div class="card" style="background: var(--default-contrast-color);"><img alt="newsthumbnail" class="card-img-top w-100 d-block fit-cover" style="height: 200px;" src="https://cdn.bootstrapstudio.io/placeholders/1400x800.png">
                <div class="card-body p-4">
                    <p class="text-primary card-text mb-0">Changelog</p>
                    <h4 class="card-title">Update 0.3<br></h4>
                    <p class="card-text">Here is some content from the 0.3 update!</p>
                    <div class="d-flex"><img alt="newswriter" class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="%sveltekit.assets%/img/presti.png">
                        <div>
                            <p class="fw-bold mb-0">Presti</p>
                            <p class="text-muted mb-0">Administrator</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card" style="background: var(--default-contrast-color);"><img alt="newsthumbnail" class="card-img-top w-100 d-block fit-cover" style="height: 200px;" src="https://cdn.bootstrapstudio.io/placeholders/1400x800.png">
                <div class="card-body p-4">
                    <p class="text-primary card-text mb-0">Changelog</p>
                    <h4 class="card-title">Update 0.2<br></h4>
                    <p class="card-text">And here some from 0.2</p>
                    <div class="d-flex"><img alt="newswriter" class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="%sveltekit.assets%/img/presti.png">
                        <div>
                            <p class="fw-bold mb-0">Presti</p>
                            <p class="text-muted mb-0">Contributor</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <div class="card" style="background: var(--default-contrast-color);"><img alt="newswthumbnail" class="card-img-top w-100 d-block fit-cover" style="height: 200px;" src="https://cdn.bootstrapstudio.io/placeholders/1400x800.png">
                <div class="card-body p-4">
                    <p class="text-primary card-text mb-0">Changelog<br></p>
                    <h4 class="card-title">Update 0.1</h4>
                    <p class="card-text">And here from 0.1</p>
                    <div class="d-flex"><img alt="newswriter" class="rounded-circle flex-shrink-0 me-3 fit-cover" width="50" height="50" src="%sveltekit.assets%/img/presti.png">
                        <div>
                            <p class="fw-bold mb-0">Presti</p>
                            <p class="text-muted mb-0">Developer</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- End: Articles Cards -->