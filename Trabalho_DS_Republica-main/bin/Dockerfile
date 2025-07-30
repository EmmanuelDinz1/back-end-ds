# Estágio de build
FROM ubuntu:latest AS build

RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      openjdk-21-jdk \
      maven \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /workspace

# Copia o pom e o código-fonte
COPY pom.xml . 
COPY src ./src

# Empacota o Spring Boot (gera o arquivo .jar no diretório target/)
RUN mvn clean package -DskipTests

# Estágio de runtime
FROM ubuntu:latest AS runtime

# Instalando o JDK 21 (em vez do JRE 17)
RUN apt-get update \
 && apt-get install -y --no-install-recommends openjdk-21-jre \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia o fat-jar do build anterior
COPY --from=build /workspace/target/trab_republica-0.0.1-SNAPSHOT.jar . 

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "trab_republica-0.0.1-SNAPSHOT.jar"]
