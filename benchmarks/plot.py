import pandas as pd
import matplotlib.pyplot as plt
import sys

def create_box_plots(file_path):
    # Read the data into a pandas DataFrame, specifying the delimiter as ';'
    df = pd.read_csv(file_path, delimiter=';', header=0)

    # Create a boxplot for the 'time' column grouped by 'size'
    df.boxplot(column='time', by='size', grid=False)

    # Set plot title and labels for clarity 
    plt.title('Boxplot of Time by Size')
    plt.suptitle('')
    plt.xlabel('Size (Bytes)')
    plt.ylabel('Time (Seconds)')

    # Show the plot 
    plt.savefig('boxplot.png')

if __name__ == '__main__':
    if len(sys.argv) != 2:
        print("Usage: python script.py <file_path>")
    else:
        file_path = sys.argv[1]
        create_box_plots(file_path)
