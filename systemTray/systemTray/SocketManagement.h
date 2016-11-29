
#include<io.h>
#include<stdio.h>
#include<winsock2.h>
#include "Robot.h"
#include "utility.h"

#define DEFAULT_BUFLEN 512
#define TCP_PORT 1239

int recvbuflen = DEFAULT_BUFLEN;
char recvbuf[DEFAULT_BUFLEN];

#pragma execution_character_set("utf-8")
#pragma comment(lib,"ws2_32.lib") //Winsock Library

int socketManagement()
{
	WSADATA wsa;
	SOCKET s, new_socket;
	struct sockaddr_in server, client;
	int c;
	char *message;

	printf("\nInitialising Winsock...");
	if (WSAStartup(MAKEWORD(2, 2), &wsa) != 0)
	{
		printf("Failed. Error Code : %d", WSAGetLastError());
		return 1;
	}

	printf("Initialised.\n");

	//Create a socket
	if ((s = socket(AF_INET, SOCK_STREAM, 0)) == INVALID_SOCKET)
	{
		printf("Could not create socket : %d", WSAGetLastError());
	}

	printf("Socket created.\n");

	//Prepare the sockaddr_in structure
	server.sin_family = AF_INET;
	server.sin_addr.s_addr = INADDR_ANY;
	server.sin_port = htons(TCP_PORT);

	//Bind
	if (bind(s, (struct sockaddr *)&server, sizeof(server)) == SOCKET_ERROR)
	{
		printf("Bind failed with error code : %d", WSAGetLastError());
		exit(EXIT_FAILURE);
	}

	puts("Bind done");

	//Listen to incoming connections
	listen(s, 3);

	//Accept and incoming connection
	puts("Waiting for incoming connections...");

	c = sizeof(struct sockaddr_in);

	Robot robot = new Robot(true);

	while ((new_socket = accept(s, (struct sockaddr *)&client, &c)) != INVALID_SOCKET)
	{
		puts("Connection accepted");

		int len;

		recv(new_socket, recvbuf, recvbuflen, NULL);


		puts(recvbuf);


		vector<string> split = splitString(recvbuf, ' ');

		int len = split.size();


		for (int i = 0; i < len; i++)
			cout << " " << split[i];







		//
		//robot.mouseMoveTo(0, 0);


		//Reply to the client
		message = "Hello Client , I have received your connection. But I have to go now, bye\n";


		send(new_socket, message, strlen(message), 0);
	}

	if (new_socket == INVALID_SOCKET)
	{
		printf("accept failed with error code : %d", WSAGetLastError());
		return 1;
	}

	closesocket(s);
	WSACleanup();

	return 0;
}