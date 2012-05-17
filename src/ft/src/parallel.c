/** manage [and test] ft parallel interface.
#ifdef _WIN32
    MS VC++ 4.2.
#endif
#ifdef SC12
    TC 2.01.
    Based on DK40_IO (C) 1999 by BECK IPC GmbH.
    This does, however, not work: the signal levels are not compatible.
#endif
    axel@uos.de 3/2000.
  */
#include <stdio.h>
#include <stdlib.h>
#include "parallel.h"

#ifdef _WIN32
# include <conio.h>
# include <windows.h>
  int _enable(), _disable();	/* we hope */
#endif
#ifdef SC12
# include <dos.h>
# include "rtos.h"
# define _inp(port)		inportb(port)
  static void _outp (unsigned short port, unsigned char byte);
  static void _outp (unsigned short port, unsigned char byte) {
    byte &= ~busy; byte |= busy & inportb(port); outportb(port, byte);
  }
#endif

#define loadOut  1		/* set byte into M1..4 */
#define loadIn   (1 << 1)	/* get byte from E1..8 */
#define dataOut  (1 << 2)	/* set output bit */
#define clock    (1 << 3)	/* set clock pulse */
#define triggerX (1 << 4)	/* get pulse from EX */
#define triggerY (1 << 5)	/* get pulse from EY */

#ifdef _WIN32
#define busy     0x80		/* base+1: printer is busy */
#endif
#ifdef SC12
#define busy     (1 << 6)	/* input on pin 6(!) */
#endif

#ifdef DEBUG
# define trace(mask, x)	((ftTraceFlags&mask) && printf x)
#else
# define trace(mask, x)
#endif

#ifdef _WIN32
int ftDisable = 0;
#endif
int ftIdle = 1;
int ftScale = 4;
int ftTraceFlags;
int ftLoadOut;

static int idle (int depth) {
  int result = 0;
#ifdef _WIN32  /* Sleep() is too coarse. */
  for (srand(123456789); depth > 0; -- depth) result = rand();
#endif
#ifdef SC12
  rtos.sleep(depth);
#endif
  return result;
}

#ifdef _WIN32
void ftShowState (unsigned short port) {
  if (ftTraceFlags) {
    int s = _inp((unsigned short)(port+1));
    int c = _inp((unsigned short)(port+2));
    printf("%03x status", port);
    if (s & 0x80) fputs(" /BSY", stdout);
    if (s & 0x40) fputs(" /ACK", stdout);
    if (s & 0x20) fputs(" PAP", stdout);
    if (s & 0x10) fputs(" OFON", stdout);
    if (s & 0x04) fputs(" /FEN", stdout);
    fputs(" control", stdout);
    if (c & 0x10) fputs(" IRQ", stdout);
    if (c & 0x08) fputs(" DSL", stdout);
    if (c & 0x04) fputs(" /INI", stdout);
    if (c & 0x02) fputs(" ALF", stdout);
    if (c & 0x01) fputs(" STR", stdout);
    putchar('\n');
  }
}
#endif

/*
  loadOut high	-+              +-
          low	 +--------------+

  loadIn  high
          low	------------------

  dataOut high	 +?........+ *8
          low	-+?........+    +-

  clock   high	-+    +idle+ *8 +-
          low	 +idle+

  trigger high	------------------
          low

  busy    high
          low   ------------------
*/

void ftOutput (unsigned short port, unsigned char byte) {
#ifdef _WIN32
  int enabled = ftDisable && _disable();
#endif
  int data;
  int bit = 8;

  trace(1, ("output %03x", port));
  while (bit-- > 0) {
    data = triggerX|triggerY;
    if (byte & (1<<bit)) data |= dataOut;
    trace(2, (" >%02x", data));
    _outp(port, data); idle(ftIdle);
    data |= clock;
    trace(4, (" >%02x", data));
    _outp(port, data); idle(ftIdle);
  }
  data = triggerX|triggerY|loadOut|clock;
  trace(2, (" >%02x", data));
  _outp(port, data);
  trace(1, (" %02x\n", byte));
#ifdef _WIN32
  if (enabled) _enable();
#endif
}

/*
  loadOut high	<-according to documentation
          low   <-according to experiment

  loadIn  high	 +----------+
          low	-+          +-----------

  dataOut high
          low	------------------------

  clock   high	-+    +idle-+ *8 +    +-
          low	 +idle+     +    +idle+

  trigger high	------------------------
          low

  busy (input)                ?  *8
*/

unsigned char ftDigital (unsigned short port) {
  unsigned char result = 0;
#ifdef _WIN32
  unsigned short status = port+1;
  int enabled = ftDisable && _disable();
#endif
#ifdef SC12
  unsigned short status = port;
#endif
  int bit = 8;
  int data = triggerX|triggerY|loadIn;
  if (ftLoadOut) data |= loadOut;
  
  trace(8, ("digital %03x", port));
  trace(32, (" >%02x", data));
  _outp(port, data); idle(ftIdle);
  data |= clock;
  trace(32, (" >%02x", data));
  _outp(port, data);
  while (bit-- > 0) {
    idle(ftIdle);
    result |= ((_inp(status) & busy) != 0) << bit;
    trace(16, (" <%02x", result));
    data = triggerX|triggerY;
    if (ftLoadOut) data |= loadOut;
    trace(32, (" >%02x", data));
    _outp(port, data); idle(ftIdle);
    data |= clock;
    trace(32, (" >%02x", data));
    _outp(port, data);
  }
  trace(8, (" %02x\n", result));
#ifdef _WIN32
  if (enabled) _enable();
#endif
  return result;
}

