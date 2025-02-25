@import hexlet.code.util.NamedRoutes

@template.layout.page(
content = @`
    <div> Hallo mein Freund
    </div>
    <form action=${NamedRoutes.urlsPath()}>
        <label>
            Url
            <input type="search" name="url" />
        </label>
        <input type="submit" value="Add" />
    </form>
`
)