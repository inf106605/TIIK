#!/bin/sh

bin_dir='./bin/'
main_class='tiik.Main'


if ! make ; then
	exit 1
fi

java -cp "${bin_dir}" "${main_class}" "$@"
exit $?
