package ftcomputing.robo;
// ---------------------------------------------------------------------------------------------------
//
// JavaFish.java                                                                Ulrich Müller 09.10.08
//                                                                              PCS1700      JBuilder5
// Basisklasse mit native Deklarationen für javaFish40.DLL
//     javaFish40.DLL ist eine Wrapper.DLL, die umFish40.DLL javakonform 1:1 kapselt
//     zusätzlich enthalten sind einige Win32-API Funktionen (escape, getTickCount, sleep)
//     Basis ist umFish40.DLL v4.3.75
//     Unterstützt werden RF Datalink RouteThrough, ROBO Connect Box und die Explorer Sensoren
//
// ---------------------------------------------------------------------------------------------------

public class JavaFish {
  public native int jrOpenInterfaceUSB(int ifTyp, int SerialNr);
  public native int jrOpenInterfaceUSBdis(int ifTyp, int SerialNr, int DistanceMode);
  public native int jrOpenInterfaceCOM(int ifTyp, int ComNr, int AnalogZyklen);
  public native int jrCloseInterface(int iHandle);
  public native int jrGetInput(int iHandle, int InputNr);
  public native int jrGetInputs(int iHandle);
  public native int jrGetAnalog(int iHandle, int AnalogNr);
  public native int jrGetIRKey(int iHandle, int Code, int KeyNr);
  public native int jrGetVoltage(int iHandle, int VoltNr);
  public native int jrSetMotor(int iHandle, int MotNr, int Dir);
  public native int jrSetMotorEx(int iHandle, int MotNr, int Dir, int Speed);
  public native int jrGetMotors(int iHandle);
  public native int jrSetMotors(int iHandle, int MotorStatus);
  public native int jrSetMotorsEx(int iHandle, int MotorStatus, int SpeedStatus, int SpeedStatus16);
  public native int jrGetModeStatus(int iHandle, int MotNr);
  public native int jrSetModeStatus(int iHandle, int MotNr, int Mode);
  public native int jrSetLamp(int iHandle, int LampNr, int OnOff);
  public native int jrSetLampEx(int iHandle, int LampNr, int OnOff, int Power);
  public native int jrRobMotor(int iHandle, int MotNr, int Dir, int Speed, int ICount);
  public native int jrRobMotors(int iHandle, int MotorStatus, int SpeedStatus, int SpeedStatus16,
                                              int ModeStatus);
  public native int jrGetCounter(int iHandle, int CounterNr);
  public native int jrSetCounter(int iHandle, int CounterNr, int ICount);
  public native int jrClearCounters(int iHandle);
  public native int jrGetDistanceValue(int iHandle, int SensorNr);
  public native int jrGetActDeviceType(int iHandle);
  public native int jrGetActDeviceSerialNr(int iHandle);
  public native int jrGetActDeviceFirmwareNr(int iHandle);

  public static native int  escape();
  public static native int  getTickCount();
  public static native void sleep(int MilliSec);

  static {
    try {
      System.loadLibrary("javaFish40");
    }
    catch (UnsatisfiedLinkError ex) {
      System.out.println("javaFish40.DLL konnte nicht geladen werden");
    }
  }
}