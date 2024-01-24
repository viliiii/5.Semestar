import numpy as np
from scipy import optimize


def objective_function(x):
    return x ** 2 + 4 * x + 4


result = optimize.minimize(objective_function, x0=0)
print("Minimizirana vrijednost x:", result.x)
