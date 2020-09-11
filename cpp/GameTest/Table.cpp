#include "stdafx.h"
#include "table.h"

CTable::CTable()
{
	m_lineDefs[eLine_Hard] = CLineDefinition("Hard", .8f, .6f, .2f);
	m_lineDefs[eLine_Soft] = CLineDefinition("Soft", .4f, .3f, .1f);
	m_lineDefs[eLine_Power] = CLineDefinition("Power", .2f, .2f, .6f);
	m_lineDefs[eLine_Flipper] = CLineDefinition("Flipper", .5f, .9f, .1f);
	m_lineDefs[eLine_Fail] = CLineDefinition("Fail", .8f, .3f, .9f);
	m_lineDefs[eLine_Score] = CLineDefinition("Score", .7f, .4f, .4f);
}

CLineSegment::CLineSegment() : CLineSegment(0.0f, 0.0f, 0.0f, 0.0f, eLine_Fail)
{
}

CLineSegment::CLineSegment(float x1, float y1, float x2, float y2, LineType type)
{
	m_start.m_x = x1;
	m_start.m_y = y1;
	m_start.m_selected = false;
	m_end.m_x = x2;
	m_end.m_y = y2;
	m_end.m_selected = false;
	m_type = type;
	m_selected = false;
}

void CLineSegment::Rotate(float degree)
{
	// create a vector from start to end
	float x1 = m_end.m_x - m_start.m_x;
	float y1 = m_end.m_y - m_start.m_y;

	// rotate vector
	float x2 = x1 * cos(degree) - y1 * sin(degree);
	float y2 = x1 * sin(degree) + y1 * cos(degree);

	// m_end = m_start + vector
	m_end.m_x = m_start.m_x + x2;
	m_end.m_y = m_start.m_y + y2;
}


float CLineSegment::DistanceToLine(float x, float y)
{
	const float x0 = m_start.m_x;
	const float x1 = m_end.m_x;
	const float y0 = m_start.m_y;
	const float y1 = m_end.m_y;

	float dx = x1 - x0;
	float dy = y1 - y0;

	float linelengthSquared = dx*dx + dy*dy;
	float param = ((x - x0)*dx + (y - y0)*dy) / linelengthSquared;
	if (param > 1)
		param = 1;
	if (param < 0)
		param = 0;

	float nearestX = x0 + param*dx;
	float nearestY = y0 + param*dy;

	float distX = x - nearestX;
	float distY = y - nearestY;
	float distance = sqrt((distX*distX) + (distY*distY));
	return distance;
}

/* float CLineSegment::DistanceToInfiniteLine(float x, float y)
{
	// See https://en.wikipedia.org/wiki/Distance_from_a_point_to_a_line
	const float x0 = m_start.m_x;
	const float x1 = m_end.m_x;
	const float y0 = m_start.m_y;
	const float y1 = m_end.m_y;

	float numerator = abs((y1 - y0)*x - (x1 - x0)*y + x1*y0 - y1*x0);
	float denomenator = sqrt((y1 - y0)*(y1 - y0) + (x1 - x0)*(x1 - x0));
	float distance = numerator / denomenator;
	
	return distance;
} */

bool CLineSegment::IsOnLine(float x, float y, float tolerance)
{
	return DistanceToLine(x,y) < tolerance;
}

CPoint& CLineSegment::NormalVector(float x, float y)
{
	CPoint point;

	float dx = m_start.m_x - m_end.m_x;
	float dy = m_start.m_y - m_end.m_y;

	if (dy == 0) {
		dx = -1;
	}
	else if (dy < 0)
	{
		dx = -dx;
		dy = -dy;
	}

	float distance = sqrt(dx * dx + dy * dy);

	point.m_x = dy / distance;
	point.m_y = -1 * dx / distance;

	return point;
}

float CPoint::DistanceToPoint(float x, float y)
{
	float dX = x - m_x;
	float dY = y - m_y;
	float distance = sqrt(dX*dX + dY*dY);

	return distance;
}

CPoint::CPoint(float x, float y) {
	m_x = x;
	m_y = y;
}

CPoint::CPoint() {
}

bool CPoint::IsOnPoint(float x, float y, float tolerance)
{
	return DistanceToPoint(x, y) < tolerance;
}

CLineDefinition::CLineDefinition(const char * name, float r, float g, float b)
{
	m_name = name;
	m_Red = r;
	m_Green = g;
	m_Red = b;
}

CPinBall::CPinBall(float init_x, float init_y, float dx, float dy, float radius, int init_life, float r, float g, float b)
{
	m_x = init_x;
	m_y = init_y;
	m_r = radius;
	m_dx = dx;
	m_dy = dy;
	m_Red = r;
	m_Green = g;
	m_Blue = b;
	m_life = init_life;
}

void CPinBall::set(float init_x, float init_y, float dx, float dy) 
{
	m_x = init_x;
	m_y = init_y;
	m_dx = dx;
	m_dy = dy;
}

void CPinBall::changeVelocity(CLineSegment& line, float ratio)
{
	if (ratio > 1 && sqrt(m_dx * m_dx + m_dy * m_dy) > 6) // set highest velocity
		ratio = 0.95;
	bool onStartPoint = line.m_start.IsOnPoint(m_x, m_y, m_r);
	if (onStartPoint || line.m_end.IsOnPoint(m_x, m_y, m_r))
	{	// hit the point
		CPoint* point = onStartPoint ? &line.m_start : &line.m_end;
		// the force is from point to center
		float dx = m_x - point->m_x;
		float dy = m_y - point->m_y;
		float length = sqrt(m_dx * m_dx + m_dy * m_dy) / sqrt(dx * dx + dy * dy);
		m_dx += ratio * length * dx;
		m_dy += ratio * length * dy;
	}
	else
	{	// hit the line
		CPoint& normalVector = line.NormalVector(m_x, m_y);
		// the force is perpendicular to the line
		float vn = m_dx * normalVector.m_x + m_dy * normalVector.m_y;
		normalVector.m_x *= vn;
		normalVector.m_y *= vn;
		m_dx = ratio * m_dx - (1 + ratio) * normalVector.m_x;
		m_dy = ratio * m_dy - (1 + ratio) * normalVector.m_y;
	}
}

bool CPinBall::isOnBall(CLineSegment& line)
{
	return line.IsOnLine(m_x, m_y, m_r);
}

void CPinBall::move()
{
	m_x += m_dx;
	m_y += m_dy;
}
