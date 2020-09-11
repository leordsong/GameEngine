#include <vector>
#pragma once

enum LineType
{
	eLine_Hard = 0,
	eLine_Soft,
	eLine_Power,
	eLine_Flipper,
	eLine_Fail,
	eLine_Score,
	eLine_END
};

class CPoint
{
public:
	float DistanceToPoint(float x, float y);
	bool IsOnPoint(float x, float y, float tolerance);
	CPoint(float x, float y);
	CPoint();
public:
	float m_x;
	float m_y;
	bool m_selected;
};

class CLineSegment
{
public:
	CLineSegment();
	CLineSegment(float x1, float y1, float x2, float y2, LineType type);
	float DistanceToLine(float x, float y);
	bool IsOnLine(float x, float y, float tolerance = 5.0f);
	CPoint& NormalVector(float x, float y);
	void Rotate(float degree);
public:
	CPoint m_start;
	CPoint m_end;
	LineType m_type;
	bool m_selected;
};

class CLineDefinition
{
public:
	CLineDefinition() {};
	CLineDefinition(const char* name, float r, float g, float b);
public:
	float m_Red;
	float m_Green;
	float m_Blue;
	const char* m_name;
};

// To hold a set of lines describing the background for the game
class CTable
{
public:
	CTable();
public:
	std::vector<CLineSegment> m_lines;
	CLineDefinition m_lineDefs[eLine_END];
};

class CPinBall
{
public:
	CPinBall(float init_x, float init_y, float dx, float dy, float radius, int init_life, float r, float g, float b);
	void changeVelocity(CLineSegment& line, float ratio = 1);
	void set(float init_x, float init_y, float dx, float dy);
	bool isOnBall(CLineSegment& line);
	void move();
	bool bump(CLineSegment& line, int base_score, float init_x, float init_y, float hard, float soft, float power);
public:
	float m_x;
	float m_y;
	float m_r;
	float m_dx;
	float m_dy;
	float m_Red;
	float m_Green;
	float m_Blue;
	int m_score = 0;
	int m_life;
};



