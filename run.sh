#!/usr/bin/env bash
# shellcheck disable=SC2214,SC2215

readonly package=hm.binkley.math
readonly artifactId=kotlin-rational
readonly version=2.1.0-SNAPSHOT

# No editable parts below here

export PS4='+${BASH_SOURCE}:${LINENO}:${FUNCNAME[0]:+${FUNCNAME[0]}():} '

set -e
set -u
set -o pipefail

readonly jar=target/$artifactId-$version-jar-with-dependencies.jar
readonly progname="${0##*/}"

function print-help() {
    cat <<EOH
Usage: $progname [-Xdh] [CLASS] [ARGUMENTS]
Runs a single-jar Kotlin project.

With no CLASS, assume the jar is executable.

  -X, --executable  stop processing command line
  -d, --debug       print script execution to STDERR
  -h, --help        display this help and exit

Examples:
  $progname            Runs the executable jar with no arguments to main
  $progname -X an-arg  Runs the executable jar passing "an-arg" to main
  $progname a-class    Runs the main from "a-class"
EOH
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
executable=false
while getopts :Xdh-: opt; do
    [[ $opt == - ]] && opt=${OPTARG%%=*} OPTARG=${OPTARG#*=}
    case $opt in
    X | executable)
        executable=true
        break
        ;;
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
((0 == $#)) && executable=true

if $executable; then
    set - -jar "$jar" "$@"
else
    readonly class="$(mangle-kotlin-classname "$package.$1")"
    shift
    set - -cp "$jar" "$class" "$@"
fi

$debug && set -x # "set - ..." clears the -x flag

rebuild-if-needed

exec java "$@"
