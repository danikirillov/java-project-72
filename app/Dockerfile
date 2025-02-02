FROM gradle:latest

WORKDIR /app

COPY /app .

RUN gradle installDist

CMD ./build/install/app/bin/app