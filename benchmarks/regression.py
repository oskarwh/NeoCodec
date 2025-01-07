import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from scipy.stats import linregress
import sys


def create_regression_plots(file_path):
    df = pd.read_csv(file_path, delimiter=';', header=0)

    # Convert lists to numpy arrays for easier manipulation and calculation
    time = np.array(df['time'])
    size = np.array(df['size'])

    # Perform linear regression to find the relation between time and size
    slope, intercept, r_value, p_value, std_err = linregress(size, time)

    # Print the results of the linear regression analysis
    print(f"Slope: {slope}")
    print(f"Intercept: {intercept}")
    print(f"R-squared: {r_value**2}")

    # Plot the data points and the regression line for visualization purposes.
    plt.scatter(size, time, label='Data points')
    plt.plot(size, intercept + slope * size, 'r', label='Fitted line')
    plt.suptitle('Regression Plot of Time by Size')
    plt.title(f"R-squared: {r_value**2}")
    plt.xlabel('Size (Bytes)')
    plt.ylabel('Time (Seconds)')
    plt.legend()
    
    plt.savefig('regression_plot.png')

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python script.py <file_path>")
    else:
        file_path = sys.argv[1]
        create_regression_plots(file_path)
