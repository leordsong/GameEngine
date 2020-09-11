#ifndef _GAME_SETTING_H
#define _GAME_SETTING_H

#define GRAVITY (0.03f)
#define RADIUS (15.0f)
#define HARD_FRICTION (0.95f)
#define SOFT_FRICTION (0.8f)
#define POWER_FRICTION (1.2f)
#define INIT_LIFE 3
#define BASE_SCORE 30

// keys
#define LEFT_FLIPPER (0x5A)
#define RIGHT_FLIPPER VK_OEM_2
#define NEW_GAME VK_F2
#define SPRING VK_SPACE

// messages (change messages accordingly with keys)
#define NEW_GAME_INSTRUCTION ("Prezz F2 to start a new game")
#define LEFT_FLIPPER_INSTRUCTION ("Prezz z to trigger left flipper")
#define RIGHT_FLIPPER_INSTRUCTION ("Prezz / to trigger left flipper")

// data files
#define TABLE_FILE ("table.txt")
#define SCORE_FILE ("score.txt")

// sound files
#define LASER_SWORD ("swing.wav")
#define LASER_GUN ("laser.wav")
#define LAUNCH ("ship.wav")

#endif
