
# include <stdio.h>
# include <stdlib.h>
# include <time.h>

void generateMatrixes(int size, double **A, double **B, double **C){
	for (int i = 0; i < size; i++){
		for (int j = 0; j < size; j++){
			A[i][j] = (double)rand() / RAND_MAX;
			B[i][j] = (double)rand() / RAND_MAX;
			C[i][j] = 0.0;
		}
	}
}

void multiply(int size, double **A, double **B, double **C){
	for (int i = 0; i < size; i++){
		for (int j = 0; j < size; j++){
			for (int k = 0; k < size; k++){
				C[i][j] += A[i][k] * B[k][j];
			}
		}
	}
}


int main(int argc, char *argv[]){
	
	if (argc != 2){
		printf("Usage: %s <matrix size>\n", argv[0]);
		return 1;
	}
	int size = atoi(argv[1]);
	
	double **A = (double **)malloc(size * sizeof(double*));
	double **B = (double **)malloc(size * sizeof(double*));
	double **C = (double **)malloc(size * sizeof(double*));
	for (int i = 0; i < size; i++){
		A[i] = (double *)malloc(size * sizeof(double*));
		B[i] = (double *)malloc(size * sizeof(double*));
		C[i] = (double *)malloc(size * sizeof(double*));
	}
	
	srand(time(NULL));
	generateMatrixes(size, A, B, C);

	multiply(size, A, B, C);
	
	for (int i = 0; i < size; i++){
		free(A[i]);
		free(B[i]);
		free(C[i]);
	}
	free(A);
	free(B);
	free(C);
	
	return 0;
}




