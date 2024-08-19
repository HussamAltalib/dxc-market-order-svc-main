# Use an official OpenJDK runtime as a parent image
FROM mhmd1492/dxc-market:app-base

# Set environment variables
ARG SERVICE
ARG PROJECT_NAME
ARG NEXUS_REPO_URL
ARG REVISION

ENV SERVICE ${SERVICE}
ENV REVISION ${REVISION}
ENV PROJECT_NAME ${PROJECT_NAME}
ENV NEXUS_REPO_URL ${NEXUS_REPO_URL}


ENV DB_USERNAME=root
ENV DB_PASSWORD=admin123

# Set the working directory inside the container
WORKDIR /app

RUN curl -L -o app.jar ${NEXUS_REPO_URL}/${PROJECT_NAME}/${REVISION}/${PROJECT_NAME}-${REVISION}.jar

# Expose the port your application will run on (if needed)
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
