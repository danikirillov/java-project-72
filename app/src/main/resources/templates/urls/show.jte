@import hexlet.code.dto.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.time.format.DateTimeFormatter
@param UrlPage page
@param DateTimeFormatter dateTime = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

@template.layout.page(
page = page,
content = @`
    <div class="container py-5">
        <h1 class="text-center mb-3">${page.getUrl().getName()}</h1>
        <h3 class="text-center mb-2">ID: ${page.getUrl().getId()}</h3>
        <p class="text-center text-muted mb-4">Created: ${page.getUrl().getCreatedAt().format(dateTime)}</p>

        <div class="text-center mb-5">
            <form method="post" action="${NamedRoutes.urlsCheckPath(page.getUrl().getId())}">
                <button type="submit" class="btn btn-primary">Проверить</button>
            </form>
        </div>

        <h3 class="mb-3">Проверки</h3>
        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-light">
                <tr>
                    <th>Айди</th>
                    <th>Код ответа</th>
                    <th>Title</th>
                    <th>H1</th>
                    <th>Description</th>
                    <th>Дата</th>
                </tr>
                </thead>
                <tbody>
                @for(var check : page.getChecks())
                    <tr>
                        <td>${check.getId()}</td>
                        <td>${check.getStatusCode()}</td>
                        <td>${check.getTitle()}</td>
                        <td>${check.getH1()}</td>
                        <td>${check.getDescription()}</td>
                        <td>${check.getCreatedAt().format(dateTime)}</td>
                    </tr>
                @endfor
                </tbody>
            </table>
        </div>
    </div>
`
)