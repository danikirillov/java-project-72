FROM gradle:latest

WORKDIR /app

RUN gradle installDist

CMD ./build/install/app/bin/app