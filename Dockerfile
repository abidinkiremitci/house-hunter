FROM eclipse-temurin:17.0.6_10-jdk-jammy as builder
MAINTAINER oblomov-todo.xyz

RUN mkdir /workspace
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn/ ./.mvn/
RUN ./mvnw dependency:go-offline

COPY . .
RUN ./mvnw clean install -DskipTests


FROM eclipse-temurin:17.0.6_10-jre-jammy as runner
MAINTAINER oblomov-todo.xyz

RUN mkdir /opt/house-hunter
WORKDIR /opt/house-hunter

COPY --from=builder /workspace/target/*.jar house-hunter.jar

COPY --from=builder /workspace/deployment/funda_nl.cer funda_nl.cer

RUN keytool -importcert -file funda_nl.cer -alias funda_nl.cer -cacerts -storepass changeit -noprompt

EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "house-hunter.jar"]