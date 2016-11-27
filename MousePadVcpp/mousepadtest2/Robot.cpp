
#include<iostream>
#include<windows.h>
#include "Robot.h"
#define WINVER 0x0500

using namespace std;

Robot::~Robot(){

}

Robot::Robot(bool debugFlag){

	DEBUG_FLAG = debugFlag;

	if (DEBUG_FLAG)
		cout << "DEBUG:: this is robot class";
}

void Robot::mouseMoveTo(float dx, float dy){

	if (DEBUG_FLAG)
		cout << "from mouse method" << endl;


	memset(&mouseInput, 0, sizeof(mouseInput));

	mouseInput.type = INPUT_MOUSE;
	mouseInput.mi.dx = (int)dx;
	mouseInput.mi.dy = (int)dy;


	mouseInput.mi.dwFlags = MOUSEEVENTF_MOVE
		//| MOUSEEVENTF_ABSOLUTE 
		//| MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP
		;
	mouseInput.mi.mouseData = 0;
	mouseInput.mi.time = 0;





	SendInput(1, &mouseInput, sizeof(INPUT)); // 3rd param is size of an INPUT structure



}


void Robot::keyboard(char * line){



	memset(&keyboardInput, 0, sizeof(keyboardInput));
	char* str = line;

	int len = strlen(str);


	for (int i = 0; i < len; i++){
		memset(&keyboardInput, 0, sizeof(keyboardInput));

		keyboardInput.type = INPUT_KEYBOARD;
		keyboardInput.ki.wVk =
			VkKeyScanA(str[i]);



		SendInput(1, &keyboardInput, sizeof(INPUT)); // 3rd param is size of an INPUT structure

		keyboardInput.ki.dwFlags = KEYEVENTF_KEYUP;
		SendInput(1, &keyboardInput, sizeof(INPUT));
	}
}


void Robot::mouseLeftCLick(bool clickAndHold){

	if (DEBUG_FLAG)
		cout << "from mouse left click method" << endl;


	memset(&mouseInput, 0, sizeof(mouseInput));

	mouseInput.type = INPUT_MOUSE;
	mouseInput.mi.dx = 0;
	mouseInput.mi.dy = 0;


	mouseInput.mi.dwFlags =  MOUSEEVENTF_ABSOLUTE 
		| MOUSEEVENTF_LEFTDOWN ;

	if (clickAndHold == false){
		mouseInput.mi.dwFlags = mouseInput.mi.dwFlags | MOUSEEVENTF_LEFTUP;
	}

	mouseInput.mi.mouseData = 0;
	mouseInput.mi.time = 0;


	




	SendInput(1, &mouseInput, sizeof(INPUT)); 


}



void Robot::mouseLeftCLickUp(){

	if (DEBUG_FLAG)
		cout << "from mouse left click method" << endl;


	memset(&mouseInput, 0, sizeof(mouseInput));

	mouseInput.type = INPUT_MOUSE;
	mouseInput.mi.dx = 0;
	mouseInput.mi.dy = 0;
	
	
	mouseInput.mi.dwFlags = MOUSEEVENTF_ABSOLUTE | MOUSEEVENTF_LEFTUP;
	
	mouseInput.mi.mouseData = 0;
	mouseInput.mi.time = 0;







	SendInput(1, &mouseInput, sizeof(INPUT));

}


void Robot::mouseRightClick(){

	if (DEBUG_FLAG)
		cout << "from mouse left click method" << endl;


	memset(&mouseInput, 0, sizeof(mouseInput));

	mouseInput.type = INPUT_MOUSE;
	mouseInput.mi.dx = 0;
	mouseInput.mi.dy = 0;


	mouseInput.mi.dwFlags = MOUSEEVENTF_ABSOLUTE
		| MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP;

	mouseInput.mi.mouseData = 0;
	mouseInput.mi.time = 0;

	



	SendInput(1, &mouseInput, sizeof(INPUT));


}




void Robot::mouseScroll(int upOrDown){

	if (upOrDown ==1)
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, -50, 0);

	else 
		mouse_event(MOUSEEVENTF_WHEEL, 0, 0, 50, 0);



}


//=========================================================
//	KEYBOARD EVENTS
//=========================================================



void Robot::AltKeyDown(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;

	
		// Press the "ALt" key
		ip.ki.wVk = VK_MENU;
		ip.ki.dwFlags = 0; // 0 for key press
		SendInput(1, &ip, sizeof(INPUT));
}



void Robot::AltKeyUp(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Release the "ALt" key
	ip.ki.wVk = VK_MENU;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}

void Robot::F4KeyDown(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Ctrl" key
	ip.ki.wVk = VK_F4;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


}




void Robot::F4KeyUp(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;

	

	ip.ki.wVk = VK_F4;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}



void Robot::AltF4(){
	AltKeyDown();
	F4KeyDown();
	
	Sleep(10);


	AltKeyUp();
	F4KeyUp();



}




void Robot::pressEnter(){
	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_RETURN;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	// Release the "Enter" key
	ip.ki.wVk = VK_RETURN;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}



void Robot::pressESC(){
	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_ESCAPE;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	// Release the "Enter" key
	ip.ki.wVk = VK_ESCAPE;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}



void Robot::copyCommand(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));




	ip.ki.wVk = 'C';
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	Sleep(10);


	// Release the "Enter" key
	ip.ki.wVk = 'C';
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));



	// Release the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}
void Robot::cutCommand(){

	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));




	ip.ki.wVk = 'X';
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	Sleep(10);


	// Release the "Enter" key
	ip.ki.wVk = 'X';
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));



	// Release the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}

void Robot::pestCommand(){
	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));




	ip.ki.wVk = 'V';
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	Sleep(10);


	// Release the "Enter" key
	ip.ki.wVk = 'V';
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));



	// Release the "Enter" key
	ip.ki.wVk = VK_CONTROL;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}



void Robot::keyboard(string  line){



	memset(&keyboardInput, 0, sizeof(keyboardInput));
	string str = line;

	int len = str.length();

	

	for (int i = 0; i < len; i++){
		memset(&keyboardInput, 0, sizeof(keyboardInput));

		keyboardInput.type = INPUT_KEYBOARD;
		keyboardInput.ki.wVk = 0;
		keyboardInput.ki.dwFlags = KEYEVENTF_UNICODE;
		keyboardInput.ki.wScan = str[i];



		SendInput(1, &keyboardInput, sizeof(INPUT)); // 3rd param is size of an INPUT structure

		keyboardInput.ki.dwFlags = KEYEVENTF_KEYUP;
		SendInput(1, &keyboardInput, sizeof(INPUT));
	}
}



void Robot::keyTypeDelete(){
	INPUT ip;
	ip.type = INPUT_KEYBOARD;
	ip.ki.wScan = 0;
	ip.ki.time = 0;
	ip.ki.dwExtraInfo = 0;


	// Press the "Enter" key
	ip.ki.wVk = VK_BACK;
	ip.ki.dwFlags = 0; // 0 for key press
	SendInput(1, &ip, sizeof(INPUT));


	// Release the "Enter" key
	ip.ki.wVk = VK_BACK;
	ip.ki.dwFlags = KEYEVENTF_KEYUP;
	SendInput(1, &ip, sizeof(INPUT));
}