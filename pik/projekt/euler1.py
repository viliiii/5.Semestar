import numpy as np
from scipy.integrate import solve_ivp
import matplotlib.pyplot as plt
import time

startTime = time.time()

# Funkcija za izračun srednje-kvadratne greške
def mean_squared_error(true_values, approx_values):
    # Provjeri jesu li nizovi istih veličina
    if len(true_values) != len(approx_values):
        # Ako veličine razlikuju više od 2, ispiši poruku
        if abs(len(true_values) - len(approx_values)) > 2:
            print("Problem: Nizovi imaju značajnu razliku u veličini.")
        else:
            # Ako je razlika manja ili jednaka 2, prilagodi nizove
            min_len = min(len(true_values), len(approx_values))
            true_values = true_values[:min_len]
            approx_values = approx_values[:min_len]

    # Izračunaj srednje-kvadratnu grešku
    return np.mean((true_values - approx_values) ** 2)



# Analitičko rješenje diferencijalne jednadžbe (za usporedbu)
def analytical_solution(t):
    return np.exp(t)


# Definirajte diferencijalnu jednadžbu
def dif_eq(t, y):
    return y


# Funkcija za rješavanje diferencijalne jednadžbe pomoću Eulerove metode
def euler_method(func, t_span, y0, h):
    t_values = np.arange(t_span[0], t_span[1] + 2*h, h)
    y_values = np.array([y0])  # Inicijalizacija niza

    for t in t_values[:-1]:
        y0 = y0 + h * func(t, y0)
        y_values = np.append(y_values, y0)  # Dodajte pojedinačni element u niz

    return t_values, y_values


# Parametri problema
t_span = (0, 5)  # Vremenski raspon (zapravo samo x-os ali se kao diferencijalne koriste za pratit promjene u vremenu pa je ovo fensi)
y0 = np.array([1.0])  # Početni uvjet
increments = [0.01, 0.001, 0.0001]  # Testirani inkrementi

# Iteracija kroz različite inkremente
for h in increments:
    # Rješavanje diferencijalne jednadžbe pomoću solve_ivp (Scipy)
    sol_scipy = solve_ivp(dif_eq, t_span, y0, method='RK45', max_step=h)

    # Rješavanje diferencijalne jednadžbe pomoću Eulerove metode
    t_euler, y_euler = euler_method(dif_eq, t_span, y0, h)

    #print(sol_scipy.y[0])
    #print("-----------------------------------")
    #print(y_euler)

    mse = mean_squared_error(sol_scipy.y[0], y_euler)

    print(f'MSE for h={h}: {mse}')



    #Plot rezultata
    # plt.figure(figsize=(10, 6))
    # plt.plot(sol_scipy.t, sol_scipy.y[0], label=f'solve_ivp (h={h})')
    # plt.plot(t_euler, y_euler, label=f'Eulerova metoda (h={h})', linestyle='dashed', marker='o')
    # plt.title('Aproksimacija rješenja diferencijalne jednadžbe')
    # plt.xlabel('Vrijeme')
    # plt.ylabel('Rješenje')
    # plt.legend()
    # plt.show()

endTime = time.time()

print(f'Proteklo vrijeme: {endTime - startTime}')
