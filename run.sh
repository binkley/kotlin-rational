#!/usr/bin/env bash
# shellcheck disable=SC2214,SC2215

readonly package=hm.binkley.math
readonly artifactId=kotlin-rational
readonly version=2.2.0-SNAPSHOT

# No editable parts below here

export PS4='+${BASH_SOURCE}:${LINENO}:${FUNCNAME[0]:+${FUNCNAME[0]}():} '

set -e
set -u
set -o pipefail

readonly jar=target/$artifactId-$version-jar-with-dependencies.jar
readonly progname="${0##*/}"

function print-help() {
    cat <<EOH
Usage: $progname [-dh][-L|--language java|kotlin]
Runs examples for this library.

  -L, --language [LANGUAGE]  runs the example for LANGUAGE; languages:
                                java
                                kotlin (the default)
  -d, --debug                print script execution to STDERR
  -h, --help                 display this help and exit

Examples:
  $progname            Runs the kotlin example
  $progname -L java    Runs the java example
EOH
}

function bad-language() {
    local language="$1"

    cat <<EOM
$progname: invalid language -- '$language'
Try '$progname --help' for more information.
EOM
}

function bad-option() {
    local opt="$1"

    cat <<EOM
$progname: invalid option -- '$opt'
Try '$progname --help' for more information.
EOM
}

function mangle-kotlin-classname() {
    local IFS=.

    local -a parts
    read -r -a parts <<<"$1"
    local last="${parts[-1]}"

    case "$last" in
    *-* | *Kt) ;;
    *) last="${last}Kt" ;;
    esac
    last="${last//-/_}"
    last=""${last^}

    parts[-1]="$last"

    echo "${parts[*]}"
}

function rebuild-if-needed() {
    [[ -e "$jar" && -z "$(find src/main -type f -newer "$jar")" ]] && return

    ./mvnw -C -Dmaven.test.skip=true package
}

debug=false
language=kotlin
while getopts :L:d:h-: opt; do
    [[ $opt == - ]] && opt=${OPTARG%%=*} OPTARG=${OPTARG#*=}
    case $opt in
    L | language) case "$OPTARG" in
        kotlin | java) language="$OPTARG" ;;
        *)
            bad-language "$OPTARG"
            exit 2
            ;;
        esac ;;
    d | debug) debug=true ;;
    h | help)
        print-help
        exit 0
        ;;
    *)
        bad-option "$opt"
        exit 2
        ;;
    esac
done
shift $((OPTIND - 1))

$debug && set -x

case $language in
java) set - -cp "$jar" "$package.JavaMain" "$@" ;;
kotlin) set - -jar "$jar" "$@" ;;
esac

$debug && set -x # "set - ..." clears the -x flag

rebuild-if-needed

exec java "$@"
