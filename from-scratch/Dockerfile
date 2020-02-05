FROM golang:1.13.7-stretch as builder

WORKDIR /go/src/app
COPY main.go main.go

RUN go build

FROM scratch
COPY --from=builder /go/src/app/app ./

ENTRYPOINT ["./app"]
