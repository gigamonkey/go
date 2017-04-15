#!/usr/bin/env python3

from sys import argv
from math import log
from random import randrange

size   = int(argv[1]) if len(argv) > 1 else 19
m      = size - 1

def fn(d, m):
    return pow(2 * m - d, 2)

def norm(score, m):
    if score == 0:
        return 0
    else:
        return round(1000 * log(score, m))

def d(i, j, stone_i, stone_j):
    return abs(stone_i - i) + abs(stone_j - j)

#board = [ [0] * size for _ in range(size) ]

#board = [ [ norm((fn(i, m) + fn(m - i, m) + fn(j, m) + fn(m - j, m)), m) for j in range(size) ] for i in range(size) ]
#board = [ [ fn(d(i, j, stone_i, stone_j), m) for j in range(size) ] for i in range(size) ]

board = [ [0] * size for _ in range(size) ]

def update_gradients(board, stone_i, stone_j, m):
    print(stone_i, stone_j)
    for i in range(size):
        for j in range(size):
            board[i][j] += norm(fn(d(i, j, stone_i, stone_j), m), m)

update_gradients(board, 2, 7, m)

#update_gradients(board, randrange(size), randrange(size), m)
#update_gradients(board, randrange(size), randrange(size), m)
#update_gradients(board, randrange(size), randrange(size), m)

#for i in range(size):
#    for j in range(size):
#        update_gradients(board, i, j, m)

def show(board):
    num_format = '{:d}'

    max_width = max(max(len(num_format.format(n)) for n in row) for row in board)

    #cell_format = '{:^' + str(max_width + 2) + '.2f}'
    cell_format = '{:^' + str(max_width + 2) + 'd}'

    print()
    for row in board:
        for col in row:
            print(cell_format.format(col), end='')
        #print('')
        print('')

#print((stone_i, stone_j))

show(board)
