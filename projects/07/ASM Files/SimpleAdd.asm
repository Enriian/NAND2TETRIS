// C_PUSH constant 7
@7
D=A
@SP
A=M
M=D
@SP
M=M+1
// C_PUSH constant 8
@8
D=A
@SP
A=M
M=D
@SP
M=M+1
// add
@SP
M=M-1
A=M
D=M
@SP
M=M-1
A=M
D=D+M
@SP
A=M
M=D
@SP
M=M+1
