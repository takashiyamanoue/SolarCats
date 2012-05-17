/** C wrapper for RTOS API.
    TC 2.01.
    axel@uos.de 3/2000, vaguely based on Beck's version.
  */
#include <dos.h>
#include "rtos.h"

#define RTOS		0xAD	/* rtos service interrupt */

#define RTX_SLEEP_TIME        0   /* Sleep for a defined time */
#define RTX_TASK_CREATE       1   /* Create and start a task  */
#define RTX_TASK_KILL         2   /* Stop and kill a task*/
#define RTX_TASK_DELETE       3   /* Remove a task from the system */
#define RTX_GET_TASKID        4   /* Get ID of the current running task*/
#define RTX_SLEEP_REQ         5   /* Sleep, until a wake request occurs*/
#define RTX_WAKEUP_TASK       6   /* Wake a task, which is waiting*/
#define RTX_END_EXEC          7   /* End task execution*/
#define RTX_CHANGE_PRIO       8   /* Change the priority of a task*/
#define RTX_ACCESS_FILESYSTEM 9   /* Enable filesystem access for task*/

#define RTX_CREATE_SEM    20   /* Create a semaphore */
#define RTX_DELETE_SEM    21   /* Delete a semaphore */
#define RTX_FREE_RES      22   /* Free a resource semaphore  */
#define RTX_GET_SEM       23   /* Get use of a counting semaphore(no wait)*/
#define RTX_RELEASE_SEM   24   /* Release a resource semaphore*/
#define RTX_RESERVE_RES   25   /* Reserve a resource semaphore*/
#define RTX_SIGNAL_SEM    26   /* Signal a counting semaphore */
#define RTX_WAIT_SEM      27   /* Wait on a counting semaphore (opt timeout)*/

#define RTX_GET_TIMEDATE  40
#define RTX_SET_TIMEDATE  41
#define RTX_GET_TICKS     42

#define req(service) union REGS regs; regs.h.ah = service; int86(RTOS, &regs, &regs)
#define req0(service) req(service); return regs.x.ax
#define req1(service, info) union REGS regs; regs.h.ah = service; regs.x.bx = info; int86(RTOS, &regs, &regs); return regs.x.ax
#define req2(service, info, addr) union REGS regs; struct SREGS sregs; regs.h.ah = service; regs.x.bx = info; sregs.es = FP_SEG(addr); regs.x.di = FP_OFF(addr); int86x(RTOS, &regs, &regs, &sregs); return regs.x.ax
#define reqt(service, addr) union REGS regs; regs.h.ah = service; regs.x.bx = FP_SEG(addr); regs.x.si = FP_OFF(addr); int86(RTOS, &regs, &regs)

static RtosStatus rsleep (Word milliseconds) {
  req1(RTX_SLEEP_TIME, milliseconds);
}

static RtosStatus createTask (Word* taskId, TaskDefBlock* taskDefBlock) {
  union REGS regs; struct SREGS sregs;
  
  regs.h.ah = RTX_TASK_CREATE;
  regs.x.bx = FP_SEG(taskId);
  regs.x.si = FP_OFF(taskId);
  sregs.es = FP_SEG(taskDefBlock);
  regs.x.di = FP_OFF(taskDefBlock);
  int86x(RTOS, &regs, &regs, &sregs);
  return regs.x.ax;
}

static RtosStatus kill (Word taskId) {
  req1(RTX_TASK_KILL, taskId);
}

static RtosStatus deleteTask (Word taskId) {
  req1(RTX_TASK_DELETE, taskId);
}

static RtosStatus getTaskID (void) {
  req0(RTX_GET_TASKID);
}

static RtosStatus wait (void) {
  req0(RTX_SLEEP_REQ);
}

static RtosStatus wakeup (Word taskId) {
  req1(RTX_WAKEUP_TASK, taskId);
}

static void exit (void) {
  req(RTX_END_EXEC);
}

static RtosStatus setPriority (Word taskId, Word priority) {
  union REGS regs;

  regs.h.ah = RTX_CHANGE_PRIO;
  regs.x.bx = taskId;
  regs.x.cx   = priority;
  int86(RTOS, &regs, &regs);
  return regs.x.dx ? regs.x.ax : 0;
}

static RtosStatus accessFiles (void) {
  req0(RTX_ACCESS_FILESYSTEM);
}

static RtosStatus createSema (Word* semId, char* name, int initValue) {
  union REGS regs; struct SREGS sregs;
  
  regs.h.ah = RTX_CREATE_SEM;
  regs.x.bx = FP_SEG(semId);
  regs.x.si = FP_OFF(semId);
  regs.x.cx = initValue;
  sregs.es = FP_SEG(name);
  regs.x.di = FP_OFF(name);
  int86x(RTOS, &regs, &regs, &sregs);
  return regs.x.ax;
}

static RtosStatus deleteSema (Word semId) {
  req1(RTX_DELETE_SEM, semId);
}

static RtosStatus freeSema (Word semId) {
  req1(RTX_FREE_RES, semId);
}

static RtosStatus getSema (Word semId) {
  req1(RTX_GET_SEM, semId);
}

static RtosStatus releaseSema (Word semId) {
  req1(RTX_RELEASE_SEM, semId);
}

static RtosStatus reserveSema (Word semId, long* milliseconds) {
  req2(RTX_RESERVE_RES, semId, milliseconds);
}

static RtosStatus signalSema (Word semId) {
  req1(RTX_SIGNAL_SEM, semId);
}

static RtosStatus waitSema (Word semId, long* milliseconds) {
  req2(RTX_WAIT_SEM, semId, milliseconds);
}

static void getTimeDate (TimeDate* td) {
  reqt(RTX_GET_TIMEDATE, td);
}

static void setTimeDate (TimeDate* td) {
  reqt(RTX_SET_TIMEDATE, td);
}

static void getTicks (unsigned long* milliseconds) {
  reqt(RTX_GET_TICKS, milliseconds);
}

Rtos rtos = {
  rsleep,
  createTask,
  kill,
  deleteTask,
  getTaskID,
  wait,
  wakeup,
  exit,
  setPriority,
  accessFiles,
  createSema,
  deleteSema,
  freeSema,
  getSema,
  releaseSema,
  reserveSema,
  signalSema,
  waitSema,
  getTimeDate,
  setTimeDate,
  getTicks,
    };
