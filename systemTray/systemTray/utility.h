
#include<vector>
#include<iostream>
#include<cstdio>
#include<cstring>
#include<sstream>
using namespace std;















const string COMMAND_MOUSE_PAD_TOUCH_D = "1";
const string COMMAND_MOUSE_MOVE = "3";
const string FLAG_STAR_PAD = "4";
const string COMMAND_CLOSE_WINDOW = "7";
const string COMMAND_TAB_WINDOW = "8";
const string COMMAND_ENTER = "9";
const string COMMAND_SCROLL_VERTICAL_DOWN = "10";
const string COMMAND_SCROLL_VERTICAL_UP = "11";
const string COMMAND_ESC = "12";
const string COMMAND_MOUSE_LEFT_CLICK = "13";
const string COMMAND_MOUSE_RIGHT_CLICK = "14";
const string COMMAND_MOUSE_LEFT_CLICKHOLD = "15";
const string COMMAND_MOUSE_PAD_SHORT_CLICK = "5";
const string COMMAND_MOUSE_PAD_UP = "16";
const string FLAG_SEARCH = "18";

const string COMMAND_COPY = "19";
const string COMMAND_PEST = "20";
const string COMMAND_CUT = "21";
const string TYPE_KEY_ALPHSBET = "22";
const string TYPE_KEY_DELETE = "23";










vector<string> splitString(char * message, char delimiter){


	int len = strlen(message);

	vector <string> returnVector;

	string tmpString = "";
	int j = 0;

	for (int i = 0; i < len; i++){

		// cout<<message[i]<<" ";



		if (message[i + 1] == '\0'){

			tmpString += message[i];
			//cout << test << endl;

			returnVector.push_back(tmpString);
			tmpString = "";
			j++;
		}
		else
		if (message[i] != delimiter){
			tmpString += message[i];
		}
		else{

			//cout << test << endl;
			returnVector.push_back(tmpString);
			tmpString = "";
			j++;
		}

	}





	return returnVector;
}





vector<string> splitString2(const char * message, char delimiter){


	int len = strlen(message);

	vector <string> returnVector;

	string tmpString = "";
	int j = 0;

	for (int i = 0; i < len; i++){

		// cout<<message[i]<<" ";



		if (message[i + 1] == '\0'){

			tmpString += message[i];
			//cout << test << endl;

			returnVector.push_back(tmpString);
			tmpString = "";
			j++;
		}
		else
		if (message[i] != delimiter){
			tmpString += message[i];
		}
		else{

			//cout << test << endl;
			returnVector.push_back(tmpString);
			tmpString = "";
			j++;
		}

	}





	return returnVector;
}



string* splitStringStrArray(char * message, char delimiter){


	int len = strlen(message);

	string returnVector[3];

	string tmpString = "";
	int j = 0;

	for (int i = 0; i < len; i++){

		// cout<<message[i]<<" ";



		if (message[i + 1] == '\0'){

			tmpString += message[i];
			//cout << test << endl;

			returnVector[j] = tmpString;
			tmpString = "";
			j++;
		}
		else
		if (message[i] != delimiter){
			tmpString += message[i];
		}
		else{

			//cout << test << endl;
			returnVector[j] = tmpString;

			tmpString = "";
			j++;
		}

	}





	return returnVector;
}



template <typename Ttype> Ttype fromString(const std::string& str){

	std::istringstream ss(str);


	Ttype returnString;
	ss >> returnString;

	return returnString;

}




int getIntFromString(string str){

	int data = -1;

	try{

		if (!str.empty()){
			data = fromString<int>(str);
			cout << "FLAG = " << data << endl;
		}

	}
	catch (exception &e){
		cout << " exception " << endl;

	}

	return data;
}



float getFloatFromString(string str){

	float data = -1;

	try{

		if (!str.empty()){
			data = fromString<float>(str);
			cout << "FLAG = " << data << endl;
		}

	}
	catch (exception &e){
		cout << " exception " << endl;

	}

	return data;
}

