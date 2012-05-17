package util;

/**
 * RTP間を圧縮(エンコード)された音声データが流れています.
 * よって、送信側はエンコード、受信側はデコードをおこないます.
 * 以下のペイロードフォーマットのencode/decodeが可能.
 * 
 * - AUDIO/PCMU(G.711 μ-Law)
 * 
 * @author Koetsu Honma
 * @version 1.0
 */
public class EnDecode {

	/**
	 * EnDecode オブジェクトを構築します.
	 */
	public EnDecode() {
	}

	/**
	 * 引数データをエンコードし、戻り値として返します.
	 * 
	 * @param sample(エンコード対象のデータ)
	 * @return エンコードされた出力
	 */
	public byte encode( short sample ) {
		final short BIAS = 132;	// 0x84
		final short CLIP = 32635;	// 32767-BIAS
		// Convert sample to sign-magnitude
		int sign = sample & 0x8000;
		if(sign != 0){
			sample = (short)-sample;
			sign = 0x80;
		}
		if(sample > CLIP) sample = CLIP;
		sample += BIAS;
		int exp;
		short temp = (short)(sample << 1);
		for(exp=7; exp > 0; exp--) {
			if((temp & 0x8000) != 0) break;
			temp = (short)(temp << 1);
		}
		temp = (short)(sample >> (exp + 3));
		int mantis = temp & 0x000f;
		byte ulawByte = (byte)(sign | (exp << 4) | mantis);
		return (byte)~ulawByte;
	}

	/**
	 * 引数データをデコードし、戻り値として返します.
	 * 
	 * @param ulawByte(デコード対象のデータ)
	 * @return デコードされた出力
	 */
	public short decode( byte ulawByte ) {
		ulawByte = (byte)(~ulawByte);
		int sign = ulawByte & 0x80;
		int exp = (ulawByte & 0x70) >> 4;
		int mantis = ulawByte & 0xf;
		int rawValue = (mantis << (12 - 8 + (exp - 1))) + (132 << exp) - 132;
		return (short)((sign != 0) ? - rawValue : rawValue);
	}
}
