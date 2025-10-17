import sys

def brainfuck(code):
    cells, ptr, i = [0]*30000, 0, 0
    loop = {}
    stack = []

    # Build loop map
    for pos, cmd in enumerate(code):
        if cmd == '[':
            stack.append(pos)
        elif cmd == ']':
            start = stack.pop()
            loop[start], loop[pos] = pos, start

    # Run code
    while i < len(code):
        cmd = code[i]
        if cmd == '>': ptr += 1
        elif cmd == '<': ptr -= 1
        elif cmd == '+': cells[ptr] = (cells[ptr] + 1) % 256
        elif cmd == '-': cells[ptr] = (cells[ptr] - 1) % 256
        elif cmd == '.': print(chr(cells[ptr]), end='')
        elif cmd == ',': cells[ptr] = ord(sys.stdin.read(1))
        elif cmd == '[' and cells[ptr] == 0: i = loop[i]
        elif cmd == ']' and cells[ptr] != 0: i = loop[i]
        i += 1

if __name__ == "__main__":
    with open(sys.argv[1]) as f:
        brainfuck(''.join(filter(lambda c: c in '+-<>[].,', f.read())))


# command to run bf file:
#     python BF.py <path_to_bf_file>
