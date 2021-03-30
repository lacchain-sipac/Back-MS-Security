################### Stage 2: A minimal docker image with command to run the app 
FROM openjdk:8-jre-alpine
ENV APP_FILE ms-security-0.0.1.jar

ENV APP_HOME /usr/apps

ENV APP_SOURCE ms-security/target

ARG JAR_FILE=target/ms-security-0.0.1.jar
COPY $JAR_FILE $APP_HOME/
COPY rsa-public.pem /security/rsa-public.pem
COPY rsa-private.pem /security/rsa-private.pem
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -Djava.security.egd=file:/dev/./urandom -jar $APP_FILE"]
EXPOSE 8090

# mvn clean install
# docker build  -t us.gcr.io/hondu-pf/ms-security:1.2.5 -f Dockerfile .
# gcloud docker -- push us.gcr.io/hondu-pf/ms-security:1.2.5
	