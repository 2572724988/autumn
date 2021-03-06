package com.autumn.util.security.sm;

/**
 * 
 * @author 老码农
 *
 *         2018-01-09 15:38:24
 */
public class Sm3 {
	
	public static final byte[] IV = { 0x73, (byte) 0x80, 0x16, 0x6f, 0x49, 0x14, (byte) 0xb2, (byte) 0xb9, 0x17, 0x24,
			0x42, (byte) 0xd7, (byte) 0xda, (byte) 0x8a, 0x06, 0x00, (byte) 0xa9, 0x6f, 0x30, (byte) 0xbc, (byte) 0x16,
			0x31, 0x38, (byte) 0xaa, (byte) 0xe3, (byte) 0x8d, (byte) 0xee, 0x4d, (byte) 0xb0, (byte) 0xfb, 0x0e,
			0x4e };

	public static int[] TJ = new int[64];

	static {
		for (int i = 0; i < SmUtils.INTEGER_LENGTH_16; i++) {
			TJ[i] = 0x79cc4519;
		}
		for (int i = SmUtils.INTEGER_LENGTH_16; i < SmUtils.INTEGER_LENGTH_64; i++) {
			TJ[i] = 0x7a879d8a;
		}
	}

	public static byte[] cf(byte[] vValue, byte[] bValue) {
		int[] v, b;
		v = convert(vValue);
		b = convert(bValue);
		return convert(cf(v, b));
	}

	private static int[] convert(byte[] arr) {
		int[] out = new int[arr.length / SmUtils.INTEGER_LENGTH_4];
		byte[] tmp = new byte[SmUtils.INTEGER_LENGTH_4];
		for (int i = 0; i < arr.length; i += SmUtils.INTEGER_LENGTH_4) {
			System.arraycopy(arr, i, tmp, 0, SmUtils.INTEGER_LENGTH_4);
			out[i / SmUtils.INTEGER_LENGTH_4] = bigEndianByteToInt(tmp);
		}
		return out;
	}

	private static byte[] convert(int[] arr) {
		byte[] out = new byte[arr.length * SmUtils.INTEGER_LENGTH_4];
		byte[] tmp = null;
		for (int i = 0; i < arr.length; i++) {
			tmp = bigEndianIntToByte(arr[i]);
			System.arraycopy(tmp, 0, out, i * SmUtils.INTEGER_LENGTH_4, SmUtils.INTEGER_LENGTH_4);
		}
		return out;
	}

	public static int[] cf(int[] cfv, int[] cfb) {
		int a, b, c, d, e, f, g, h;
		int ss1, ss2, tt1, tt2;
		a = cfv[0];
		b = cfv[1];
		c = cfv[2];
		d = cfv[3];
		e = cfv[4];
		f = cfv[5];
		g = cfv[6];
		h = cfv[7];

		int[][] arr = expand(cfb);
		int[] w = arr[0];
		int[] w1 = arr[1];

		for (int j = 0; j < SmUtils.INTEGER_LENGTH_64; j++) {
			ss1 = (bitCycleLeft(a, 12) + e + bitCycleLeft(TJ[j], j));
			ss1 = bitCycleLeft(ss1, 7);
			ss2 = ss1 ^ bitCycleLeft(a, 12);
			tt1 = ffj(a, b, c, j) + d + ss2 + w1[j];
			tt2 = ggj(e, f, g, j) + h + ss1 + w[j];
			d = c;
			c = bitCycleLeft(b, 9);
			b = a;
			a = tt1;
			h = g;
			g = bitCycleLeft(f, 19);
			f = e;
			e = xorCalc(tt2);
		}
		int[] out = new int[8];
		out[0] = a ^ cfv[0];
		out[1] = b ^ cfv[1];
		out[2] = c ^ cfv[2];
		out[3] = d ^ cfv[3];
		out[4] = e ^ cfv[4];
		out[5] = f ^ cfv[5];
		out[6] = g ^ cfv[6];
		out[7] = h ^ cfv[7];
		return out;
	}

	private static int[][] expand(int[] b) {
		int[] w = new int[SmUtils.INTEGER_LENGTH_68];
		int[] w1 = new int[SmUtils.INTEGER_LENGTH_64];
		System.arraycopy(b, 0, w, 0, b.length);

		for (int i = SmUtils.INTEGER_LENGTH_16; i < SmUtils.INTEGER_LENGTH_68; i++) {
			w[i] = p1(w[i - 16] ^ w[i - 9] ^ bitCycleLeft(w[i - 3], 15)) ^ bitCycleLeft(w[i - 13], 7) ^ w[i - 6];
		}

		for (int i = 0; i < SmUtils.INTEGER_LENGTH_64; i++) {
			w1[i] = w[i] ^ w[i + SmUtils.INTEGER_LENGTH_4];
		}

		return new int[][] { w, w1 };
	}

