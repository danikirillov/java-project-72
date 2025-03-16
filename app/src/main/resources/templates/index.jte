@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes
@param BasePage page


@template.layout.page(
page = page,
content = @`
    <div class="container my-4">
        <h2>Hallo mein Freund</h2>
        <form action="${NamedRoutes.urlsPath()}" method="post" class="mt-3">
            <div class="mb-3">
                <label for="urlInput" class="form-label">Урл</label>
                <input type="search" name="url" id="urlInput" class="form-control" placeholder="Введи урл"/>
            </div>
            <button type="submit" class="btn btn-primary">Добавить</button>
        </form>
    </div>
`
)