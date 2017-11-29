src_dir=./src/
bin_dir=./bin/
main_class=tiik.Main
main_class_bin_path=./bin/tiik/Main.class


.PHONY: comp
comp: ${main_class_bin_path}

.PHONY: run
run: comp
	java -cp '${bin_dir}' ${main_class}

${bin_dir}%.class: ${src_dir}%.java $(shell find '${src_dir}' -type f)
	mkdir -p '${bin_dir}'
	javac -cp '${src_dir}' -d '${bin_dir}' '$<'

.PHONY: clean
clean:
	rm -rf '${bin_dir}'

