@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container py-5">
    <h1 class="text-center mb-4">Urls List</h1>
    @if(page.getUrls().isEmpty())
        <p class="text-muted text-center">Урлов нет</p>
    @else
        <div class="row">
            @for(var url : page.getUrls())
                <div class="col-md-4 mb-3">
                    <div class="card shadow-sm">
                        <div class="card-body">
                            <h2 class="card-title h5">
                                <a href="${NamedRoutes.urlsPath(url.getId())}" class="text-decoration-none">${url.getName()}</a>
                            </h2>
                        </div>
                    </div>
                </div>
            @endfor
        </div>
    @endif
    </div>
`
)