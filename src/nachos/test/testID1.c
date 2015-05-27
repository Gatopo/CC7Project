#include "syscall.h"
#include "stdio.h"
#include "stdlib.h"

#define BUFSIZE 1024

char buf[BUFSIZE];

int main(int argc, char** argv)
{
    char tkn[10];
    strcpy( tkn, "Mario" );
    int fileDescriptor = creat("File 2 testID0.txt");
    close(fileDescriptor);
    write(fileDescriptor, tkn, 4);
    return 0;
}

/**
    LOG OF TEST
    1. Clos is Ok, pending to implement unlink.

**/