	private static byte[] bigEndianIntToByte(int num) {
		return back(SmUtils.intToBytes(num));
	}

	private static int bigEndianByteToInt(byte[] bytes) {
		return SmUtils.byteToInt(back(bytes));
	}

	private static int ffj(int x, int y, int z, int j) {
		if (j >= 0 && j <= SmUtils.INTEGER_LENGTH_15) {
			return xor(x, y, z);
		} else {
			return andWithOr(x, y, z);
		}
	}

	private static int ggj(int x, int y, int z, int j) {
		if (j >= 0 && j <= SmUtils.INTEGER_LENGTH_15) {
			return xor(x, y, z);
		} else {
			return andWithNegative(x, y, z);
		}
	}

	/**
	 * 异或运算函数
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 *
	 */
	private static int xor(int x, int y, int z) {
		int tmp = x ^ y ^ z;
		return tmp;
	}

	private static int andWithOr(int x, int y, int z) {
		int tmp = ((x & y) | (x & z) | (y & z));
		return tmp;
	}

	private static int andWithNegative(int x, int y, int z) {
		int tmp = (x & y) | (~x & z);
		return tmp;
	}

	private static int xorCalc(int x) {
		int y = rotateLeft(x, 9);
		y = bitCycleLeft(x, 9);
		int z = rotateLeft(x, 17);
		z = bitCycleLeft(x, 17);
		int t = x ^ y ^ z;
		return t;
	}

	private static int p1(int x) {
		int t = x ^ bitCycleLeft(x, 15) ^ bitCycleLeft(x, 23);
		return t;
	}

	/**
	 * 对最后一个分组字节数据padding
	 * 
	 * @param in
	 * @param bLen
	 *            分组个数
	 * @return
	 */
	public static byte[] padding(byte[] in, int bLen) {
		int k = 448 - (8 * in.length + 1) % 512;
		if (k < 0) {
			k = 960 - (8 * in.length + 1) % 512;
		}
		k += 1;
		byte[] padd = new byte[k / 8];
		padd[0] = (byte) 0x80;
		long n = in.length * 8 + bLen * 512;
		byte[] out = new byte[in.length + k / 8 + 64 / 8];
		int pos = 0;
		System.arraycopy(in, 0, out, 0, in.length);
		pos += in.length;
		System.arraycopy(padd, 0, out, pos, padd.length);
		pos += padd.length;
		byte[] tmp = back(SmUtils.longToBytes(n));
		System.arraycopy(tmp, 0, out, pos, tmp.length);
		return out;
	}

	/**
	 * 字节数组逆序
	 * 
	 * @param in
	 * @return
	 */
	private static byte[] back(byte[] in) {
		byte[] out = new byte[in.length];
		for (int i = 0; i < out.length; i++) {
			out[i] = in[out.length - i - 1];
		}
		return out;
	}

	public static int rotateLeft(int x, int n) {
		return (x << n) | (x >> (32 - n));
	}

	private static int bitCycleLeft(int n, int bitLen) {
		bitLen %= 32;
		byte[] tmp = bigEndianIntToByte(n);
		int byteLen = bitLen / 8;
		int len = bitLen % 8;
		if (byteLen > 0) {
			tmp = byteCycleLeft(tmp, byteLen);
		}

		if (len > 0) {
			tmp = bitSmall8CycleLeft(tmp, len);
		}

		return bigEndianByteToInt(tmp);
	}

	private static byte[] bitSmall8CycleLeft(byte[] in, int len) {
		byte[] tmp = new byte[in.length];
		int t1, t2, t3;
		for (int i = 0; i < tmp.length; i++) {
			t1 = (byte) ((in[i] & 0x000000ff) << len);
			t2 = (byte) ((in[(i + 1) % tmp.length] & 0x000000ff) >> (8 - len));
			t3 = (byte) (t1 | t2);
			tmp[i] = (byte) t3;
		}
		return tmp;
	}

	private static byte[] byteCycleLeft(byte[] in, int byteLen) {
		byte[] tmp = new byte[in.length];
		System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
		System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);
		return tmp;
	}

}
