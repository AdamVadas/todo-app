#!/bin/sh
mvn clean package && docker build -t org.example/todo-app .
docker rm -f todo-app || true && docker run -d -p 8080:8080 -p 4848:4848 --name todo-app org.example/todo-app 
