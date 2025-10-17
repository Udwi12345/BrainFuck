// BF.cpp
// Usage: g++ -O2 BF.cpp -o bf  && ./bf <file_path>.bf
// A simple Brainfuck interpreter in C++17

#include <bits/stdc++.h>
using namespace std;

int main(int argc, char** argv){
    if(argc < 2){ cerr << "Usage: " << argv[0] << " file.bf\n"; return 1; }
    ifstream in(argv[1]);
    if(!in){ cerr << "Cannot open " << argv[1] << "\n"; return 1; }

    string code, line;
    while(getline(in,line)) for(char c: line) if(string("+-<>[].,").find(c)!=string::npos) code.push_back(c);

    // build loop map
    unordered_map<int,int> jump;
    vector<int> stack;
    for(int i=0;i<(int)code.size();++i){
        if(code[i]=='[') stack.push_back(i);
        else if(code[i]==']'){
            if(stack.empty()){ cerr<<"Unmatched ] at "<<i<<"\n"; return 2; }
            int j = stack.back(); stack.pop_back();
            jump[j] = i;
            jump[i] = j;
        }
    }
    if(!stack.empty()){ cerr<<"Unmatched [ at "<<stack.back()<<"\n"; return 2; }

    vector<unsigned char> cells(30000,0);
    int ptr = 0;
    for(int pc=0; pc<(int)code.size(); ++pc){
        char cmd = code[pc];
        switch(cmd){
            case '>': ++ptr; if(ptr >= (int)cells.size()) { cells.resize(ptr+1); } break;
            case '<': --ptr; if(ptr < 0){ cerr<<"Pointer moved left of 0\n"; return 3; } break;
            case '+': cells[ptr] = (unsigned char)((cells[ptr] + 1) & 0xFF); break;
            case '-': cells[ptr] = (unsigned char)((cells[ptr] - 1) & 0xFF); break;
            case '.': cout << (char)cells[ptr]; cout.flush(); break;
            case ',': {
                int ch = cin.get();
                cells[ptr] = (unsigned char)( ch == EOF ? 0 : ch );
                break;
            }
            case '[':
                if(cells[ptr] == 0) pc = jump[pc];
                break;
            case ']':
                if(cells[ptr] != 0) pc = jump[pc];
                break;
        }
    }
    return 0;
}
