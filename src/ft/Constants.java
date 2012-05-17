package ft;
/** constants, masks, etc.
  */
public interface Constants {
  /** turn M1.. off */		public static final int OFF	= 0;
  /** turn M1.. on: left */	public static final int LEFT	= 1;
  /** turn M1.. on: right */	public static final int RIGHT	= 2;
  
  /** output: mask */		public static final int M1	=  3;
  /** output: mask */		public static final int M2	= (3 << 2);
  /** output: mask */		public static final int M3	= (3 << 4);
  /** output: mask */		public static final int M4	= (3 << 6);
  /** output: all */		public static final int Mall	= 0xff;

  /** number of motors */	public static final int Mnum	= 4;
  /** shift value */		public static final int Mshift	= 2;

  /** convert index to mask */	public static final int[] M =
						{ 0, M1, M2, M3, M4 };

  /** digital input: mask */	public static final int E1	=  1;
  /** digital input: mask */	public static final int E2	= (1 << 1);
  /** digital input: mask */	public static final int E3	= (1 << 2);
  /** digital input: mask */	public static final int E4	= (1 << 3);
  /** digital input: mask */	public static final int E5	= (1 << 4);
  /** digital input: mask */	public static final int E6	= (1 << 5);
  /** digital input: mask */	public static final int E7	= (1 << 6);
  /** digital input: mask */	public static final int E8	= (1 << 7);
  /** digital input: all */	public static final int Eall	= 0xff;

  /** no. of digital inputs */	public static final int Enum	= 8;
  /** shift value */		public static final int Eshift	= 1;

  /** convert index to mask */	public static final int[] E =
				{ 0, E1, E2, E3, E4, E5, E6, E7, E8 };

  /** analog input: mask */	public static final int EX	=  1;
  /** analog input: mask */	public static final int EY	= (1 << 1);

  /** no. of analog inputs */	public static final int Anum	= 2;
  /** shift value */		public static final int Ashift	= 1;

  /** convert index to mask */	public static final int[] A	= { 0, EX, EY };

  /** no. of cyclers */		public static final int Cnum	= 1;
}