
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
#include <thread>
#include <mutex>
#include <chrono>


#define DEFAULT_BUFLEN 17
#define TCP_PORT 1239

int recvbuflen = DEFAULT_BUFLEN;
char recvbuf[DEFAULT_BUFLEN];

#pragma execution_character_set("utf-8")
#pragma comment(lib,"ws2_32.lib") //Winsock Library
//#pragma comment(linker, "/SUBSYSTEM:windows /ENTRY:mainCRTStartup")


using namespace std;



std::mutex g_lock;

string __ipLIst;
string myPcName;


int socketManagement();
void performAction(vector<string>, SOCKET, Robot & robot);
void onePadThread(SOCKET* new_socket);
int mouseUpCalled = 1;

int main3(){



	// fill it out for keyboard key presses...

	//	Robot robot = new Robot(false);

	//	char* text = "h a l a r p u t";

	mouseUpCalled = 1;

	socketManagement();

	//robot.mouseMoveTo(0,0);
	//robot.mouseLeftCLick();


	//	robot.keyboard(text);
	

	return 0;



}

void printIPs(){

	myPcName= "" ,__ipLIst = "";

	WORD wVersionRequested;
	WSADATA wsaData;
	char name[255];
	PHOSTENT hostinfo;
	wVersionRequested = MAKEWORD(1, 1);
	char *ip;

	

	if (WSAStartup(wVersionRequested, &wsaData) == 0)
	if (gethostname(name, sizeof(name)) == 0)
	{

		myPcName = string(name);
		//		printf("Host name: %s\n", name);

		if ((hostinfo = gethostbyname(name)) != NULL)
		{
			int nCount = 0;
			while (hostinfo->h_addr_list[nCount])
			{
				ip = inet_ntoa(*(struct in_addr *)hostinfo->h_addr_list[nCount]);

				__ipLIst += "\n" + string(ip);
				++nCount;

				//printf("IP #%d: %s\n", ++nCount, ip);
			}
		}
	}
	return;

}

void dummy(int a){}



int recvbuflenName = DEFAULT_BUFLEN;
char recvbufName[DEFAULT_BUFLEN];







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
	if (::bind(s, (struct sockaddr *)&server, sizeof(server)) == SOCKET_ERROR)
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




	/*struct hostent *ent = gethostbyname("mehedee");
	struct in_addr ip_addr = *(struct in_addr *)(ent->h_addr);
	printf("Hostname: %s, was resolved to: %s\n ",
	"mehedee", inet_ntoa(ip_addr)
	);*/

	//printIPs();
	int lenVect;
	vector<string> splitV;




	int i = 0;
	string tmpString = "";


	//while ((new_socket = accept(s, (struct sockaddr *)&client, &c)) != INVALID_SOCKET)
	while (true)
	{

		new_socket = accept(s, (struct sockaddr *)&client, &c);
		//Sleep(20);

		//puts("Connection accepted");

		//g_lock.lock();
		OutputDebugString("1st thread");

		//try
		{
			memset(recvbufName, 0, sizeof recvbufName);


			recv(new_socket, recvbufName, recvbuflenName, NULL);

			//puts(recvbuf);


			splitV = splitString(recvbufName, ' ');

			OutputDebugString(recvbufName);




			lenVect = splitV.size();


			//cout << "\n----------" << endl;

			//for (int i = 0; i < len; i++)
			////cout << splitV[i] << endl;


			//
			//robot.mouseMoveTo(0, 0);


			//Reply to the client
			//message = "mehedee\n";


			if (lenVect > 0){
				//cout << splitV[0] << "=====" << endl;


				string testDe = " " + splitV[0] + " " + "\n";
				OutputDebugString(testDe.c_str());


				//int Flag = -1;
				//Flag = getIntFromString(splitV[0]);


				//performAction(splitV,new_socket, robot);


				if (splitV[0] == COMMAND_MOUSE_PAD_TOUCH_D){
					mouseUpCalled = 1;
				}





				if (splitV[0] == FLAG_SEARCH){
					char hostname[100];
					gethostname(hostname, 100);

					int hostnameLen = strlen(hostname);



					hostname[hostnameLen] = '\n';
					hostname[hostnameLen + 1] = '\0';



					//cout << "message added 2 |" << hostname[hostnameLen] << endl;



					send(new_socket, hostname, hostnameLen + 2, 0);

				}
				else if (splitV[0] == FLAG_STAR_PAD){

			//		std::thread tPad = std::thread(onePadThread, &new_socket);


				}


				//			closesocket(new_socket);
			}


			/*if (new_socket != INVALID_SOCKET)
			{
			closesocket(new_socket);
			}*/
		}
		//catch (const char *ex)
		{

		}
		closesocket(s);
		WSACleanup();

		return 0;

		//	_endthread();
	}
}



void onePadThread(SOCKET* new_socket)

{

	int lenVect;
	vector<string> splitV;
	Robot robot = new Robot();
	bool clickAndHold = false; 
	
	OutputDebugString("2nd thread");


	//send(*new_socket, "4", 1, 0);

	while (true)
	{

		memset(recvbuf, 0, sizeof recvbuf);
	
		recv(*new_socket, recvbuf, recvbuflen, NULL);
	
		splitV = splitString(recvbuf, ' ');
	
	lenVect = splitV.size();


	OutputDebugString(recvbuf);




	if (lenVect > 0){
		if (splitV[0] == COMMAND_MOUSE_LEFT_CLICK) {
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

			mouseUpCalled = 2;

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

		else if (splitV[0] == COMMAND_COPY) {
			robot.copyCommand();
		}

		else if (splitV[0] == COMMAND_CUT) {
			robot.cutCommand();
		}



		else if (splitV[0] == COMMAND_PEST) {
			robot.pestCommand();
		}

		else if (splitV[0] == TYPE_KEY_ALPHSBET) {

			if (splitV.size() > 1){

				std::string aa = " " + splitV[1] + " " + std::to_string(splitV.size()) + " \n";


				OutputDebugString(aa.c_str());


				robot.keyboard(splitV[1]);
			}
		}
		else if (splitV[0] == TYPE_KEY_DELETE) {

			robot.keyTypeDelete();
		}



		if (splitV[0] == COMMAND_MOUSE_MOVE &&mouseUpCalled == 1) {



			int x = stoi(splitV[1]);
			int y = stoi(splitV[2]);
			robot.mouseMoveTo(x, y);

			//thread tw1 = robot.callMoveTo(x,y);
			//tw1.join();


		}
	}
}




	//g_lock.unlock();


	closesocket(*new_socket);

}




void performAction(vector<string>  splitV, SOCKET new_socket, Robot &robot){



	if (splitV[0] == "18"){


		char hostname[100];
		gethostname(hostname, 100);

		//cout << "message added  " << hostname << endl;
		int hostnameLen = strlen(hostname);



		hostname[hostnameLen] = '\n';
		hostname[hostnameLen + 1] = '\0';



		//cout << "message added 2 |" << hostname[hostnameLen] << endl;



		send(new_socket, hostname, hostnameLen + 2, 0);

	}
	else if (splitV[0] == "3") {

		int x = getFloatFromString(splitV[1]);
		int y = getFloatFromString(splitV[2]);

		robot.mouseMoveTo(x, y);

		//			send(new_socket, message, strlen(message), 0);

	}

}
