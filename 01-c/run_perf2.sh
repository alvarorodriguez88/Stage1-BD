#!/bin/bash

output_file="c-benchmark-results1.csv"

executable="./matrix_multiplication"

iterations=10

echo "Matrix size,Iteration,Execution time(ms),Memory usage(KB),CPU Usage(%)" > $output_file

run_iteration(){
	size=$1
	iteration=$2

	memory_output=$( /usr/bin/time -v $executable $size 2>&1)
	memory_usage=$(echo "$memory_output" | grep "Maximum resident set size" | awk '{print $6}')
	perf_output=$(perf stat -e task-clock $executable $size 2>&1)
	task_clock=$(echo $perf_output | awk -F',' '{print $2}' | cut -d" " -f 1)
	cpu_usage=$(echo "$perf_output" | grep "#" | awk '{print $5}' | tr ',' '.')
	echo "$size,$iteration,$task_clock,$memory_usage,$cpu_usage" >> $output_file
}



for size in 10 100 1000; do
	for iteration in $(seq 1 $iterations); do
		echo "Iteration $iteration for matrix size: $size"
		run_iteration $size $iteration
	done
done

