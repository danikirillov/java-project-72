@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Java-project-72</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
          rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
          crossorigin="anonymous"/>
</head>
<body class="d-flex flex-column min-vh-100">
<header class="bg-light py-3">
    <div class="container d-flex justify-content-start">
        <h1 class="me-4">
            <a class="text-decoration-none" href="${NamedRoutes.homePath()}">Home</a>
        </h1>
        <h1>
            <a class="text-decoration-none" href="${NamedRoutes.urlsPath()}">Urls</a>
        </h1>
    </div>
</header>

<main class="container my-4">
    @if(page != null && page.getFlash() != null)
        <div class="alert ${page.getFlashType()}">${page.getFlash()}</div>
    @endif
    ${content}
</main>

<footer class="mt-auto bg-dark text-white py-3">
    <div class="container text-center">
        <p>ho ho ho</p>
    </div>
</footer>
</body>
</html>