/*
  loadOut high	<-according to documentation
          low   <-according to experiment

  loadIn  high
          low	-----------------------

  dataOut high
          low	-----------------------

  clock   high	-----------------------
          low

  trigger high	-+    +----------------
          low	 +idle+

  busy    low	----+	               +-
          high	    +--<--measured-->+
*/

unsigned ftAnalog (unsigned short port, int nTrigger) {
  unsigned result = 0;
#ifdef _WIN32
  unsigned short status = port+1;
  int enabled = ftDisable && _disable();
#endif
#ifdef SC12
  unsigned short status = port;
#endif
  int data = (nTrigger ? triggerX : triggerY)|clock;
  if (ftLoadOut) data |= loadOut;

  trace(64, ("analog %03x", port));
  trace(128, (" >%02x", data));
  _outp(port, data); idle(ftIdle);
  data = triggerX|triggerY|clock;
  if (ftLoadOut) data |= loadOut;
  trace(128, (" >%02x", data));
  _outp(port, data);
  while (_inp(status) & busy) ++ result, idle(ftScale);
  trace(64, (" %u\n", result));
#ifdef _WIN32
  if (enabled) _enable();
#endif
  return result;
}

#ifdef TEST
#ifdef _WIN32
# include <sys/timeb.h>
#endif
#include "main.h"

MAIN {
  int count = 1, c, n;
  int mode = 1;					/* always check digital in */
  unsigned char out = 0;			/* output only if != 0 */
#ifdef _WIN32
  unsigned short port = 0x378;
  struct _timeb start, now;
#endif
#ifdef SC12
  unsigned short port = DK40;
  unsigned long start, now;
#endif

  OPT
  ARG 'c': PARM					/* -c count */
    count = atoi(*argv); NEXTOPT
#ifdef _WIN32
  ARG 'd':					/* -d: use disable/enable */
    ftDisable = 1; continue;
#endif
  ARG 'e':					/* -e: don't sample digital */
    mode &= ~1;
  ARG 'f':					/* -f: set loadOut for input */
    ftLoadOut = 1;
  ARG 'i': PARM					/* -i idle */
    ftIdle = (int)strtol(*argv, NULL, 0); NEXTOPT
  ARG 'l': PARM					/* -l 1..4: M1..4 left */
    n = 2 * (atoi(*argv)-1);
    if (n >= 0 && n <= 6) out |= 1 << n; NEXTOPT
  ARG 'r': PARM					/* -r 1..4: M1..4 right */
    n = 2 * (atoi(*argv)-1);
    if (n >= 0 && n <= 6) out |= 2 << n; NEXTOPT
#ifdef _WIN32
  ARG 'p': PARM					/* -p port */
    port = (int)strtol(*argv, NULL, 0); NEXTOPT
#endif
  ARG 's': PARM					/* -s scale */
    ftScale = (int)strtol(*argv, NULL, 0); NEXTOPT
  ARG 't': PARM					/* -t traceFlags */
    ftTraceFlags = (int)strtol(*argv, NULL, 0); NEXTOPT
  ARG 'x':					/* -x: also check EX */
    mode |= 2;
  ARG 'y':					/* -y: also check EY */
    mode |= 4;
  OTHER
    fputs(
#ifdef _WIN32
      "usage: parallel [-defxy] [-c count] [-i idle] [-l|r 1..4] [-p port]"
      					" [-s scale] [-t traceFlags]\n",
#endif
#ifdef SC12
      "usage: parallel [-efxy] [-c count] [-i idle] [-l|r 1..4]"
      					" [-s scale] [-t traceFlags]\n",
#endif
			stderr), exit(1);
  ENDOPT

#ifdef _WIN32
  ftShowState(port);
  _ftime(&start);
#endif
#ifdef SC12
  rtos.getTicks(&start);
#endif
  for (c = 0; c < count; ++ c) {
    unsigned char uc; static unsigned char e1_8;
    unsigned u; static unsigned ex, ey;
    
    if (out) ftOutput(port, out);
    if (mode & 1)
      if ((uc = ftDigital(port)) != e1_8)
        printf("E1..8 %02x\n", uc), e1_8 = uc;
    if (mode & 2)
      if ((u = ftAnalog(port, 0)) != ex)
        printf("EX %u\n", u), ex = u;
    if (mode & 4)
      if ((u = ftAnalog(port, 1)) != ey)
        printf("EY %u\n", u), ey = u;
  }
#ifdef _WIN32
  _ftime(&now);
  now.time -= start.time;
  now.millitm -= start.millitm; now.millitm += 1000 * (unsigned short)now.time;
  printf("%u msec\n", now.millitm);
#endif
#ifdef SC12
  rtos.getTicks(&now);
  printf("%u msec\n", now-start);
#endif
  return 0;
}
#endif
