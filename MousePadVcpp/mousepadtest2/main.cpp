
#include<io.h>
#include<stdio.h>
#include<winsock2.h>
#include<string>
#include<windows.h>
#include <exception>

#include <iostream>
#include "Robot.h"
#include "utility.h"
#include <string>

#define DEFAULT_BUFLEN 50
#define TCP_PORT 1239

int recvbuflen = DEFAULT_BUFLEN;
char recvbuf[DEFAULT_BUFLEN];

#pragma execution_character_set("utf-8")
#pragma comment(lib,"ws2_32.lib") //Winsock Library




using namespace std;



int socketManagement();
void performAction(vector<string>, SOCKET,Robot & robot);



int main(){



	// fill it out for keyboard key presses...

//	Robot robot = new Robot(false);

//	char* text = "h a l a r p u t";

	socketManagement();
	
	//robot.mouseMoveTo(0,0);
	//robot.mouseLeftCLick();


//	robot.keyboard(text);



	return 0;



}



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
	int len;
	
	int firstTime = 0;
	bool clickAndHold = false;

	while ((new_socket = accept(s, (struct sockaddr *)&client, &c)) != INVALID_SOCKET)
	{
		puts("Connection accepted");

		int len;

		
		memset(recvbuf, 0, sizeof recvbuf);


		recv(new_socket, recvbuf, 50, NULL);

		
		puts(recvbuf);



		vector<string> splitV = splitString(recvbuf, ' ');

		len = splitV.size();


		cout << "\n----------" << endl;

		for (int i = 0; i < len; i++)
			cout <<splitV[i]<<endl ;

		
		//
		//robot.mouseMoveTo(0, 0);


		//Reply to the client
		message = "mehedee\n";


		if (splitV.size() > 0){
			cout << splitV[0] << "=====" << endl;



			//int Flag = -1;
			//Flag = getIntFromString(splitV[0]);


			//performAction(splitV,new_socket, robot);


		if (splitV[0] == COMMAND_MOUSE_MOVE) {
			
			int x = stof(splitV[1]);
			int y = stof(splitV[2]);
			robot.mouseMoveTo(x, y);

			
		}else

		if (splitV[0] == FLAG_SEARCH){
			char hostname[100];
			gethostname(hostname, 100);

			cout << "message added  " << hostname << endl;
			int hostnameLen = strlen(hostname);



			hostname[hostnameLen] = '\n';
			hostname[hostnameLen + 1] = '\0';



			cout << "message added 2 |" << hostname[hostnameLen] << endl;



			send(new_socket, hostname, hostnameLen + 2, 0);

			}
		else if (splitV[0] == COMMAND_MOUSE_MOVE) {
				int x = stof(splitV[1]);
				int y = stof(splitV[2]);
				robot.mouseMoveTo(x, y);
	
			}
	
		else if (splitV[0] == COMMAND_MOUSE_LEFT_CLICK) {
			robot.mouseLeftCLick(false);
		}

		else if (splitV[0] == COMMAND_MOUSE_RIGHT_CLICK) {
			robot.mouseRightClick();
		}
		
		else if (splitV[0] == COMMAND_MOUSE_PAD_SHORT_CLICK) {
			robot.mouseLeftCLick(false);
		}
		else if (splitV[0] == COMMAND_MOUSE_LEFT_CLICKHOLD){
			robot.mouseLeftCLick(true);
			clickAndHold = true;

		}

		else if (splitV[0] == COMMAND_MOUSE_PAD_UP){
			if (clickAndHold == true){
				clickAndHold = false;
				robot.mouseLeftCLickUp();

			}

		}
		else if (splitV[0] == COMMAND_CLOSE_WINDOW) {
			robot.AltF4();
		}

		else if (splitV[0] == COMMAND_TAB_WINDOW) {
			
		}

		else if (splitV[0] == COMMAND_ENTER) {
			robot.pressEnter();

		}

		else if (splitV[0] == COMMAND_SCROLL_VERTICAL_DOWN) {
			robot.mouseScroll(1);
		}

		else if (splitV[0] == COMMAND_SCROLL_VERTICAL_UP) {
			robot.mouseScroll(2);
		}

		else if (splitV[0] == COMMAND_ESC) {
			robot.pressESC();
		}

	


		}
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


void performAction(vector<string>  splitV,SOCKET new_socket,Robot &robot){



	if (splitV[0] == "18"){


		char hostname[100];
		gethostname(hostname, 100);

		cout << "message added  " << hostname << endl;
		int hostnameLen = strlen(hostname);



		hostname[hostnameLen] = '\n';
		hostname[hostnameLen + 1] = '\0';



		cout << "message added 2 |" << hostname[hostnameLen] << endl;



		send(new_socket, hostname, hostnameLen + 2, 0);
		
	}
	else if (splitV[0] == "3") {

		int x = getFloatFromString(splitV[1]);
		int y = getFloatFromString(splitV[2]);

		robot.mouseMoveTo(x,y);

		//			send(new_socket, message, strlen(message), 0);

	}

}
