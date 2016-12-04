
#include<windows.h>
#include<string>
#include<cstdio>
#include<thread>

#pragma once





class Robot{

private:

	INPUT keyboardInput;
	INPUT mouseInput;

public:
	bool DEBUG_FLAG = false;


	Robot(){
		DEBUG_FLAG = false;
	}

	Robot(bool debugFlag);
	
	~Robot();


	void keyboard(char*);
	void mouse();
	//void mouseMoveTo(float, float);
	void mouseMoveTo(int, int);
	void mouseLeftCLick(bool clickAndHold);
	void mouseLeftDoubleClick();
	void mouseRightClick();
	void mouseScroll(int);
	void mouseLeftCLickUp();


	void keyboard(std::string s);

	void AltKeyDown();
	void AltKeyUp();
	void F4KeyDown();
	void F4KeyUp();

	void pressEnter();
	void pressESC();



	void AltF4();

	void copyCommand();
	void cutCommand();
	void pestCommand();

	void keyTypeDelete();


	std::thread callMoveTo(int x,int y){
		return std::thread([=] {mouseMoveTo(x, y); });
	}

};


void Robot::mouseMoveTo(int dx, int dy){




	memset(&mouseInput, 0, sizeof(mouseInput));

	mouseInput.type = INPUT_MOUSE;
	mouseInput.mi.dx = dx;
	mouseInput.mi.dy = dy;


	mouseInput.mi.dwFlags = MOUSEEVENTF_MOVE
		//| MOUSEEVENTF_ABSOLUTE 
		//| MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP
		;
	mouseInput.mi.mouseData = 0;
	mouseInput.mi.time = 0;





	SendInput(1, &mouseInput, sizeof(INPUT)); // 3rd param is size of an INPUT structure



}
