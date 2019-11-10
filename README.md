# StackOverflow Posts Analyzer


## Installation

### From Source

Use Maven to load the dependencies and build the jar file.

```
mvn clean install
```

Build the docker container.

```
docker build \
    --tag 'posts-analyzer' \
    --build-arg APP_JAR=`find ./target -maxdepth 1 -name 'posts-analyzer-*.jar' \
    ./
```

Run the docker container.

```
docker run \
    --publish '8080:8080' \
    --memory '512m' \
    'posts-analyzer'
```

This binds port 8080 of the docker container to port 8080 of the host system.


### From Docker Hub

Pull the docker container from docker hub.

```
docker pull 'sqlr/posts-analyzer'
```

Run the docker container.

```
docker run \
    --publish '8080:8080' \
    --memory '512m' \
    'sqlr/posts-analyzer'
```

This binds port 8080 of the docker container to port 8080 of the host system.


## Analyse XML

Use the application by sending a HTTP POST request with JSON body to path `/analyze` on the exposed port.
The body should contain a 'url' element with the location of the XML file to analyse.

```
curl \
    --request POST \
    --header 'Content-Type:application/json;charset=UTF-8' \
    --data '{"url":"http://example.com/posts-data.xml"}' \
    'http://localhost:8080/analyze/'
```
