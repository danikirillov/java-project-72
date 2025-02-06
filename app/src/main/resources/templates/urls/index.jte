@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <h1>Urls List</h1>
    @if(page.getUrls().isEmpty())
        <p>No urls to show</p>
    @else
        @for(var url : page.getUrls())
            <div>
                <h2>
                    <a href="${NamedRoutes.urlsPath(url.getId())}">${url.getName()}</a>
                </h2>
            </div>
        @endfor
    @endif
`
)