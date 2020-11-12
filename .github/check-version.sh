#!/usr/bin/env bash

set -eo pipefail

GITHUB_TAG_URL_BASE="https://github.com/$GITHUB_REPOSITORY/releases/tag"

# Analyze target version to be release

WANTED_VERSION="$(cat ./gradle.properties | grep currentVersion | cut -d'=' -f2)"
WANTED_VERSION="$(echo $WANTED_VERSION)" # Trim whitespaces


# Ensure version name follows semantic versioning

VERSION_REGEX='^[0-9]+(\.[0-9]+){2}$'
if [[ ! $WANTED_VERSION =~ $VERSION_REGEX ]]; then
  echo "[ERROR] Version number $WANTED_VERSION is not valid." >&2
  echo "Please follow 3-number semantic versioning system." >&2
  exit
fi


# Check published releases

HTTP_STATUS=$(curl -LI "${GITHUB_TAG_URL_BASE}/${WANTED_VERSION}" -o /dev/null -w '%{http_code}\n' -s)

if [[ $HTTP_STATUS = '400' ]]; then
  echo "[ERROR] Version number $WANTED_VERSION cannot be used as a tag." >&2
  exit
elif [[ $HTTP_STATUS = '200' ]]; then
  echo "[ERROR] This version has been already released. Abort." >&2
  exit
fi

echo $WANTED_VERSION
