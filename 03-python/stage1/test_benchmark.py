import pytest
import numpy as np
from memory_profiler import memory_usage


def generateMatrixes(size):
    A = np.random.rand(size, size)
    B = np.random.rand(size, size)
    return A, B

def multiply(A, B):

    result = [[0 for _ in range(len(B[0]))] for _ in range(len(A))]

    for i in range(len(A)):
        for j in range(len(B[0])):
            for k in range(len(B)):
                result[i][j] += A[i][k] * B[k][j]

        return result


def memory_benchmark(size):
    A, B = generateMatrixes(size)
    return memory_usage((multiply, (A, B)), interval=0.1)



@pytest.mark.parametrize("size", [10, 100, 1000])
def test_function(benchmark, size):
    mem_usage = memory_benchmark(size)

    max_mem = max(mem_usage)
    min_mem = min(mem_usage)
    avg_mem = sum(mem_usage) / len(mem_usage)

    A, B = generateMatrixes(size)

    result = benchmark.pedantic(multiply, args=(A, B), rounds=1, warmup_rounds=5, iterations=10)

    print(f"Para tamaño {size}: Memoria máxima: {max_mem:.7f} MB, mínima: {min_mem:.7f} MB, media: {avg_mem:.7f} MB")