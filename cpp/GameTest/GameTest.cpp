//------------------------------------------------------------------------
// GameTest.cpp
//------------------------------------------------------------------------
#include "stdafx.h"
//------------------------------------------------------------------------
#include <windows.h> 
#include <math.h>  
#include <fstream>
//------------------------------------------------------------------------
#include "app\app.h"
#include "table.h"
#include "GameSetting.h"

//------------------------------------------------------------------------
// Variables
//------------------------------------------------------------------------
CTable* pTable;
CPinBall* pPinBall;
CPoint* pPoint; // Initial position
CLineSegment* pLeftFlipper;
CLineSegment* pRightFlipper;
int pLeftAngle = 0, pRightAngle = 0;
CLineSegment* pDarthVader;
int pHighScore = 0;
float init_power = 0;
std::fstream file;

//------------------------------------------------------------------------
// Help functions
//------------------------------------------------------------------------
void loadScore(const char* fileName);
void saveScore(const char* fileName);
void loadTable(const char* fileName);
void printMessage(float x, float* y, float dy, char buffer[256], char* message, char intBuffer[10]);
void drawLine(CLineSegment& line);
void saveHighScore();
bool bump(CLineSegment& line);

//------------------------------------------------------------------------
// Called before first update. Do any initial setup here.
//------------------------------------------------------------------------
void Init()
{
	pTable = new CTable;
	loadTable(TABLE_FILE);
	pPoint = new CPoint(696.5f, 150.0f);
	pPinBall = new CPinBall(pPoint->m_x, pPoint->m_y, 0, 0, RADIUS, INIT_LIFE, 1.0f, 1.0f, 1.0f);
	pLeftFlipper = new CLineSegment(430, 152, 475, 120, eLine_Flipper);
	pRightFlipper = new CLineSegment(607, 152, 565, 120, eLine_Flipper);
	pDarthVader = new CLineSegment(524, 405, 494, 440, eLine_Power);
	loadScore(SCORE_FILE);
}

//------------------------------------------------------------------------
// Update your simulation here. deltaTime is the elapsed time since the last update in ms.
// This will be called at no greater frequency than the value of APP_MAX_FRAME_RATE
//------------------------------------------------------------------------
void Update(float deltaTime)
{
	// move pinball
	pPinBall->move();
	// move special character
	pDarthVader->Rotate(PI / 40);
	// gravity
	if (!pPoint->IsOnPoint(pPinBall->m_x, pPinBall->m_y, 3))
		pPinBall->m_dy -= GRAVITY;

	// calculate bump
	bool pIsBump = false;
	for (CLineSegment& line : pTable->m_lines)
	{
		pIsBump = bump(line);
		if (pIsBump) break;
	}
	if (!pIsBump) pIsBump = bump(*pLeftFlipper);
	if (!pIsBump) pIsBump = bump(*pRightFlipper);
	if (!pIsBump) pIsBump = bump(*pDarthVader);

	// key press
	if (App::IsKeyPressed(RIGHT_FLIPPER))
	{	//  right flipper
		if (pRightAngle == 0) pRightAngle = 1;
		// play sound
		App::PlaySoundW(LASER_SWORD);
	}
	if (App::IsKeyPressed(LEFT_FLIPPER))
	{	// left flipper 
		if (pLeftAngle == 0) pLeftAngle = 1;
		// play sound
		App::PlaySoundW(LASER_SWORD);
	}
	if (App::IsKeyPressed(NEW_GAME))
	{
		// save high score
		saveHighScore();
		
		//start a new game
		pPinBall->set(pPoint->m_x, pPoint->m_y, 0, 0);
		pPinBall->m_score = 0;
		pPinBall->m_life = INIT_LIFE;
	}
	if (App::IsKeyPressed(SPRING))
	{
		if (init_power < 8)
			init_power += 0.5;
	}
	else if (init_power > 0) 
	{
		if (pPinBall->m_life > 0 && pPoint->IsOnPoint(pPinBall->m_x, pPinBall->m_y, 3))
		{
			pPinBall->m_dy = init_power;
			App::PlaySoundW(LAUNCH);
		}
		init_power = 0;
	}
	
	// rotate right flipper
	if (pRightAngle > 0 && pRightAngle < 11)
	{
		pRightFlipper->Rotate(-PI / 40);
		pRightAngle++;
	}
	else if (pRightAngle > 10 && pRightAngle < 21)
	{
		pRightFlipper->Rotate(PI / 40);
		pRightAngle++;
	}
	else {
		pRightAngle = 0;
	}

	// rotate left flipper
	if (pLeftAngle > 0 && pLeftAngle < 11)
	{
		pLeftFlipper->Rotate(PI / 40);
		pLeftAngle++;
	}
	else if (pLeftAngle > 10 && pLeftAngle < 21)
	{
		pLeftFlipper->Rotate(-PI / 40);
		pLeftAngle++;
	}
	else {
		pLeftAngle = 0;
	}

}

