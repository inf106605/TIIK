#!/bin/sh

bin_dir='./bin/'
main_class='tiik.Main'


java -cp "${bin_dir}" "${main_class}" "$@"
exit $?
