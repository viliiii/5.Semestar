//
// Created by vilim on 23.1.2024.
//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <omp.h>
//NAPOMENA: program se poziva ili s 0 ili s 1 argumentom: 
//OMP_NUM_THREADS=  mct <number of samples> 

//s 0 argumenata radi s onoliko dretvi koliko trenutni sustav ima jezgri pomocu 
//funkcije omp_get_num_procs().
int main(int argc, char *argv[]){

    if (argc > 2) {
        printf("Usage: OMP_NUM_THREADS %s <number of threads>\n", argv[0]);
        return 1;
    }
    int N;
    int TN;

    if(argc == 2){
        N = atoi(argv[1]);
        TN = omp_get_max_threads();
        printf("Number of points: %d \n", N);
        printf("Number of threads: %d \n", TN);
    }else{
        N = 500000000;
        TN = omp_get_num_procs();
        printf("Number of points not specified, default number of points is used: %d \n", N);
        printf("Number of threads not specified, default number of threads is used: %d \n", TN);
    }


    double pocetak = omp_get_wtime();


    double sum = 0.0;
    double random_dot;
    
    
    omp_set_num_threads(TN);

    #pragma omp parallel private(random_dot)
{
    unsigned int seed = omp_get_thread_num() + 1;

    #pragma omp for reduction(+:sum)
    for(int i=0; i < N; i++){
        random_dot = (double)rand_r(&seed) /RAND_MAX * 10.0;
        sum += random_dot*random_dot*random_dot;
    }
}
    

    double estimate = (10.0*sum)/N;
    double stddev = sqrt(pow(2500-estimate, 2.0)) ;
    double stderror = stddev/ sqrt(N);

    double kraj = omp_get_wtime();

    double elapsed = kraj-pocetak;


    printf("Monte Carlo Estimate: %lf\n", estimate);

    printf("Error (Standard Error): %lf\n", stderror);

    printf("Elapsed time: %lf\n", elapsed);




    return 0;
}