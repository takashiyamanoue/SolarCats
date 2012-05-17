/** test ft serial interface.
#ifdef _WIN32
    MS VC++ 4.2.
#endif
#ifdef SC12
    TC 2.01.
#endif
    axel@uos.de 3/2000.
  */
#include <stdio.h>
#include <stdlib.h>
#include "fossil.h"
#include "main.h"
#ifdef _WIN32
# include <sys/timeb.h>
#endif
#ifdef SC12
#include "rtos.h"
#endif

MAIN {
  int count = 1, c, n;
  Byte outBuf[] = { 0xc5, 0 };
  Byte byte, e1_8 = 0;
  Word ex = 0, ey = 0;
  Memory val;
#ifdef _WIN32
  struct _timeb start, now;
#endif
#ifdef SC12
  unsigned long start, now;
#endif
  
  OPT
  ARG 'c': PARM					/* -c count */
    count = atoi(*argv); NEXTOPT
  ARG 'd':					/* -d digital */
    outBuf[0] = 0xc1;
  ARG 'l': PARM					/* -l 1..4: M1..4 left */
    n = 2 * (atoi(*argv)-1);
    if (n >= 0 && n <= 6) outBuf[1] |= 1 << n; NEXTOPT
  ARG 'r': PARM					/* -r 1..4: M1..4 right */
    n = 2 * (atoi(*argv)-1);
    if (n >= 0 && n <= 6) outBuf[1] |= 2 << n; NEXTOPT
  OTHER
    fputs("usage: serial [-d] [-c count] [-l|r 1..4]\n", stderr);
  ENDOPT
  
  if (!fossil.init(EXT)) fputs("cannot get EXT port\n", stderr), exit(1);
  fossil.setup(EXT);
  fossil.flowControl(EXT);
  fossil.purgeOutput(EXT); fossil.purgeInput(EXT);

#ifdef _WIN32
  _ftime(&start);
#endif
#ifdef SC12
  rtos.getTicks(&start);
#endif
  for (c = 0; c < count; ++ c) {
    if (fossil.write(EXT, outBuf, 2) != 2)
      fputs("output error\n", stderr), exit(1);
    switch (outBuf[0]) {
    case 0xc1:
      if ((byte = fossil.get(EXT)) != e1_8) printf("E1..8 %02x\n", e1_8 = byte);
      break;
    case 0xc5:
      if ((byte = fossil.get(EXT)) != e1_8) printf("E1..8 %02x\n", e1_8 = byte);
      val.byte.high = fossil.get(EXT); val.byte.low = fossil.get(EXT);
      if (val.word != ex) printf("EX %d\n", ex = val.word);
      break;
    case 0xc9:
      if ((byte = fossil.get(EXT)) != e1_8) printf("E1..8 %02x\n", e1_8 = byte);
      val.byte.high = fossil.get(EXT); val.byte.low = fossil.get(EXT);
      if (val.word != ey) printf("EY %d\n", ey = val.word);
      break;
    default:
      printf("outBuf error 0x%02x", outBuf[0]); exit(1);
    }
  }
#ifdef _WIN32
  _ftime(&now);
  now.time -= start.time;
  now.millitm -= start.millitm; now.millitm += 1000 * (unsigned short)now.time;
  printf("%d in %u msec, %d Hz\n",
			count, now.millitm, count*1000 / now.millitm);
#endif
#ifdef SC12
  rtos.getTicks(&now);
  printf("%d in %u msec, %d Hz\n", count, now-start, count*1000 / (now-start));
#endif
  
  fossil.deinit(EXT);
  return 0;
}
