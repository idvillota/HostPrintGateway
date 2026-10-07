#!/bin/sh
APP_HOME=$( cd "$( dirname "$0" )" && pwd )
WRAPPER_JAR="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
exec java -jar "$WRAPPER_JAR" "$@"
