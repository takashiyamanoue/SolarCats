/** C wrapper for RTOS API.
    TC 2.01.
    axel@uos.de 3/2000, vaguely based on Beck's version.
  */
#ifndef RTOS_H
#define RTOS_H
#include "types.h"

typedef Word RtosStatus;

typedef struct {
 void (*proc)();	/* task procedure pointer */
 char name[4];		/* name 4 characters not null terminated */
 Word* stackptr;	/* task stack pointer */ 
 Word stacksize;	/* size of task stack (bytes) */
 Word attrib;	  	/* task attributes, not supported by the RTOS API */
 Word priority;		/* task priority, range: 20<=priority<=127 */
 Word unsupported[5];
   } TaskDefBlock;

typedef struct {
  Byte sec;	/* seconds	(0-59) */
  Byte min;	/* minutes	(0-59) */
  Byte hr;	/* hours	(0-23) */
  Byte dy;	/* day		(1-31) */
  Byte mn;	/* month	(1-12) */
  Byte yr;	/* year		(0-99) */
  Byte dow;	/* day of week	(Mon=1 to Sun=7) */
  Byte dcen;	/* century if time/date is correct */
    } TimeDate;

typedef struct {
  RtosStatus (*sleep)(Word milliseconds);
  RtosStatus (*createTask)(Word* taskId, TaskDefBlock* taskDefBlock);
  RtosStatus (*kill)(Word taskId);
  RtosStatus (*deleteTask)(Word taskId);
  RtosStatus (*getTaskID)(void);
  RtosStatus (*wait)(void);
  RtosStatus (*wakeup)(Word taskId);
  void (*exit)(void);
  RtosStatus (*setPriority)(Word taskId, Word priority);
  RtosStatus (*accessFiles)(void);
  RtosStatus (*createSema)(Word* semId, char* name, int initValue);
  RtosStatus (*deleteSema)(Word semId);
  RtosStatus (*freeSema)(Word semId);
  RtosStatus (*getSema)(Word semId);
  RtosStatus (*releaseSema)(Word semId);
  RtosStatus (*reserveSema)(Word semId, long* milliseconds);
  RtosStatus (*signalSema)(Word semId);
  RtosStatus (*waitSema)(Word semId, long* milliseconds);
  void (*getTimeDate)(TimeDate* td);
  void (*setTimeDate)(TimeDate* td);
  void (*getTicks)(unsigned long* milliseconds);
    } Rtos;

extern Rtos rtos;
#endif
