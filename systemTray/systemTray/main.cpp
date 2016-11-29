
#include "resource.h"
#include "main_c.h"
#include "Header.h"

#include<windows.h>
#include <shellapi.h>
#include<string>
#include<cstdio>
#include <process.h>
#include<thread>




#define ID_TRAY_APP_ICON    1001
#define ID_TRAY_EXIT        1002
#define WM_SYSICON          (WM_USER + 1)

/*variables*/
UINT WM_TASKBAR = 0;
HWND Hwnd;
HMENU Hmenu;
NOTIFYICONDATA notifyIconData;
NOTIFYICONDATA nid;
TCHAR szTIP[64] = TEXT("Mousepad here!");
char szClassName[] = "MousePad.";
HBITMAP bitmap;
RECT rect, imageRect;

/*procedures  */
LRESULT CALLBACK WindowProcedure(HWND, UINT, WPARAM, LPARAM);
void minimize();
void restore();
void InitNotifyIconData();

#pragma warning(disable: 4996)










unsigned int __stdcall mythread(void*){

	main3();

	return 0;
}


int WINAPI WinMain(HINSTANCE hThisInstance,
	HINSTANCE hPrevInstance,
	LPSTR lpszArgument,
	int nCmdShow)
{

	test();
	printIPs();


	/* This is the handle for our window */
	MSG messages;            /* Here messages to the application are saved */
	WNDCLASSEX wincl;        /* Data structure for the windowclass */
	WM_TASKBAR = RegisterWindowMessageA("TaskbarCreated");
	/* The Window structure */
	wincl.hInstance = hThisInstance;
	wincl.lpszClassName = szClassName;
	wincl.lpfnWndProc = WindowProcedure;      /* This function is called by windows */
	wincl.style = CS_DBLCLKS;                 /* Catch double-clicks */
	wincl.cbSize = sizeof (WNDCLASSEX);



	/* Use default icon and mouse-pointer */
	wincl.hIcon = LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON2));
	wincl.hIconSm = LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON2));
	wincl.hCursor = LoadCursor(NULL, IDC_ARROW);
	wincl.lpszMenuName = NULL;                 /* No menu */
	wincl.cbClsExtra = 0;                      /* No extra bytes after the window class */
	wincl.cbWndExtra = 0;                      /* structure or the window instance */
	wincl.hbrBackground = (HBRUSH)(CreateSolidBrush(RGB(255, 255, 255)));
	/* Register the window class, and if it fails quit the program */
	if (!RegisterClassEx(&wincl))
		return 0;

	/* The class is registered, let's create the program*/
	Hwnd = CreateWindowEx(
		0,                   /* Extended possibilites for variation */
		szClassName,         /* Classname */
		szClassName,         /* Title Text */
		WS_OVERLAPPED | WS_CAPTION | WS_SYSMENU | WS_MINIMIZEBOX | WS_MAXIMIZEBOX, /* default window */
		CW_USEDEFAULT,       /* Windows decides the position */
		CW_USEDEFAULT,       /* where the window ends up on the screen */
		544,                 /* The programs width */
		375,                 /* and height in pixels */
		HWND_DESKTOP,        /* The window is a child-window to desktop */
		NULL,                /* No menu */
		hThisInstance,       /* Program Instance handler */
		NULL                 /* No Window Creation data */
		);


	/*Initialize the NOTIFYICONDATA structure only once*/
	InitNotifyIconData();
	/* Make the window visible on the screen */
	ShowWindow(Hwnd, nCmdShow);



	
	bool first = 1;

	


	/* Run the message loop. It will run until GetMessage() returns 0 */
	while (GetMessage(&messages, NULL, 0, 0))
	{
		/* Translate virtual-key messages into character messages */
		TranslateMessage(&messages);
		/* Send message to WindowProcedure */
		DispatchMessage(&messages);
	
		
		if (first){
			HANDLE myhandleA = (HANDLE)_beginthreadex(0, 0, &mythread, (void*)0, 0, 0);

			//WaitForSingleObject(myhandleA, INFINITE);
			//CloseHandle(myhandleA);


			//std::thread t = std::thread(main3);
			

			first = false;
		}

	}




	return messages.wParam;

}



