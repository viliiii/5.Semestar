//
// Created by vilim on 23.1.2024.
//
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
int main(int argc, char *argv[]){

    if (argc > 2) {
        printf("Upotreba: %s <broj uzoraka>\n", argv[0]);
        return 1;
    }
    int N;

    if(argc == 2){
        N = atoi(argv[1]);
        printf("Number of points: %d \n", N);
    }else{
        N = 500000000;
        printf("Number of points not specified, default number of points is used: %d \n", N);
    }

    
    clock_t pocetak = clock();

    unsigned int seed = (unsigned int)time(NULL);
    srand(seed);
    double sum = 0.0;
    double random_dot;
    for(int i=0; i < N; i++){
        random_dot = (double)rand() /RAND_MAX * 10.0;
        sum += random_dot*random_dot*random_dot;
    }

    double estimate = (10.0*sum)/N;
    double stddev = sqrt(pow(2500-estimate, 2.0)) ;
    double stderror = stddev/ sqrt(N);

    clock_t kraj = clock();

    double elapsed = (double)(kraj-pocetak) / CLOCKS_PER_SEC;


    printf("Monte Carlo Estimate: %lf\n", estimate);

    printf("Error (Standard Error): %lf\n", stderror);

    printf("Elapsed time: %lf\n", elapsed);


    return 0;
}