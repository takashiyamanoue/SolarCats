/** (partial) C wrapper for Fossil API.
    TC 2.01.
    axel@uos.de 3/2000.
  */
#ifndef FOSSIL_H
#define FOSSIL_H
#include "types.h"

typedef enum { EXT = 0, COM = 1 } FossilPort;

typedef enum {				/* bit encoded */
  DATA = 0x01,				/* receiver buffer has data */
  OVERRUN = 0x02,			/* receiver overrun */
  NOTFULL = 0x20,			/* output buffer not full */
  EMPTY	= 0x40				/* output buffer empty */
    } FossilStatus;

typedef struct {
  FossilStatus (*setup)(FossilPort port);	/* ft parameters */
  FossilStatus (*put)(FossilPort port, Byte byte);
  Byte (*get)(FossilPort port);
  Boolean (*init)(FossilPort port);
  void (*deinit)(FossilPort port);
  void (*flowControl)(FossilPort port);	/* disables */
  void (*flush)(FossilPort port);
  void (*purgeOutput)(FossilPort port);
  void (*purgeInput)(FossilPort port);
  Word (*read)(FossilPort port, Byte * buf, Word len);
  Word (*write)(FossilPort port, const Byte * buf, Word len);
    } Fossil;

extern Fossil fossil;
#endif
