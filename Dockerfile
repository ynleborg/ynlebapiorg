FROM adoptopenjdk/openjdk11:alpine-jre
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} app.jar
ADD store.json store.json
ADD leaderboard_final.json leaderboard_final.json
ADD leaderboardcombined_final.json leaderboardcombined_final.json
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
