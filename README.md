# Container usage patterns

This is the repository for my "Container (Usage) Patterns" talk.

## Set up

```
docker pull nginx:1.17.7-alpine \
  && docker pull alpine:3.9 \
  && docker pull hadolint/hadolint \
  && docker pull wagoodman/dive
```

## Volumes/Networks

```sh
# create a volume
❯ docker volume create my-vol

# start a container and mount the volume
❯ docker run -it -v my-vol:/tmp alpine:3.9 sh
# in the container cd to /tmp
/ cd tmp

# start anoter container and mount the same volume
❯ docker run -it -v my-vol:/tmp alpine:3.9 sh
# in the container cd to /tmp
/ cd tmp
/ # touch a file
echo 'hello from one container' >> shared-file.txt

# In the other container
cat shared-file.txt
```

### Networks

```sh
# create a network
docker network create my-net

# start a container joining that network
docker run -d --name frontend --net my-net nginx:1.17.7-alpine

# start another container and inspect the /etc/hosts
# The last entry is this containers IP address
docker run --rm --net my-net alpine:3.9 cat /etc/hosts

# start another container and inspect the /etc/hosts
docker run --rm --net my-net alpine:3.9 cat /etc/resolv.conf

# You can ping another container BY NAME on the same network
docker run -it --rm --net=my-net alpine:3.9
> ping frontend
> wget frontend
> cat index.html

# Start a container on the default network
```

## dive

- Build `container-patterns` without any optimizing in `build.gradle`

```sh
❯ docker build -t demo -f Unoptimized-Dockerfile .
```

- Explore the filesystem of `builder` image using:

```sh
❯ docker run --rm -it \
      -v /var/run/docker.sock:/var/run/docker.sock \
      wagoodman/dive:latest demo
```

- Build `container-patterns` by putting the build files first
- Repeat with `dive`

## Hadolint

```sh
❯ docker run --rm -i hadolint/hadolint < jenkins-dockerfile
```

## Docker From Docker

```sh
❯ docker run --rm -it -v /var/run/docker.sock:/var/run/docker.sock ubuntu:19.04
/ # in the container
/ apt update && apt install -y docker.io
/ docker container ls
```

## Sidecar

```sh
❯ cid=$(docker run -d nginx:1.17.7-alpine)
❯ docker run -it --pid=container:${cid} alpine:3.9 sh
```

## Format

```sh
❯ docker image ls hadolint/hadolint --format '{{.Digest}}'
❯ docker inspect hadolint/hadolint --format '{{json .RootFS.Layers}}'
❯ docker container ls --format 'table {{.Names}}\t{{.Image}}'
❯ # build container-patterns to an image called 'demo'
❯ docker inspect --format '{{json .Config.Labels}}' demo | jq
```

## ONBUILD

```sh
❯ # be sure to call it build-onbuild
❯ docker build -t build-onbuild -f Dockerfile.base .
❯ # this uses build-onbuild as its "base" that causes the ONBUILD to fire
❯ docker build -t demo -f Dockerfile.final .
```

## Dockerignore

```sh
❯ # Comment out `.dockerignore` file
❯ docker build -t demo .
❯ # watch how much was loaded in the daemon context
❯ # Uncomment out .dockerignore file
❯ docker build -t demo .
```

## ENTRYPOINT+CMD

```sh
❯ # Look inside Dockerfile, there are 3 separate ways to build the code
❯ # Use the first option to build an image
❯ docker build -t demo .
❯ docker run --name demo1 demo

❯ # In another terminal
❯ docker exec demo1 ps aux
❯ # inspect PID 1

❯ # Now use the second "exec" format option to build an image
❯ docker build -t demo .
❯ # Repeat the above exercise
```

## Multi-stage dockerfiles

```sh
❯ # In this directory there is a parallel-build-dockerfile
❯ # Build it
❯ export DOCKER_BUILDKIT=1
❯ # Build it again
```
