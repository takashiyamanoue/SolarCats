/** native part of Serial|Parallel Input|Output Stream.
  */
#include "ft.comm.h"
#ifdef _WIN32
#include "parallel.h"
#include <windows.h>
#endif

#ifdef _WIN32
static struct serial { char* name; HANDLE handle; } serial [4];
static struct parallel { char* name; unsigned short base; 
                         unsigned char command; } parallel [4];

static void initialize (JNIEnv* env) {
  jclass clazz = (*env)->FindClass(env, "ft/comm/Driver");
  if (clazz) {
    jfieldID fid;
    fid = (*env)->GetStaticFieldID(env, clazz, "traceFlags", "I");
    if (fid) ftTraceFlags = (*env)->GetStaticIntField(env, clazz, fid);
    fid = (*env)->GetStaticFieldID(env, clazz, "idle", "I");
    if (fid) ftIdle = (*env)->GetStaticIntField(env, clazz, fid);
    fid = (*env)->GetStaticFieldID(env, clazz, "scale", "I");
    if (fid) ftScale = (*env)->GetStaticIntField(env, clazz, fid);
    fid = (*env)->GetStaticFieldID(env, clazz, "disable", "Z");
    if (fid) ftDisable = (*env)->GetStaticBooleanField(env, clazz, fid);
    fid = (*env)->GetStaticFieldID(env, clazz, "loadOut", "Z");
    if (fid) ftLoadOut = (*env)->GetStaticBooleanField(env, clazz, fid);
    if (ftTraceFlags)
      printf("traceFlags 0x%02x idle %d scale %d disable %d loadOut %d\n",
                 ftTraceFlags, ftIdle, ftScale, ftDisable, ftLoadOut);
  }
}
#endif

/* ft.comm.SerialOutputStream */
/* int nativeConstructor (String portName) throws IOException */
jint JNICALL Java_ft_comm_SerialOutputStream_nativeConstructor
    (JNIEnv* env, jobject this, jstring _port) {
  const char* port = (*env)->GetStringUTFChars(env, _port, NULL);
  int i, n = sizeof serial / sizeof serial[0];
  for (i = 0; i < n; ++ i) {
    if (!serial[i].name)
      serial[i].name = _strdup(port), serial[i].handle = INVALID_HANDLE_VALUE;
    else if (strcmp(port, serial[i].name))
      continue;
    break;
  }
  if (i >= n) {
   (*env)->ThrowNew(env,
    (*env)->FindClass(env, "java/lang/IllegalArgumentException"), "bad portid");
    i = -1;
  } else {
#ifdef _WIN32
    if (serial[i].handle == INVALID_HANDLE_VALUE) {
      serial[i].handle = CreateFile(port,
        GENERIC_READ | GENERIC_WRITE,
        0,    /* comm devices must be opened w/exclusive-access */
        NULL, /* no security attrs */
        OPEN_EXISTING, /* comm devices must use OPEN_EXISTING */
        0,    /* not overlapped I/O */
        NULL  /* hTemplate must be NULL for comm devices */);
      if (serial[i].handle == INVALID_HANDLE_VALUE)
        (*env)->ThrowNew(env, (*env)->FindClass(env, "java/io/IOException"),
		    					"cannot open port");
      else {
        COMMTIMEOUTS timeouts = { 0, 0, 0, 0, 0 };
        DCB dcb;

        if (!SetCommTimeouts(serial[i].handle, &timeouts))
            (*env)->ThrowNew(env, (*env)->FindClass(env, "java/io/IOException"),
		    					"cannot clear timeouts");
        else if (!GetCommState(serial[i].handle, &dcb))
          (*env)->ThrowNew(env, (*env)->FindClass(env, "java/io/IOException"),
		    					"cannot get mode");
        else {
          dcb.BaudRate = 9600;
          dcb.ByteSize = 8;
          dcb.Parity = NOPARITY;
          dcb.StopBits = ONESTOPBIT;
          if (!SetCommState(serial[i].handle, &dcb))
            (*env)->ThrowNew(env, (*env)->FindClass(env, "java/io/IOException"),
		    					"cannot set mode");
        }
      }
    }
#endif
  }
  (*env)->ReleaseStringUTFChars(env, _port, port);
  return (jint)i;
}  
/* void close (int portHandle) throws IOException */
void JNICALL Java_ft_comm_SerialOutputStream_close
    (JNIEnv* env, jobject this, jint portHandle) {
#ifdef _WIN32
  if (serial[portHandle].handle != INVALID_HANDLE_VALUE) {
    if (!CloseHandle(serial[portHandle].handle))
      (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "close error");
    serial[portHandle].handle = INVALID_HANDLE_VALUE;
  }
#endif
}
/* int write (int portHandle, byte[] buf, int offset, int len)
						throws IOException */
jint JNICALL Java_ft_comm_SerialOutputStream_write
    (JNIEnv* env, jobject this, jint portHandle,
				jbyteArray _buf, jint offset, jint len) {
  jbyte* buf = (*env)->GetByteArrayElements(env, _buf, NULL); 
  jint result;
#ifdef _WIN32
  if (!WriteFile(serial[portHandle].handle, buf+offset, len, &result, NULL)
      || result != len) {
    (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "write error");
    result = -1;
  }
#endif
  (*env)->ReleaseByteArrayElements(env, _buf, buf, JNI_ABORT);
  return result;
}

