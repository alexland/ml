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

package regression;

import java.util.Random;

import Jama.Matrix;

public class RegressionProb81 {

	Random rand;
	double[][] x;
	int[] y;
	double[] weights;
	double[] results;

	public RegressionProb81(Random rand) {
		this.rand = rand;
		x = new double[1000][6];
		weights = new double[6];
		results = new double[1000];
		y = new int[1000];
		generateData();
	}

	public void generateData() {

		for (int i = 0; i < 1000; i++) {
			x[i][0] = 1;
			double x1 = getSign() * rand.nextDouble();
			double x2 = getSign() * rand.nextDouble();
			x[i][1] = x1;
			x[i][2] = x2;
			x[i][3] = x1 * x2;
			x[i][4] = Math.pow(x1, 2);
			x[i][5] = Math.pow(x2, 2);
			y[i] = (Math.pow(x[i][1], 2) + Math.pow(x[i][2], 2) - 0.6) >= 0 ? 1
					: -1;
		}

		// generate noise for 10%
		/*
		 * for (int i = 0; i < 100; i++) { int r = rand.nextInt(100); y[r] =
		 * -y[r]; }
		 */

	}

	private int getSign() {
		return rand.nextBoolean() ? 1 : -1;
	}

	public void regressionWithoutTransformation() {

		Matrix m = new Matrix(x, 1000, 6);
		Matrix mTrans = m.transpose();
		Matrix mDag = mTrans.times(m).inverse().times(mTrans);
		double[] ysd = new double[y.length];
		for (int n = 0; n < y.length; n++) {
			ysd[n] = (double) y[n];
		}
		Matrix ym = new Matrix(ysd, 1000);
		Matrix weightM = mDag.times(ym);

		weightM.print(10, 3);

		for (int l = 0; l < weightM.getRowDimension(); l++) {
			weights[l] = weightM.get(l, 0);
		}

		Matrix g = m.times(weightM);
		for (int k = 0; k < 1000; k++) {
			results[k] = g.get(k, 0);
		}

	}

	public double getError() {

		int error = 0;
		for (int i = 0; i < 1000; i++) {
			if (getSign(results[i]) != y[i])
				error++;
		}

		return (error * 1.0) / 1000;
	}

	private int getSign(double d) {
		return d >= 0.0 ? 1 : -1;
	}

	public static void main(String[] args) {
		RegressionProb81 r2 = new RegressionProb81(new Random());
		r2.regressionWithoutTransformation();
		System.out.println(r2.getError());
	}
}
