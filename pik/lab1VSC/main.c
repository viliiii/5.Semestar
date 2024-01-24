#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include "omp.h"

int main(){
#pragma omp parallel
    {
        int ID = omp_get_thread_num();
        printf("Hello world from thread ID: (%d)\n", ID);
    }
    return 0;
}
