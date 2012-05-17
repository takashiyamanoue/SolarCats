#ifndef _MAIN_H
#define _MAIN_H

/* TC 2.01 does not understand continuation lines?? */

#define MAIN int main (int argc, char * argv [])

/* precondition argv[1..argc-1] are the arguments to be decoded */
/* ...and argc is count of arguments plus one */

#define OPT while (-- argc > 0) { if (** ++ argv == '-') switch (* ++ *argv) { case 0: -- *argv; break; case '-': if (! (*argv)[1]) { ++ argv, -- argc; break; } default: do { switch (** argv) { case 0:

#define ARG continue; case

/* assert -f **argv is the flag */

#define PARM if (* ++ *argv) ; else if (-- argc > 0) ++ argv; else { -- *argv, ++ argc; break; }

/* assert -f value *argv is the value */

#define NEXTOPT if (! ** argv) -- *argv; else while ((*argv)[1]) ++ *argv;
 
#define OTHER continue; }

/* assert -? **argv is the unknown flag */
/* ...or flag without value */

#define ENDOPT } while (* ++ *argv); continue; } break; }

/* assert */
/* - */
/* anything, once -- has been seen */
/* argc is count of arguments not yet decoded */
/* if argc >= 1 then argv[0..argc-1] are the strings */

#endif
