@import hexlet.code.dto.UrlPage
@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="container py-5">
        <h1 class="text-center">${page.getUrl().getName()}</h1>
    </div>
`
)