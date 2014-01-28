#include"dsm.h" 
#include <stdio.h> 
#include <stdlib.h> 
#include <sys/socket.h> 
#include <netinet/in.h> 
#include<sys/mman.h> 
#include<pthread.h> 
#include<signal.h> 
#include<string.h> 

#define PAGE_SIZE 4096 

int * page_table;

void * page_address; 

/* Initialize variables for Socket: Master as Server*/ 
int socket_desc; 
struct sockaddr_in address; 
int addrlen; 
int other_socket; 
int isMaster; 

int globalFlag = 1;
int Numpagestoalloc;

/* Initialize variables for Socket: Master as Server*/ 
int socket_desc_slave; 
struct sockaddr_in address_slave; 
int addrlen_slave; 
int other_socket_slave; 

void *listenerFunction(void *arg); 
void segfault_sigaction(int signal, siginfo_t *si, void *arg); 

pthread_t receiver; 
pthread_mutex_t mutex1 = PTHREAD_MUTEX_INITIALIZER;

void initializeDSM(int ismaster, char * masterip, int mport, char *otherip, int oport, int numpagestoalloc) { 

	page_table = malloc(numpagestoalloc*sizeof(int));
	int i;
	if(ismaster)
		for(i=0;i<numpagestoalloc;i++)
			*(page_table + i) = 1;
	else
		for(i=0;i<numpagestoalloc;i++)
			*(page_table + i) = 0;			

	Numpagestoalloc = numpagestoalloc;
	isMaster = ismaster; 
	signal(SIGPIPE, SIG_IGN);

	/*Seg Fault initializations variables*/ 
	struct sigaction sa; 
	memset(&sa, 0, sizeof(sigaction)); 
	sigemptyset(&sa.sa_mask); 
	sa.sa_sigaction = segfault_sigaction; 
	sa.sa_flags = SA_SIGINFO; 
	sigaction(SIGSEGV, &sa, NULL);

	page_address = mmap(0x100000, PAGE_SIZE * numpagestoalloc, PROT_READ | PROT_WRITE, MAP_ANON |  MAP_SHARED, 0, 0);

	if(!isMaster)
		mprotect(page_address, PAGE_SIZE * numpagestoalloc, PROT_NONE);
	else
		mprotect(page_address, PAGE_SIZE * numpagestoalloc, PROT_WRITE | PROT_READ );

	void *message = ""; 

	socket_desc = socket(AF_INET,SOCK_STREAM, 0); 
	if(socket_desc == 0) 
		printf(""); 

	address.sin_family = AF_INET; 
	address.sin_addr.s_addr = INADDR_ANY; 
	address.sin_port = htons(mport); 

	socket_desc_slave = socket(AF_INET,SOCK_STREAM,0); 
	if(socket_desc_slave == 0)
		printf(""); 

	address_slave.sin_family = AF_INET; 
	address_slave.sin_addr.s_addr = INADDR_ANY; 
	address_slave.sin_port = htons(oport);

	int rrec = pthread_create(&receiver, NULL, listenerFunction, (void *) message); 
} 

void * getsharedregion() { 
	return page_address; 
}

