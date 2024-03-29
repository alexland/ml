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
package perceptron;

import java.util.Arrays;
import java.util.Random;

import common.GUI;
import common.Line;
import common.Point;
import common.RefPlane;

public class Perceptron {

	public static final int ATTR_COUNT = 2;

	private GUI gui;
	private boolean noPaint;
	private static RefPlane plane;

	Random rand = new Random();

	public Perceptron(GUI gui, boolean showGui) {
		this.gui = gui;
		this.noPaint = !showGui;
	}

	private void generateRefPlane() {
		plane = new RefPlane();
		plane.generateInputs(100);
		plane.generateTargetLine();
		plane.generateOutput();
	}

	private int solve() {
		double[] weights = new double[ATTR_COUNT + 1];
		Arrays.fill(weights, 0.1);

		Random r = new Random();
		int count = 0;
		for (int i = 0; i < 10000; i++) {
			int next = r.nextInt(plane.getInputs().length);
			Point xn = plane.getInputs()[next];

			double sign = weights[0] + weights[1] * xn.getX() + weights[2]
					* xn.getY();

			if ((sign < 0 && plane.getOutputs()[next] > 0)
					|| (sign > 0 && plane.getOutputs()[next] < 0)) {
				// misclassified
				weights[0] += plane.getOutputs()[next];
				weights[1] += plane.getOutputs()[next] * xn.getX();
				weights[2] += plane.getOutputs()[next] * xn.getY();
				count++;

				double xx = -(weights[0] / weights[1]);
				double yy = -(weights[0] / weights[2]);
				if (!noPaint) {
					gui.drawHypothesisLine(new Line(new Point(xx, 0),
							new Point(0, yy)));
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
					}
				}
			}

		}

		return count;
	}

	public static void main(String[] args) {
		GUI g = new GUI("Perceptron Learning");

		boolean showGui = true;
		if (args.length == 1) {
			if ("nogui".equals(args[0]))
				showGui = false;
		}
		Perceptron p = new Perceptron(g, showGui);

		p.generateRefPlane();

		g.show();
		g.drawInputs(plane.getInputs());
		g.drawTargetLine(plane.getTargetLine());
		g.show();

		p.solve();
	}

}
