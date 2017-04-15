#!/usr/bin/env python3

from sys import argv
from math import log

size   = int(argv[1]) if len(argv) > 1 else 19
m      = size - 1
m2     = m * m
size_2 = size / 2

def fn(d, m):
    return pow(m - d, 2)

if False:
    board = [ [0] * size for _ in range(size) ]

    for i in range(size):
        for j in range(size):
            #board[i][j] = round((pow((size - 1) - i, 2) + pow(i, 2) + pow((size - 1) - j, 2) + pow(j, 2)) / size)
            #board[i][j] = round((2 * ((m * m) + (i * i) + (j * j) - (i * m) - (j * m))) / size)
            #board[i][j] = round((pow(m - i, 2) + pow(i, 2) + pow(m - j, 2) + pow(j, 2)) / size)
            #board[i][j] = round((m2 + i * (i - m) + j * (j - m)) / size_2)
            board[i][j] = m2 + i * (i - m) + j * (j - m)
else:
    #board = [ [ round(100 * (log((m2 + i * (i - m) + j * (j - m)), m))) for j in range(size) ] for i in range(size) ]
    #board = [ [ round(1000 * log((fn(i, m) + fn(m - i, m) + fn(j, m) + fn(m - j, m)), m)) for j in range(size) ] for i in range(size) ]
    board = [ [ round(1000 * log((fn(i, m) + fn(m - i, m) + fn(j, m) + fn(m - j, m)), m)) for j in range(size) ] for i in range(size) ]

def show(board):
    num_format = '{:d}'

    max_width = max(max(len(num_format.format(n)) for n in row) for row in board)

    #cell_format = '{:^' + str(max_width + 2) + '.2f}'
    cell_format = '{:^' + str(max_width + 2) + 'd}'

    print()
    for row in board:
        for col in row:
            print(cell_format.format(col), end='')
        print('')
        print('')

show(board)
