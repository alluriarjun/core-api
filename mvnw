#!/bin/sh
# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script
# ----------------------------------------------------------------------------

set -e

# OS specific support.
cygwin=false;
darwin=false;
mingw=false
case "$(uname)" in
  CYGWIN*) cygwin=true ;;
  DARWIN*) darwin=true ;;
  MINGW*) mingw=true ;;
esac

# Absolute path to the project base dir.
APP_HOME="$(cd "$(dirname "$0")" && pwd)"
APP_NAME="Maven"
APP_BASE_NAME="$(basename "$0")"

# Default JVM options.
DEFAULT_JVM_OPTS=""

# Use the `JAVA_HOME` env var if set, otherwise just use `java`.
if [ -n "$JAVA_HOME" ]; then
    JAVA_CMD="$JAVA_HOME/bin/java"
    if [ ! -x "$JAVA_CMD" ]; then
        echo "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME" >&2
        echo "Please set the JAVA_HOME variable in your environment to match the location of your Java installation." >&2
        exit 1
    fi
else
    JAVA_CMD="java"
    which java >/dev/null 2>&1 || {
        echo "ERROR: JAVA_HOME is not set and no 'java' command could be found." >&2
        exit 1
    }
fi

# Maven wrapper properties
WRAPPER_PROPERTIES="$APP_HOME/.mvn/wrapper/maven-wrapper.properties"

# Read the distribution URL from the properties file
if [ -f "$WRAPPER_PROPERTIES" ]; then
    DISTRIBUTION_URL=$(grep "distributionUrl" "$WRAPPER_PROPERTIES" | cut -d'=' -f2- | tr -d '[:space:]')
fi

# Determine the Maven home directory
MAVEN_USER_HOME="${MAVEN_USER_HOME:-$HOME/.m2}"
MAVEN_BASE_FOLDER="$MAVEN_USER_HOME/wrapper/dists"
DISTRIBUTION_FILENAME="${DISTRIBUTION_URL##*/}"
DISTRIBUTION_NAME="${DISTRIBUTION_FILENAME%.zip}"
MAVEN_HOME="$MAVEN_BASE_FOLDER/$DISTRIBUTION_NAME/$DISTRIBUTION_NAME"

# Download Maven if not already present
if [ ! -f "$MAVEN_HOME/bin/mvn" ]; then
    mkdir -p "$MAVEN_BASE_FOLDER"
    TMP_DIR="$MAVEN_BASE_FOLDER/tmp"
    mkdir -p "$TMP_DIR"
    TMP_FILE="$TMP_DIR/$DISTRIBUTION_FILENAME"
    echo "Downloading $DISTRIBUTION_URL to $TMP_FILE"
    if command -v curl >/dev/null 2>&1; then
        curl -o "$TMP_FILE" "$DISTRIBUTION_URL"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$TMP_FILE" "$DISTRIBUTION_URL"
    else
        echo "ERROR: Neither curl nor wget is available." >&2
        exit 1
    fi
    mkdir -p "$MAVEN_BASE_FOLDER/$DISTRIBUTION_NAME"
    unzip -q "$TMP_FILE" -d "$MAVEN_BASE_FOLDER/$DISTRIBUTION_NAME"
    rm -f "$TMP_FILE"
fi

CLASSPATH="$MAVEN_HOME/boot/plexus-classworlds-*.jar"

# shellcheck disable=SC2086
exec "$JAVA_CMD" \
  $DEFAULT_JVM_OPTS \
  $JAVA_OPTS \
  $MAVEN_OPTS \
  -classpath "$CLASSPATH" \
  "-Dmaven.home=$MAVEN_HOME" \
  "-Dmaven.multiModuleProjectDirectory=$APP_HOME" \
  org.codehaus.plexus.classworlds.launcher.Launcher "$@"