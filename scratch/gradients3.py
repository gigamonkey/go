#!/usr/bin/env python3

from sys import argv
from math import log
from random import randrange

size   = int(argv[1]) if len(argv) > 1 else 19
m      = size - 1


def add_point(board, stone_i, stone_j):
    #print(stone_i, stone_j)
    for i in range(size):
        for j in range(size):
            board[i][j] += score((i,j), (stone_i, stone_j))



def score(p1, p2):
    x1, y1 = p1
    x2, y2 = p2
    d      = abs(x1 - x2) + abs(y1 - y2)
    big    = 100 * (m * 2) * (m * 2)
    big = pow(2, 16)
    big = (2 * m * 2 * m) + 1

    md = m * 2

    # This weird formula gives us a coefficent such that the farthest
    # away point and the second farthest away point will differ by
    # one.
    big = (md * (md * (md * (md - 2) + 3) - 2) + 2) / (2 * md - 1)
    return round(big / (d * d + 1))


if __name__ == '__main__':

    board = [ [0] * size for _ in range(size) ]

    #add_point(board, 2, 7)

    #x, y = (randrange(size) for i in range(2))

    top_left = False
    all_points = False
    corners = True

    if top_left:
        x, y = 0, 0
        print(x, y)
        add_point(board, x, y)
    elif all_points:
        for i, j in ((i, j) for i in range(size) for j in range(size)):
            #if not (i == j and i == m / 2):
            add_point(board, i, j)
    elif corners:
        add_point(board, 0, 0)
        add_point(board, 0, size - 1)
        add_point(board, size - 1, 0)
        add_point(board, size - 1, size - 1)

    #for i in range(size):
    #add_point(board, 0, i, m)
        #add_point(board, i, 0, m)
        #add_point(board, size - 1, i, m)
        #add_point(board, i, size - 1, m)

    num_format = '{:d}'

    max_width = max(max(len(num_format.format(n)) for n in row) for row in board)

    cell_format = '{:^' + str(max_width + 2) + 'd}'

    print()
    for row in board:
        for col in row:
            print(cell_format.format(col), end='')
        print('')
