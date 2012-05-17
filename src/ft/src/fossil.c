/** (partial) C wrapper for Fossil API.
    TC 2.01.
    axel@uos.de 3/2000.
  */
#include <dos.h>
#include "fossil.h"

#define FOSSIL		0x14	/* fossil service interrupt */

#define SETUP		0x00	/* set serial port params */
# define B9600		0xE0	/* bits 7..5 */
# define NONE		0x00	/* bits 4..3 */
# define STOP1		0x00	/* bit 2 */
# define BIT8		0x03	/* bits 1..0 */
#define PUT		0x01	/* put byte */
#define GET		0x02	/* get byte */
#define INIT		0x04	/* initialize fossil driver */
#define DEINIT		0x05	/* deinitialize fossil driver */
#define FLUSH		0x08	/* flush output buffer */
#define PURGEOUTPUT	0x09	/* purge output buffer */
#define PURGEINPUT	0x0A	/* purge input buffer */
#define FLOWCONTROL	0x0F	/* enable/disable flow control */
# define CTSRTS		0x02	/* cts/rts flow control */
#define READ		0x18	/* read block of data (does not wait?) */
#define WRITE		0x19	/* write block of data */

#define req(port, service) union REGS regs; regs.h.ah = service; regs.x.dx = port; int86(FOSSIL, &regs, &regs)
#define req0(port, service) req(port, service); return regs.x.ax
#define req1(port, service, info) union REGS regs; regs.h.ah = service; regs.h.al = info; regs.x.dx = port; int86(FOSSIL, &regs, &regs); return regs.x.ax
#define reqb(port, service, buf, len) union REGS regs; struct SREGS sregs; regs.h.ah = service; regs.x.cx = len; regs.x.dx = port; sregs.es = FP_SEG(buf); regs.x.di = FP_OFF(buf); int86x(FOSSIL, &regs, &regs, &sregs); return regs.x.ax

static FossilStatus setup (FossilPort port) {
  req1(port, SETUP, B9600 | NONE | STOP1 | BIT8) >> 8;
}

static FossilStatus put (FossilPort port, Byte byte) {
  req1(port, PUT, byte) >> 8;
}

static Byte get (FossilPort port) {
  req0(port, GET);
}

static Boolean init (FossilPort port) {
  req0(port, INIT) == 0x1954;
}

static void deinit (FossilPort port) {
  req(port, DEINIT);
}

static void flowControl (FossilPort port) {
  req(port, FLOWCONTROL);
}

static void flush (FossilPort port) {
  req(port, FLUSH);
}

static void purgeOutput (FossilPort port) {
  req(port, PURGEOUTPUT);
}

static void purgeInput (FossilPort port) {
  req(port, PURGEINPUT);
}

static Word read (FossilPort port, Byte * buf, Word len) {
  reqb(port, READ, buf, len);
}

static Word write (FossilPort port, const Byte * buf, Word len) {
  reqb(port, WRITE, buf, len);
}

Fossil fossil = {
  setup,
  put,
  get,
  init,
  deinit,
  flowControl,
  flush,
  purgeOutput,
  purgeInput,
  read,
  write
    };