/*  This function is called by the Windows function DispatchMessage()  */

LRESULT CALLBACK WindowProcedure(HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{

	if (message == WM_TASKBAR && !IsWindowVisible(Hwnd))
	{
		minimize();
		return 0;
	}

	switch (message)                  /* handle the messages */
	{

	
	BITMAP bp;

	case WM_ACTIVATE:
		Shell_NotifyIcon(NIM_ADD, &nid);
		//Shell_NotifyIcon(NIM_SETVERSION, &notifyIconData);
		//NIM_SETVERSION
		break;
	case WM_CREATE:

		//printIPs();
		//bitmap = (HBITMAP)LoadImage(NULL, MAKEINTRESOURCE(IDB_BITMAP3), IMAGE_BITMAP, 0, 0, LR_DEFAULTCOLOR);
		//bitmap = (HBITMAP)LoadImage(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_BITMAP3), IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE);
		//GetModuleHandle(NULL), MAKEINTRESOURCE(ID_ICON1), IMAGE_BITMAP, 0, 0, LR_LOADFROMFILE

		bitmap = LoadBitmap(GetModuleHandle(NULL), MAKEINTRESOURCE(IDB_BITMAP2));
		
		//SetForegroundWindow((HWND)(((ULONG)Hwnd) | 0x01));
		
		

		/*ShowWindow(Hwnd, SW_HIDE);
		Hmenu = CreatePopupMenu();
		AppendMenu(Hmenu, MF_STRING, ID_TRAY_EXIT, TEXT("Exit The Demo"));*/
		



		break;

	case WM_SYSCOMMAND:
		/*In WM_SYSCOMMAND messages, the four low-order bits of the wParam parameter
		are used internally by the system. To obtain the correct result when testing the value of wParam,
		an application must combine the value 0xFFF0 with the wParam value by using the bitwise AND operator.*/

		switch (wParam & 0xFFF0)
		{
		case SC_MINIMIZE:
		case SC_CLOSE:
			minimize();
			return 0;
			break;
		}
		break;


		// Our user defined WM_SYSICON message.
	case WM_SYSICON:
	{

					   switch (wParam)
					   {
					   case ID_TRAY_APP_ICON:
						   SetForegroundWindow(Hwnd);

						   break;
					   }


					   if (lParam == WM_LBUTTONUP)
					   {

						   restore();


					   }
					   else if (lParam == WM_RBUTTONDOWN)
					   {
						   // Get current mouse position.
						   POINT curPoint;
						   GetCursorPos(&curPoint);
						   SetForegroundWindow(Hwnd);

						   // TrackPopupMenu blocks the app until TrackPopupMenu returns

						   UINT clicked = TrackPopupMenu(Hmenu, TPM_RETURNCMD | TPM_NONOTIFY, curPoint.x, curPoint.y, 0, hwnd, NULL);



						   SendMessage(hwnd, WM_NULL, 0, 0); // send benign message to window to make sure the menu goes away.
						   if (clicked == ID_TRAY_EXIT)
						   {
							   // quit the application.
							   Shell_NotifyIcon(NIM_DELETE, &nid);
							   PostQuitMessage(0);
						   }
						   if (clicked == ID_TRAY_TASK)
						   {
							   char *t = "MousePad\nAn opne source software\nAologizing for bad performance :(\n\nDeveloper :\n mehedee.hassan@outlook.com";
							   MessageBox(NULL, t, "Software Info", NULL);
						   }
						   if (clicked == ID_TRAY_MY_IP)
						   {
							   MessageBox(NULL, __ipLIst.c_str(), "Ip List", NULL);
						   }
					   }
	}
		break;

		// intercept the hittest message..
	case WM_NCHITTEST:
	{
						 UINT uHitTest = DefWindowProc(hwnd, WM_NCHITTEST, wParam, lParam);
						 if (uHitTest == HTCLIENT)
							 return HTCAPTION;
						 else
							 return uHitTest;
	}
	

	case WM_CLOSE:

		minimize();
		return 0;
		break;

	case WM_DESTROY:

		PostQuitMessage(0);
		break;


	case WM_SIZE:
		//InvalidateRect(hwnd, NULL, FALSE);
		

		break;


	

	case WM_PAINT:
		


		HDC hdc3 = GetDC(hwnd);

		GetClientRect(hwnd, &rect);
		rect.left = 215;
		rect.top = 26;
		
		char * text = "Your local ip list:\n";


		std::string tempa = std::string(text);
		
		

		std::string tempb = tempa + ""
			+ __ipLIst
			;

		
		DrawTextA(hdc3, tempb.c_str(), tempb.length(), &rect, DT_LEFT);
		DeleteDC(hdc3);


		

		hdc3 = GetDC(hwnd);
		GetClientRect(hwnd, &rect);
		rect.left = 215;
		rect.top = 5;

		if (myPcName != "")
			myPcName = "Hi,Your PC: " + myPcName;

		

		DrawTextA(hdc3, myPcName.c_str(), myPcName.length(), &rect, DT_LEFT);
		DeleteDC(hdc3);




		hdc3 = GetDC(hwnd);
		GetClientRect(hwnd, &rect);
		rect.left = 215;
		rect.top = 230;

		char * bottomText = "If automatic process doesn't work\nPlease add manually one at a time\nto your android device\nand try to connect.";
		DrawTextA(hdc3, bottomText, strlen(bottomText), &rect, DT_LEFT);
		DeleteDC(hdc3);



		//PAINTSTRUCT ps2;


		
		

		PAINTSTRUCT ps;
		RECT r,r2;

		
		HDC hdc = BeginPaint(hwnd, &ps), 
		
			
		hdcBitmap = CreateCompatibleDC(hdc);
	


		SelectObject(hdcBitmap, bitmap);
		GetObject(bitmap, sizeof(bp), &bp);
		GetClientRect(hwnd, &r);
		
		// troublesome part, in my oppinion

		StretchBlt(hdc, 0, 0, 200, 350 , hdcBitmap, 0, 0, 200, 350, MERGECOPY);


		EndPaint(hwnd, &ps);
		DeleteDC(hdcBitmap);
		DeleteDC(hdc);


		PAINTSTRUCT ps2;
		HDC hdc2 = BeginPaint(hwnd, &ps2);
		GetClientRect(hwnd, &r2);
		

		TextOut(hdc2, 400, 300, TEXT("Text Out String"), strlen("Text Out String"));

		EndPaint(hwnd, &ps2);
		DeleteDC(hdc2);

		





		break;

	


	}


	return DefWindowProc(hwnd, message, wParam, lParam);
}


void minimize()
{
	// hide the main window
	ShowWindow(Hwnd, SW_HIDE);
}


void restore()
{
	ShowWindow(Hwnd, SW_SHOWNORMAL);
}

void InitNotifyIconData()
{
	

	




	memset(&nid, 0, sizeof(NOTIFYICONDATA));


	nid.cbSize = sizeof(NOTIFYICONDATA);
	nid.hWnd = Hwnd;
	// or whatever HWND you provided when you created it 
	nid.uID = ID_TRAY_APP_ICON; // or whatever ID you provided when you created it
	nid.uFlags = NIF_INFO | NIF_ICON | NIF_MESSAGE | NIF_TIP;
	nid.uTimeout = 1; // In milliseconds. Min value is 10 seconds, max is 30 seconds. If outside of range, it automatically takes closest limit 
	nid.dwInfoFlags = NIIF_INFO;
	nid.hIcon = (HICON)LoadIcon(GetModuleHandle(NULL), MAKEINTRESOURCE(IDI_ICON3));
	nid.uTimeout = 1;

	nid.uCallbackMessage = WM_SYSICON;
	
	strcpy(nid.szInfo, "I'm here");
	strcpy(nid.szInfoTitle, "Hi!");
	strncpy(nid.szTip, szTIP, sizeof(szTIP));


	ShowWindow(Hwnd, SW_HIDE);
	Hmenu = CreatePopupMenu();
	AppendMenu(Hmenu, MF_STRING, ID_TRAY_EXIT, TEXT("Exit"));
	AppendMenu(Hmenu, MF_STRING, ID_TRAY_TASK, TEXT("Info"));
	AppendMenu(Hmenu, MF_STRING, ID_TRAY_MY_IP, TEXT("My Ip"));


	Shell_NotifyIcon(NIM_ADD, &nid);


}
