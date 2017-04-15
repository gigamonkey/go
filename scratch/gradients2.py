#!/usr/bin/env python3

from sys import argv
from math import log
from random import randrange

size   = int(argv[1]) if len(argv) > 1 else 19
m      = size - 1

def add_point(board, stone_i, stone_j, m):
    print(stone_i, stone_j)
    for i in range(size):
        for j in range(size):
            board[i][j] += norm(fn(d(i, j, stone_i, stone_j), m), m)
            #board[i][j] += fn(d(i, j, stone_i, stone_j), m)


def fn(d, m):
    return pow(2 * m - d, 2)

def norm(score, m):
    if score == 0:
        return 0
    else:
        return round(1000 * log(score, m))

def d(i, j, stone_i, stone_j):
    return abs(stone_i - i) + abs(stone_j - j)


if __name__ == '__main__':

    board = [ [0] * size for _ in range(size) ]

    #add_point(board, 2, 7, m)
    #for i, j in ((i, j) for i in range(size) for j in range(size)):
    #    add_point(board, i, j, m)
    #for i in range(size):
    #add_point(board, 0, i, m)
        #add_point(board, i, 0, m)
        #add_point(board, size - 1, i, m)
        #add_point(board, i, size - 1, m)
    add_point(board, 0, 0, m)
    add_point(board, 0, size - 1, m)
    add_point(board, size - 1, 0, m)
    add_point(board, size - 1, size - 1, m)

    num_format = '{:d}'

    max_width = max(max(len(num_format.format(n)) for n in row) for row in board)

    cell_format = '{:^' + str(max_width + 2) + 'd}'

    print()
    for row in board:
        for col in row:
            print(cell_format.format(col), end='')
        print('')
