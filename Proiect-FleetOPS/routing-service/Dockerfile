FROM ubuntu:22.04
RUN apt-get update && apt-get install -y build-essential cmake libasio-dev
WORKDIR /app
COPY . .
RUN cmake . && make
EXPOSE 8081
CMD ["./routing_service"]