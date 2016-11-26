
#include<windows.h>

#pragma once





class Robot{

private:

	INPUT keyboardInput;
	INPUT mouseInput;

public:
	bool DEBUG_FLAG = false;


	
	Robot(bool debugFlag);
	~Robot();
		

	void keyboard(char*);
	void mouse();
	void mouseMoveTo(float, float);
	void mouseLeftCLick(bool clickAndHold);
	void mouseLeftDoubleClick();
	void mouseRightClick();
	void mouseScroll(int);
	void mouseLeftCLickUp();


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



};


