// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/4/Fill.asm

// Runs an infinite loop that listens to the keyboard input. 
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel. When no key is pressed, 
// the screen should be cleared.

(input_loop)
@SCREEN
D=A
@8191
D=D+A
@main_loop_pressed
M=D
@main_loop_not_pressed
M=D
@KBD
D=M

@if_not_pressed
D;JEQ
@if_pressed
D;JGT
@loop
0;JMP

// Key pressed
(if_pressed)
@SCREEN
M=-1
D=A

(main_loop_pressed)
D=D+1
A=D
M=-1
@main_loop_pressed
M-D;JNE
@input_loop
0;JMP

// Key not pressed
(if_not_pressed)
@SCREEN
M=0
D=A

(main_loop_not_pressed)
D=D+1
A=D
M=0
@main_loop_not_pressed
M-D;JNE
@input_loop
0;JMP