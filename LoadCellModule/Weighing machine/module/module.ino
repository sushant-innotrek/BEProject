#include <SPI.h>
#include "Adafruit_GFX.h"
#include <MCUFRIEND_kbv.h>
#include <DS3231.h>
#include <Fonts/FreeSansBold9pt7b.h>
#include <TouchScreen.h>
#include <String.h>
#include "HX711.h"
HX711 cell(A5, 12);
int initcnt = 0;
long val = 0;
float temp = 0;
float cal = 0;
boolean zeroed = false;
float zeroDifference = 0;
void status_print(char [], char []);
void ButtonUpdate(int);
void status_connection();
boolean isConnected = false;
void Loadcell();
TSPoint waitOneTouch();
void mPlus_isPressed();
void mReset_isPressed();
void zero_isPressed();
boolean ispressed = false;
boolean updateHold = false;
MCUFRIEND_kbv tft;
#define  BLACK   0x0000
#define  WHITE   0xFFFF
#define  CYAN    0x07FF
#define  GREEN   0x07E0
#define  MAGENTA 0xF81F
#define  YELLOW  0xFFE0
#define  RED     0xF800
#define  BLUE    0x001F
#define YP A1
#define XM A2
#define YM 7
#define XP 6
#define SENSITIVITY 100
#define MINPRESSURE 10
#define MAXPRESSURE 1000
TouchScreen ts = TouchScreen(XP, YP, XM, YM, SENSITIVITY);
void setup()
{
  tft.reset();
  Serial.begin(9600);
  tft.begin(tft.readID());
  tft.setRotation(0);
  tft.fillScreen(BLACK);
}
void loop() {
  checkTouch();
  if (initcnt < 50)
    initcnt++;
  val = 0.5 * val    +   0.5 * cell.read();
  temp  = val * 0.01;
  temp = (temp  - 84521 ) / 855850.0 * 800;
  Serial.println();
  if (initcnt < 2)
    status_print("Calculating", "Shop N Go module ");
  else if (initcnt == 45)
  {
    tft.fillScreen(BLACK);
    status_connection();
    tft.setCursor( 175, 95);
    tft.setFont(&FreeSansBold9pt7b);
    tft.setTextSize(2);
    tft.setTextColor(WHITE, BLACK);
    tft.print("kg");
    updateHold = true;
    status_print("Ready       ", " ");
    cal = 0 - temp;
  }
  else if (initcnt > 45)
  {
    temp = temp + zeroDifference + cal;
    tft.setCursor( 25 , 60);
    tft.setFont();
    tft.setTextSize(6);
    tft.setTextColor(WHITE, BLACK);
    tft.print(abs(temp) , 2);
    delay(100);
  }

}


TSPoint checkTouch() {
  TSPoint p;
  p = ts.getPoint();
  pinMode(XM, OUTPUT);
  pinMode(YP, OUTPUT);
  ispressed = false;
  if (p.z > 0) //is pressed
  {
    if (p.x > 400 && p.x < 550) // M+ or MR is pressed
    {
      if (p.y < 500) // M+ is pressed
      {
        buttonUpdate(1);
        ispressed = true;
        mPlus_isPressed();
      }
      else
      {
        buttonUpdate(2);
        ispressed = true;
        mReset_isPressed();
      }
    }
    else if (p.x > 550 && p.x < 700)
    {
      buttonUpdate(3);
      ispressed = true;
      zero_isPressed();
    }
  }
  if (ispressed)
  {
    updateHold = true;
    delay(100);
  }
  if (ispressed == false && updateHold == true)
  {
    buttonUpdate(0);
    updateHold = false;
    ispressed = false;
  }
}
void status_print(char a[], char b[])
{
  tft.setCursor(10, 190 + 15 + 60);
  tft.setFont();
  tft.setTextSize(2);
  tft.setTextColor(WHITE, BLACK);
  tft.print(a);

  tft.setCursor(10, 190 + 15 + 60 + 25);
  tft.setTextSize(2);
  tft.setTextColor(WHITE, BLACK);
  tft.print(b);
}
void buttonUpdate(int ch)
{
  switch (ch)
  {
    case 1:
      tft.fillRoundRect(10, 120, 100, 50, 6, CYAN); //M+ button
      tft.setCursor(10 + 38, 120 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, CYAN);
      tft.print("M+");
      break;
    case 2:
      tft.fillRoundRect(130, 120, 100, 50, 6, CYAN); //MR button
      tft.setCursor(130 + 35, 120 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, CYAN);
      tft.print("MR");
      break;
    case 3:
      tft.fillRoundRect(10, 190, 220, 50, 6, CYAN); //Zero Button
      tft.setCursor(10 + 78, 190 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, CYAN);
      tft.print("Zero");
      break;
    default:
      tft.fillRoundRect(10, 120, 100, 50, 6, BLUE); //M+ button
      tft.setCursor(10 + 38, 120 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, BLUE);
      tft.print("M+");
      tft.fillRoundRect(130, 120, 100, 50, 6, BLUE); //MR button
      tft.setCursor(130 + 35, 120 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, BLUE);
      tft.print("MR");
      tft.fillRoundRect(10, 190, 220, 50, 6, BLUE); //Zero Button
      tft.setCursor(10 + 78, 190 + 15);
      tft.setFont();
      tft.setTextSize(3);
      tft.setTextColor(WHITE, BLUE);
      tft.print("Zero");
  }
}
void status_connection()
{
  if (!isConnected)
  {
    tft.setCursor(68, 15);
    tft.setFont(&FreeSansBold9pt7b);
    tft.setTextSize(1);
    tft.setTextColor(RED, BLACK);
    tft.print("Disconnected");
  }
  else
  {
    tft.setCursor(75, 15);
    tft.setFont(&FreeSansBold9pt7b);
    tft.setTextSize(1);
    tft.setTextColor(GREEN, BLACK);
    tft.print("Connected");
  }
}
void mPlus_isPressed()
{
}
void mReset_isPressed()
{
}
void zero_isPressed()
{
  if (zeroed == true)
  {
    zeroDifference = 0;
    temp = 0;
    zeroed = false;
  }
  else
  {
    zeroDifference = 0 - temp;
    zeroed = true;
  }
}
