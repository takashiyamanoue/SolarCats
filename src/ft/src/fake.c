/** fake for FOSSIL API to test speed.
    MS VC++ 4.2.
    axel@uos.de 3/2000.
  */
#include "fossil.h"
#include <windows.h>

static HANDLE handle;

static FossilStatus setup (FossilPort port) {
  return 0;
}
static Byte get (FossilPort port) {
  unsigned char buf[1];
  int result;
  ReadFile(handle, buf, 1, &result, NULL);
  return result <= 0 ? -1 : buf[0];
}
static Boolean init (FossilPort port) {
  handle = CreateFile("COM1",
    GENERIC_READ | GENERIC_WRITE,
    0,    /* comm devices must be opened w/exclusive-access */
    NULL, /* no security attrs */
    OPEN_EXISTING, /* comm devices must use OPEN_EXISTING */
    0,    /* not overlapped I/O */
    NULL  /* hTemplate must be NULL for comm devices */);
  if (handle == INVALID_HANDLE_VALUE) return FALSE;
  else {
    COMMTIMEOUTS timeouts = { 0, 0, 0, 0, 0 };
    DCB dcb;

    if (!SetCommTimeouts(handle, &timeouts)) return FALSE;
    else if (!GetCommState(handle, &dcb)) return FALSE;
    else {
      dcb.BaudRate = 9600;
      dcb.ByteSize = 8;
      dcb.Parity = NOPARITY;
      dcb.StopBits = ONESTOPBIT;
      if (!SetCommState(handle, &dcb)) return FALSE;
      return TRUE;
    }
  }
}
static void deinit (FossilPort port) {
  CloseHandle(handle);
}
static void flowControl (FossilPort port) {
}
static void purgeOutput (FossilPort port) {
}
static void purgeInput (FossilPort port) {
}
static Word write (FossilPort port, const Byte * buf, Word len) {
  int result;
  return !WriteFile(handle, buf, len, &result, NULL)
         || result != len ? -1 : result;
}

Fossil fossil = {
  setup,
  NULL,
  get,
  init,
  deinit,
  flowControl,
  NULL,
  purgeOutput,
  purgeInput,
  NULL,
  write
    };
