FROM ubuntu:22.04

RUN apt-get update && apt-get install -y build-essential cmake libasio-dev curl

WORKDIR /app
COPY . .

RUN cmake . && make

CMD ["./routing_service"]