//------------------------------------------------------------------------
// Add your display calls here (DrawLine or Print) 
// See App.h 
//------------------------------------------------------------------------
void Render()
{
	// display instructions
	float y = 740.0f;
	float dy = -12.0f;
	char buffer[256];
	char intBuffer[10];
	
	printMessage(10.0f, &y, dy, buffer, NEW_GAME_INSTRUCTION, intBuffer);
	printMessage(10.0f, &y, dy, buffer, LEFT_FLIPPER_INSTRUCTION, intBuffer);
	printMessage(10.0f, &y, dy, buffer, RIGHT_FLIPPER_INSTRUCTION, intBuffer);

	sprintf(intBuffer, "%d", pHighScore);
	printMessage(10.0f, &y, dy, buffer, "Highest Score: ", intBuffer);

	sprintf(intBuffer, "%d", pPinBall->m_score);
	printMessage(10.0f, &y, dy, buffer, "Current Score: ", intBuffer);

	sprintf(intBuffer, "%d", pPinBall->m_life);
	printMessage(10.0f, &y, dy, buffer, "PinBalls left: ", intBuffer);

	// draw table
	for (auto& line : pTable->m_lines)
	{
		drawLine(line);
	}

	// draw spring
	if (init_power > 0) {
		App::DrawRectangle(680, 136, 33, -init_power * 8);
	}

	// draw flippers
	drawLine(*pLeftFlipper);
	drawLine(*pRightFlipper);
	
	// draw special character
	drawLine(*pDarthVader);

	// draw pinball
	App::DrawCircle(pPinBall->m_x, pPinBall->m_y, pPinBall->m_r, pPinBall->m_Red, pPinBall->m_Green, pPinBall->m_Blue);
	
}

//------------------------------------------------------------------------
// Add your shutdown code here. Called when the APP_QUIT_KEY is pressed.
// Just before the app exits.
//------------------------------------------------------------------------
void Shutdown()
{
	// save score
	saveHighScore();
	saveScore(SCORE_FILE);
	// delete variables
	delete pTable;
	delete pPoint;
	delete pPinBall;
	delete pLeftFlipper;
	delete pRightFlipper;
	delete pDarthVader;
}

//------------------------------------------------------------------------
// Help functions
//------------------------------------------------------------------------
void loadScore(const char* fileName)
{
	file.open(fileName, std::fstream::in);
	if (file.is_open())
	{
		file >> pHighScore;
		file.close();
	}
}

void saveScore(const char* fileName)
{
	file.open(fileName, std::fstream::out);
	if (file.is_open())
	{
		file << pHighScore;
		file.close();
	}
}

void loadTable(const char* fileName)
{
	file.open(fileName, std::fstream::in);
	if (file.is_open())
	{
		int version = 0;
		file >> version;
		if (version <= 1)
		{
			pTable->m_lines.clear();
			while (!file.eof())
			{
				CLineSegment line;
				int type;
				file >> type;
				line.m_type = static_cast<LineType>(type);
				file >> line.m_start.m_x;
				file >> line.m_start.m_y;
				file >> line.m_end.m_x;
				file >> line.m_end.m_y;
				pTable->m_lines.push_back(line);
			}
		}

		file.close();
	}
}

void printMessage(float x, float* y, float dy, char buffer[256], char* message, char intBuffer[10])
{
	strcpy_s(buffer, 255, message);
	strcat(buffer, intBuffer);
	App::Print(10.0f, *y, buffer, 1.0f, 1.0f, 1.0f, GLUT_BITMAP_8_BY_13);
	*y += dy;
}

void drawLine(CLineSegment& line)
{
	CLineDefinition& def = pTable->m_lineDefs[line.m_type];
	App::DrawLine(line.m_start.m_x, line.m_start.m_y, line.m_end.m_x, line.m_end.m_y, def.m_Red, def.m_Green, def.m_Blue);
}

void saveHighScore() {
	if (pPinBall->m_score > pHighScore)
		pHighScore = pPinBall->m_score;
}

bool bump(CLineSegment& line)
{
	if (pPinBall->isOnBall(line)) {
		switch (line.m_type)
		{
		case eLine_Fail:
			pPinBall->set(pPoint->m_x, pPoint->m_y, 0, 0);
			pPinBall->m_life--;
			saveHighScore();
			break;
		case eLine_Score:
			pPinBall->m_score += BASE_SCORE;
			pPinBall->changeVelocity(line);
			App::PlaySoundW(LASER_GUN);
			break;
		case eLine_Hard:
			pPinBall->changeVelocity(line, HARD_FRICTION);
			break;
		case eLine_Soft:
			pPinBall->changeVelocity(line, SOFT_FRICTION);
			break;
		case eLine_Power:
			pPinBall->changeVelocity(line, POWER_FRICTION);
			App::PlaySoundW(LASER_GUN);
			break;
		case eLine_Flipper:
			pPinBall->changeVelocity(line, POWER_FRICTION);
			break;
		default:
			pPinBall->changeVelocity(line);
		}
		return true;
	}
	return false;
}