// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)
// The algorithm is based on repetitive addition.

// Setting inputs to new registers
@R0
D=M
@n
M=0

@R1
D=M
@i
M=D


// Loop by subtracting from R1 set in i, jumping when it is below 0
(loop)
@set_final_number
D;JEQ
@R0
D=M
@n
M=D+M
@i
M=M-1
D=M
@loop
0;JMP

(set_final_number)
@n
D=M
@R2
M=D

(exit)
@exit
0;JMP

