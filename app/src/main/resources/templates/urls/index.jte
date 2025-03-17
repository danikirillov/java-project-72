@import hexlet.code.model.UrlCheck
@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.UrlsPage
@import java.time.format.DateTimeFormatter
@param UrlsPage page
@param DateTimeFormatter dateTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

@template.layout.page(
page = page,
content = @`
    <div class="container py-5">
        <h1 class="text-center mb-4">Urls List</h1>
        @if(page.getUrls().isEmpty())
            <div class="alert alert-info text-center" role="alert">
                Урлов нет
            </div>
        @else
            <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                @for(var url : page.getUrls())
                    <div class="col">
                        <div class="card h-100 shadow-sm hover-shadow">
                            <div class="card-body">
                                <h2 class="card-title h5 mb-3">
                                    <a href="${NamedRoutes.urlsPath(url.id())}" class="text-decoration-none stretched-link">${url.name()}</a>
                                </h2>
                                <p class="card-text text-muted small">
                                    <i class="bi bi-clock"></i> Aйди: ${url.id()}
                                </p>
                                @if(url.lastCheck().isPresent()) 
                                <p class="card-text text-muted small">
                                    <i class="bi bi-clock"></i> 
                                    Проверен: ${url.lastCheck().map(UrlCheck::getCreatedAt).map(date -> date.format(dateTime)).orElse("не проверен")}
                                    Код: ${url.lastCheck().map(UrlCheck::getStatusCode).map(String::valueOf).orElse("не проверен")}
                                </p>
                                @endif
                            </div>
                        </div>
                    </div>
                @endfor
            </div>
        @endif
    </div>
`
)