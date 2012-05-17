/** manage [and test] ft parallel interface.
#ifdef _WIN32
    MS VC++ 4.2.
#endif
#ifdef SC12
    TC 2.01.
    Based on DK40_IO (C) 1999 by BECK IPC GmbH.
#endif
    axel@uos.de 3/2000.
  */
#ifndef PARALLEL_H
#define PARALLEL_H

#ifdef SC12
# define DK40	0x600		/* port for pins on DK40 board */
				/* pins are connected to parallel interface */
#endif

extern int ftTraceFlags;	/* set to activate trace */
extern int ftIdle;		/* set to tune delay loop */
extern int ftScale;		/* set to tune analog inputs */
#ifdef _WIN32
  extern int ftDisable;		/* set to disable/re-enable interrupts */
#endif
extern int ftLoadOut;		/* loadOut during input */

#ifdef _WIN32
  void ftShowState (unsigned short port);
#endif
void ftOutput (unsigned short port, unsigned char byte);
unsigned char ftDigital (unsigned short port);
unsigned ftAnalog (unsigned short port, int nTrigger);

#endif
