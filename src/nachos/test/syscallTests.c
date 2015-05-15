#include "syscall.h"
#include "stdio.h"
#include "stdlib.h"

#define BUFSIZE 1024

char buf[BUFSIZE];

int main(int argc, char** argv)
{
  int fd, amount;

int unlinkFile = 0;
	creat("helloTest.txt");
	int fd = open("helloTest.txt");
	if (fd > -1)
	{
		read(fd, buf, 2067);
		write(1, buf, 2067);
	}
	return 0;
}
