#!/usr/bin/env sh
# Gradle wrapper
GRADLE_WRAPPER_JAR="$( dirname "$0" )/gradle/wrapper/gradle-wrapper.jar"
JAVA_OPTS="-Xmx4096m -XX:MaxMetaspaceSize=512m"
exec java $JAVA_OPTS -jar "$GRADLE_WRAPPER_JAR" "$@"
