package org.example;

import com.sun.management.OperatingSystemMXBean;
import org.openjdk.jmh.annotations.*;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)

public class MatrixMultiplicationBenchmarking {

	@State(Scope.Thread)
	public static class Operands{
		@Param({"10", "100", "1000"})
		private int size;
		private double[][] a;
		private double[][] b;
		private List<Long> memoryUsages;
		private List<Long> cpuPercentages;

		@Setup
		public void setup() {
			a = new double[size][size];
			b = new double[size][size];
			memoryUsages = new ArrayList<>();
			cpuPercentages = new ArrayList<>();
			Random random = new Random();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					a[i][j] = random.nextDouble();
					b[i][j] = random.nextDouble();
				}
			}
		}

		public double calculateAverage(List<Long> values) {
			long sum = 0;
			for (long value : values) {
				sum += value;
			}
			return values.isEmpty() ? 0 : (double) sum / values.size();
		}

		@TearDown(Level.Trial)
		public void printResults() {
			double avgMemoryUsage = calculateAverage(memoryUsages);
			double avgCpuTime = calculateAverage(cpuPercentages);

			System.out.println("_");
			System.out.println("Average Memory used: " + avgMemoryUsage + " bytes");
			System.out.println("_");
			System.out.println("CPU percentage used: " + avgCpuTime + " %");
			System.out.println("_");
		}



	}

	@Benchmark
	public void multiplication(Operands operands){

		Runtime runtime = Runtime.getRuntime();
		runtime.gc();
		long beforeMemory = getMemory(runtime);

		OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		double beforeCpuLoad = getCpuLoad(osBean);

		new MatrixMultiplication().execute(operands.a, operands.b);

		double afterCpuLoad = getCpuLoad(osBean);
		double usedCpu = afterCpuLoad - beforeCpuLoad;

		long afterMemory = getMemory(runtime);
		long usedMemory = afterMemory - beforeMemory;

		operands.memoryUsages.add(usedMemory);
		operands.cpuPercentages.add((long) usedCpu);
	}

	private static double getCpuLoad(OperatingSystemMXBean osBean) {
		double cpuLoad = osBean.getProcessCpuLoad() * 100;
		return cpuLoad;
	}

	private static long getMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
	}

}











