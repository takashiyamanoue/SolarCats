package util;

/**
 * RTP�Ԃ����k(�G���R�[�h)���ꂽ�����f�[�^������Ă��܂�.
 * ����āA���M���̓G���R�[�h�A��M���̓f�R�[�h�������Ȃ��܂�.
 * �ȉ��̃y�C���[�h�t�H�[�}�b�g��encode/decode���\.
 * 
 * - AUDIO/PCMU(G.711 ��-Law)
 * 
 * @author Koetsu Honma
 * @version 1.0
 */
public class EnDecode {

	/**
	 * EnDecode �I�u�W�F�N�g���\�z���܂�.
	 */
	public EnDecode() {
	}

	/**
	 * �����f�[�^���G���R�[�h���A�߂�l�Ƃ��ĕԂ��܂�.
	 * 
	 * @param sample(�G���R�[�h�Ώۂ̃f�[�^)
	 * @return �G���R�[�h���ꂽ�o��
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
	 * �����f�[�^���f�R�[�h���A�߂�l�Ƃ��ĕԂ��܂�.
	 * 
	 * @param ulawByte(�f�R�[�h�Ώۂ̃f�[�^)
	 * @return �f�R�[�h���ꂽ�o��
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
