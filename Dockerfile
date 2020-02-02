FROM airhacks/glassfish
COPY ./target/todo-app.war ${DEPLOYMENT_DIR}
