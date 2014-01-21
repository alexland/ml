/*
The MIT License (MIT)

Copyright (c) 2014 Vinay Penmatsa

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

 */

package hoeffding;

import java.util.Arrays;
import java.util.Random;

public class CoinFlip {
	
	private double v_1 = 0.0;
	private double v_rand = 0.0;
	private double v_min = 0.0;
	
	// heads - true, tails - false
	boolean[][] flips = new boolean[1000][10];
	int[] headCount = new int[1000];
	
	public CoinFlip(Random rand) {
		Arrays.fill(headCount, 0);
				
		int minCount = Integer.MAX_VALUE;
		for(int i=0; i<1000; i++) {
			for(int j=0; j<10; j++) {
				boolean nb = rand.nextBoolean();
				flips[i][j] = nb;
				if(nb)
					headCount[i] ++;
			}
			if(headCount[i] < minCount) {
				minCount = headCount[i];
			}
		}
		
		int c_rand = rand.nextInt(1000);
		boolean[] c_rand_tosses = flips[c_rand];
		for(int i=0; i<10; i++) {
			if(c_rand_tosses[i]) v_rand += 1.0; 
		}
		v_rand /= 10.0;
		
		for(int i=0; i<10; i++) {
			if(flips[0][i]) v_1 += 1.0; 
		}
		v_1 /= 10.0;
		
		v_min = minCount/10.0;
	}
	
	
	
	
	public double getV_1() {
		return v_1;
	}




	public double getV_rand() {
		return v_rand;
	}




	public double getV_min() {
		return v_min;
	}




	public static void main(String[] args) {
		double avg_v_1 = 0.0;
		double avg_v_rand = 0.0;
		double avg_v_min = 0.0;
		
		Random r = new Random();
		// 100000 experiments
		for(int i=0;i<100000; i++) {
			CoinFlip cf = new CoinFlip(r);
			avg_v_1 += cf.getV_1();
			avg_v_rand += cf.getV_rand();
			avg_v_min += cf.getV_min();
		}
		
		avg_v_1 /= 100000.0;
		avg_v_rand /= 100000.0;
		avg_v_min /= 100000.0;
		
		System.out.println(avg_v_1 + ", " + avg_v_rand + ", " + avg_v_min);
		
		// v_1 and v_rand should satisfy hoefding's inequality: epsilon ~ .001
	}
}
