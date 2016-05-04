#include <iostream>
using namespace std;
#pragma pack(2)
class BU
{
int number;
union UBffer
{
char buffer[13];
int number;
}ubuf;
void foo(){}
typedef char* (*f)(void*);
enum{hdd,ssd,blueray}disk;
}bu;
int main(void)
{ 
printf("%d\n", sizeof(bu));
return 0;
}