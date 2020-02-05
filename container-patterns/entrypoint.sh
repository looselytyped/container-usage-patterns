#!/bin/bash

# Exit on error. Append "|| true" if you expect an error.
set -o errexit
# Exit on error inside any functions or subshells.
set -o errtrace
# Do not allow use of undefined vars. Use ${VAR:-} to use an undefined VAR
set -o nounset
# Catch the error in case mysqldump fails (but gzip succeeds) in `mysqldump |gzip`
set -o pipefail

if [ "$1" = 'default' ]; then
  # do default thing here
  exec java -jar /var/app.jar
else
  echo "Running user supplied arg"
  # if the user supplied say /bin/bash
  exec "$@"
fi
