package Compilers;
// BF.java

// Usage: javac BF.java && java BF <file_path>.bf

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class BF {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) { // to handle case for correnct commmand
            System.err.println("Usage: java BF file.bf");
            return;
        }
        String src = new String(Files.readAllBytes(Paths.get(args[0]))); // Reading source code
        StringBuilder codeB = new StringBuilder();
        for (char c : src.toCharArray()) { // filter valid commands (removing comments)
            if ("+-<>[].,".indexOf(c) >= 0) { // returns -1 if char is not found
                codeB.append(c);
            }
        }
        String code = codeB.toString();

        // map loops
        Map<Integer, Integer> jump = new HashMap<>();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < code.length(); ++i) {
            char c = code.charAt(i);
            if (c == '[') {
                stack.push(i);
            } else if (c == ']') {
                // Example Case: ++]]>+.
                if (stack.isEmpty()) { // handling when Loop is not open but closed
                    System.err.println("Unmatched ] at " + i);
                    return;
                }
                int j = stack.pop();
                jump.put(j, i);
                jump.put(i, j);
            }
        }
        if (!stack.isEmpty()) { // handling when Loop is not closed
            // Example Case: ++[>+.
            System.err.println("Unmatched [ at " + stack.peek());
            return;
        }

        // Memory Initualization and Execution
        int[] cells = new int[30000]; // Memory cells
        int ptr = 0; // Data pointer
        for (int pc = 0; pc < code.length(); ++pc) {
            char cmd = code.charAt(pc);
            switch (cmd) {
                case '>':
                    ptr++;
                    if (ptr >= cells.length)
                        cells = Arrays.copyOf(cells, cells.length * 2);
                    break;
                case '<':
                    ptr--;
                    if (ptr < 0) {
                        System.err.println("Pointer < 0");
                        return;
                    }
                    break;
                case '+':
                    cells[ptr] = (cells[ptr] + 1) & 0xFF;
                    break;
                case '-':
                    cells[ptr] = (cells[ptr] - 1) & 0xFF;
                    break;
                case '.':
                    System.out.print((char) cells[ptr]);
                    System.out.flush();
                    break;
                case ',':
                    int r = System.in.read();
                    cells[ptr] = (r == -1 ? 0 : r);
                    break;
                case '[':
                    if (cells[ptr] == 0)
                        pc = jump.get(pc);
                    break;
                case ']':
                    if (cells[ptr] != 0)
                        pc = jump.get(pc);
                    break;
            }
        }
    }
}