void * listenerFunction(void *arg) {
	char buffer[PAGE_SIZE]; 

	if(isMaster) { 
		if (bind(socket_desc,(struct sockaddr *)&address,sizeof(address))<0) 
			printf("");  
		if (listen(socket_desc,3)<0) 
			printf("");
 
		addrlen = sizeof(address); 
		if ((other_socket=accept(socket_desc,(struct sockaddr *)&address,&addrlen))<0) 
			printf(""); 
		else  
			printf(""); 

		while(1) {      
			/*receive page number to other*/ 
			int page_num; 
			int ret;
			int temp;
			ret = recv(other_socket, &page_num, sizeof(int), 0);
			if(ret == 0) {
				mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
				pthread_cancel(receiver);
			}

			while(*(page_table + page_num) == 0);

			pthread_mutex_lock(&mutex1);
			mprotect(page_address + page_num * PAGE_SIZE, PAGE_SIZE, PROT_READ);
			*(page_table + page_num) = 0;

			memcpy(buffer, page_address + PAGE_SIZE * page_num, PAGE_SIZE); 
			memcpy(&temp, buffer, sizeof(int));
			mprotect(page_address + PAGE_SIZE * page_num, PAGE_SIZE, PROT_NONE);
			ret = send(other_socket, buffer, PAGE_SIZE, 0);

			if(ret == -1) {
				mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
				pthread_cancel(receiver);
			}
			pthread_mutex_unlock(&mutex1);
		} 
	}
	else {

		if (bind(socket_desc_slave,(struct sockaddr *)&address_slave,sizeof(address_slave))<0) 
			printf("Failed to bind"); 
		if (listen(socket_desc_slave,3)<0) 
			printf(""); 
		addrlen_slave = sizeof(address_slave); 
		if ((other_socket_slave=accept(socket_desc_slave,(struct sockaddr *)&address_slave,&addrlen_slave))<0) 
			printf(""); 
		else  
			printf(""); 

		while(1) {
			int page_num;
			int ret;
			int temp;
			ret = recv(other_socket_slave, &page_num, sizeof(int), 0);
			if(ret == 0)
			{
				mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
				pthread_cancel(receiver);
			}

			while(*(page_table + page_num) == 0);

			pthread_mutex_lock(&mutex1);
			mprotect(page_address + page_num * PAGE_SIZE, PAGE_SIZE,  PROT_READ);
			*(page_table + page_num) = 0;
			memcpy(buffer, page_address + PAGE_SIZE * page_num, PAGE_SIZE);
			memcpy(&temp, buffer, sizeof(int));
			mprotect(page_address + PAGE_SIZE * page_num, PAGE_SIZE, PROT_NONE);

			ret = send(other_socket_slave, buffer, PAGE_SIZE, 0);
			if(ret == -1) {
				mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
				pthread_cancel(receiver); 
			}
			pthread_mutex_unlock(&mutex1);
		}
	} 
}

/*Seg Fault Handler */ 
void segfault_sigaction(int signal, siginfo_t *si, void *arg) 
{
	char buffer[PAGE_SIZE]; 
	int diff_addr = si->si_addr - page_address; 
	int page_num = diff_addr/PAGE_SIZE; 
	/*send page number to other*/ 

	if(!isMaster)
	{
		addrlen = sizeof(address); 
		if(globalFlag == 1) {
			globalFlag = 0;

			int result = connect(socket_desc, (struct sockaddr *)&address, addrlen); 
			if(result == -1) 
			{ 
				perror("Error has occurred while connecting"); 
				exit(0); 
			}     
			else 
				printf(""); 
		}

		int ret;
		ret = send(socket_desc, &page_num, sizeof(int), 0); 
		if(ret == -1) {
			mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
			return;
		}

		if (ret != sizeof(int)) { 
			perror("Error ---- Can not send message\n"); 
			exit(0); 
		} 
		ret = recv(socket_desc, buffer, PAGE_SIZE, 0);
		if(ret == 0) {
			mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
			return;
		}
		mprotect(page_address + page_num * PAGE_SIZE, PAGE_SIZE, PROT_WRITE | PROT_READ);
		memcpy(page_address + PAGE_SIZE * page_num, buffer, PAGE_SIZE);
		*(page_table + page_num) = 1;
	}
	else {
		addrlen_slave = sizeof(address_slave); 
		if(globalFlag == 1) {
			globalFlag = 0;
			int result = connect(socket_desc_slave, (struct sockaddr *)&address_slave, addrlen_slave); 
			if(result == -1) { 
				perror("Error has occurred"); 
				exit(0); 
			}     
			else 
				printf(""); 
		}
		int ret;
		ret = send(socket_desc_slave, &page_num, sizeof(int), 0);
		if(ret == -1){
			mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
			return;
		}
		ret = recv(socket_desc_slave, buffer, PAGE_SIZE, 0);
		if(ret == 0) {
			mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_WRITE | PROT_READ );
			return;
		}
		mprotect(page_address + page_num * PAGE_SIZE, PAGE_SIZE, PROT_WRITE | PROT_READ);
		memcpy(page_address + PAGE_SIZE * page_num, buffer, PAGE_SIZE);
		*(page_table + page_num) = 1;
	}    
}

void destroyDSM() {
	mprotect(page_address, PAGE_SIZE * Numpagestoalloc, PROT_NONE );

	if(isMaster)
		close(socket_desc);
	else
		close(socket_desc_slave);
}
