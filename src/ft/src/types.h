/** types for APIs.
    TC 2.01.
    axel@uos.de 3/2000.
  */
#ifndef TYPES_H
#define TYPES_H

#ifndef _WIN32
#if sizeof(short) != 2 || sizeof(int) != 2 || sizeof(long) != 4
  ERROR basic sizes invalid
#endif
#endif

typedef unsigned char Byte;
typedef short Word;
typedef enum { FALSE = 0, TRUE = 1 } Boolean;

typedef union {
  struct { Byte low, high; } byte;
  Word word;
    } Memory;

#endif
