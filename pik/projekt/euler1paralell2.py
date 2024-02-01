import numpy as np
from scipy.integrate import solve_ivp
import time
from mpi4py import MPI
import sys
import matplotlib.pyplot as plt

startTime = time.time()

def mean_squared_error(true_values, approx_values):
    min_len = min(len(true_values), len(approx_values))
    true_values = true_values[:min_len]
    approx_values = approx_values[:min_len]
    return np.mean((true_values - approx_values) ** 2)

def analytical_solution(t):
    return np.exp(t)

def dif_eq(t, y):
    return y

def euler_method(func, t_span, y0, h):
    t_values = np.arange(t_span[0], t_span[1] + 2 * h, h)
    y_values = np.zeros_like(t_values)

    y_values[0] = y0

    for i in range(1, len(t_values)):
        y_values[i] = y_values[i - 1] + h * func(t_values[i - 1], y_values[i - 1])

    return t_values, y_values

def solve_scipy_worker(t_span, y0, h):
    return solve_ivp(dif_eq, t_span, [y0], method='RK45', max_step=h)

def parallel_solve_with_mpi(h_values, rank, size, comm):
    t_span = (0, 5)
    y0 = 1.0

    mse_values = []

    for i, h in enumerate(h_values):
            t_euler, y_euler = euler_method(dif_eq, t_span, y0, h)
            sol_scipy = solve_scipy_worker(t_span, y0, h)

            mse = mean_squared_error(sol_scipy.y[0], y_euler)
            mse_values.append((h, mse))

            plt.figure(figsize=(10, 6))
            plt.plot(sol_scipy.t, sol_scipy.y[0], label=f'solve_ivp (h={h})', color='blue', linewidth=3)
            plt.plot(t_euler, y_euler, label=f'Eulerova metoda (h={h})', color='r', linewidth=1)
            plt.title('Aproksimacija rješenja diferencijalne jednadžbe')
            plt.xlabel('Vrijeme')
            plt.ylabel('Rješenje')
            plt.legend()
            plt.show()


    all_mse_values = comm.gather(mse_values, root=0)

    if rank == 0:
        for rank, mse_list in enumerate(all_mse_values):
            for h, mse in mse_list:
                print(f'Rank {rank}, MSE for h={h}: {mse}')

        endTime = time.time()
        print(f'Proteklo vrijeme: {endTime - startTime}')
        sys.stdout.flush()

def main():
    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    size = comm.Get_size()

    increments = [1, 0.1, 0.01, 0.001, 0.0001, 0.00001]

    local_increments = [increments[i] for i in range(rank, len(increments), size)]

    parallel_solve_with_mpi(local_increments, rank, size, comm)

if __name__ == "__main__":
    main()
