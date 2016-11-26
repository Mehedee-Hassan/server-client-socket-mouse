
#include<vector>
#include<iostream>
#include<cstdio>
#include<cstring>
#include "utility.h"
using namespace std;



int testSplitString(char delim){


	vector<string>aa;

	char* aastr = "2 2.2323 4.23";
	aa = splitString(aastr, delim);

	
	int len = aa.size();

	cout << "\ntest split string vector\n";
	for (int i = 0; i < len; i ++)
		cout << " " << aa[i];


	return 0;
}
