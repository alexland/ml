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

package common;

import java.util.Random;

public class RefPlane {

	Point[] inputs;
	Line targetLine;
	int[] outputs;
	Random rand = new Random();

	public void generateInputs(int size) {
		inputs = new Point[size];

		for (int i = 0; i < size; i++) {
			inputs[i] = new Point(getSign() * rand.nextDouble(), getSign()
					* rand.nextDouble());
		}
	}

	public void generateTargetLine() {
		double d1 = getSign() * rand.nextDouble();
		double d2 = getSign() * rand.nextDouble();

		while (d1 > d2) {
			d1 = getSign() * rand.nextDouble();
			d2 = getSign() * rand.nextDouble();
		}
		targetLine = new Line(new Point(getSign() * rand.nextDouble(), d1),
				new Point(getSign() * rand.nextDouble(), d2));
	}

	public void generateOutput() {
		outputs = new int[inputs.length];
		int i = 0;
		for (Point p : inputs) {
			if (isLeft(targetLine.getP1(), targetLine.getP2(), p)) {
				outputs[i] = -1;
			} else
				outputs[i] = 1;
			i++;
		}
	}

	private int getSign() {
		return rand.nextBoolean() ? 1 : -1;
	}

	public boolean isLeft(Point a, Point b, Point c) {
		return ((b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a
				.getY()) * (c.getX() - a.getX())) > 0;
	}

	public Point[] getInputs() {
		return inputs;
	}

	public Line getTargetLine() {
		return targetLine;
	}

	public int[] getOutputs() {
		return outputs;
	}

}
