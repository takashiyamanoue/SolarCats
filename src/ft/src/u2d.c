/** TC 2.01 seems to require \r\n */
#include <stdio.h>

main () {
  char buf [8192];
  while (gets(buf)) printf("%s\r\n", buf);
}