/* ft.comm.SerialInputStream */
/* int nativeConstructor (String port) throws IOException */
jint JNICALL Java_ft_comm_SerialInputStream_nativeConstructor
    (JNIEnv* env, jobject this, jstring port) {
  return Java_ft_comm_SerialOutputStream_nativeConstructor(env, this, port);
}
/* void close (int portHandle) throws IOException */
void JNICALL Java_ft_comm_SerialInputStream_close
    (JNIEnv* env, jobject this, jint portHandle) {
  Java_ft_comm_SerialOutputStream_close(env, this, portHandle);
}
/* int read (int portHandle) throws IOException */
jint JNICALL Java_ft_comm_SerialInputStream_read__I
    (JNIEnv* env, jobject this, jint portHandle) {
  unsigned char buf[1];
  jint result;
#ifdef _WIN32
  if (!ReadFile(serial[portHandle].handle, buf, 1, &result, NULL))
    (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "read error");
  result = result <= 0 ? -1 : buf[0];
#endif
  return result;
}
/* int read (int portHandle, byte[] buf, int offset, int len)
						throws IOException */
JNIEXPORT jint JNICALL Java_ft_comm_SerialInputStream_read__I_3BII
    (JNIEnv* env, jobject this, jint portHandle,
				jbyteArray _buf, jint offset, jint len) {
  jbyte* buf = (*env)->GetByteArrayElements(env, _buf, NULL); 
  jint result;
#ifdef _WIN32
  if (!ReadFile(serial[portHandle].handle, buf+offset, len, &result, NULL))
    (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "read error");
  if (result <= 0) result = -1;
#endif
  (*env)->ReleaseByteArrayElements(env, _buf, buf, 0);
  return result;
}

/* ft.comm.ParallelOutputStream */
/* int nativeConstructor (String portName) throws IOException */
jint JNICALL Java_ft_comm_ParallelOutputStream_nativeConstructor
    (JNIEnv* env, jobject this, jstring _port) {
  char* port = (char*)(*env)->GetStringUTFChars(env, _port, NULL);
  int i, n = sizeof parallel / sizeof parallel[0];
  for (i = 0; i < n; ++ i) {
    if (!parallel[i].name)
      parallel[i].name = _strdup(port),
      parallel[i].base = (unsigned short)strtol(port, &port, 16);
    else if (strcmp(port, parallel[i].name))
      continue;
    break;
  }
  if (i >= n) {
   (*env)->ThrowNew(env,
    (*env)->FindClass(env, "java/lang/IllegalArgumentException"), "bad portid");
    i = -1;
  }
  (*env)->ReleaseStringUTFChars(env, _port, port);
  return (jint)i;
}  
/* int write (int portHandle, byte[] buf, int offset, int len)
						throws IOException */
jint JNICALL Java_ft_comm_ParallelOutputStream_write
    (JNIEnv* env, jobject this, jint portHandle,
				jbyteArray _buf, jint offset, jint len) {
  jbyte* buf = (*env)->GetByteArrayElements(env, _buf, NULL); 
  jint result;
#ifdef _WIN32
  if (len != 2) {
    (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "write error");
    result = -1;
  } else {
    parallel[portHandle].command = buf[offset];
    ftOutput(parallel[portHandle].base, buf[offset+1]);
    result = len;
  }
#endif
  (*env)->ReleaseByteArrayElements(env, _buf, buf, JNI_ABORT);
  return result;
}

/* ft.comm.ParallelInputStream */
/* int nativeConstructor (String portName) throws IOException */
jint JNICALL Java_ft_comm_ParallelInputStream_nativeConstructor
    (JNIEnv* env, jobject this, jstring port) {
  int result = Java_ft_comm_ParallelOutputStream_nativeConstructor(env, this, port);
#ifdef _WIN32
  initialize(env);
  if (result >= 0)
    ftShowState(parallel[result].base);
#endif
  return result;
}
/* int read (int portHandle) */
jint JNICALL Java_ft_comm_ParallelInputStream_read__I
    (JNIEnv* env, jobject this, jint portHandle) {
#ifdef _WIN32
  return ftDigital(parallel[portHandle].base);
#endif
}
/* int read (int portHandle, byte[] buf, int offset, int len)
						throws IOException */
JNIEXPORT jint JNICALL Java_ft_comm_ParallelInputStream_read__I_3BII
    (JNIEnv* env, jobject this, jint portHandle,
				jbyteArray _buf, jint offset, jint len) {
  jbyte* buf = (*env)->GetByteArrayElements(env, _buf, NULL); 
  jint result;
#ifdef _WIN32
  if (len != 3) {
    (*env)->ThrowNew(env,
              (*env)->FindClass(env, "java/io/IOException"), "read error");
    result = -1;
  } else {
    buf[offset] = ftDigital(parallel[portHandle].base);
    result = ftAnalog(parallel[portHandle].base, parallel[portHandle].command == 0xC9);
    buf[offset+1] = result>>8 & 255;
    buf[offset+2] = result & 255;
    result = 3;
  }
#endif
  (*env)->ReleaseByteArrayElements(env, _buf, buf, 0);
  return result;
}
