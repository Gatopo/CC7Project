#include "syscall.h"
#include "stdio.h"
#include "stdlib.h"

#define BUFSIZE 1024

char buf[BUFSIZE];

int main(int argc, char** argv)
{
    creat("File 2 testID0.txt");
    return 0;
}

/**
    LOG OF TEST
    1. When creating files, do not enter whitespace files such as "File 1.txt". For some reason the file
    cannot be created. Fixed Adding regexp condition.
    2. When writing on the file after created, and if syscall created was called again, the file was
    created from 0 and was not performing an opening of the already created file. A solution is to create
    a map or strcture to have a memory if created files and use the flag <create> in the method open of the
    file system.
**/
