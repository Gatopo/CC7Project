#include "syscall.h"
#include "stdio.h"
#include "stdlib.h"

#define BUFSIZE 1024

char buf[BUFSIZE];

int main(int argc, char** argv)
{
    char tkn[10];

    strcpy( tkn, "Mario" );

	//creat("haloWOrld.txt");
	int fd = open("haloWOrld.txt");
	if (fd > -1)
	{
		read(fd, buf, 4);
		printf("%s DEBUGGING BUFFER",buf);
		write(fd, tkn, 4);
		close(fd);

	}
	return 0;
}
