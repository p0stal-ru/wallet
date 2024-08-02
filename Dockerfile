# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем остальные исходные коды приложения
COPY src ./src

# Собираем проект и упаковываем его в jar-файл
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM amazoncorretto:17

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar-файл из первого этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Задаем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
