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

import java.util.Arrays;
import java.util.Random;

import Jama.Matrix;

import common.GUI;
import common.Line;
import common.Point;
import common.RefPlane;

public class LinearRegression {

	public static final int ATTR_COUNT = 2;

	private GUI gui;
	private boolean noPaint;
	private static RefPlane plane;

	Random rand = new Random();

	public LinearRegression(GUI gui, boolean showGui) {
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

		// 2 attributes, 100 points 100 x 3 matrix
		double[][] xMatrix = new double[100][3];

		int i = 0;

		for (Point p : plane.getInputs()) {
			xMatrix[i][0] = 1;
			xMatrix[i][1] = p.getX();
			xMatrix[i][2] = p.getY();
			i++;
		}

		Matrix xm = new Matrix(xMatrix, 100, 3);
		Matrix xmt = xm.transpose();
		Matrix xmprod = xmt.times(xm);
		Matrix xmprodInv = xmprod.inverse();
		Matrix xmDag = xmprodInv.times(xmt);
		// xmDag.print(10, 3);

		int[] ys = plane.getOutputs();
		double[] ysd = new double[ys.length];
		for (int n = 0; n < ys.length; n++) {
			ysd[n] = (double) ys[n];
		}

		Matrix ym = new Matrix(ysd, 100);
		Matrix weightM = xmDag.times(ym);
		weightM.print(10, 3);

		double xx = -(weightM.get(0, 0) / weightM.get(1, 0));
		double yy = -(weightM.get(0, 0) / weightM.get(2, 0));
		if (!noPaint) {
			gui.drawHypothesisLine(new Line(new Point(xx, 0), new Point(0, yy)));
		}

		return 0;
	}

	public static void main(String[] args) {
		GUI g = new GUI("Linear Regression Learning");

		boolean showGui = true;
		if (args.length == 1) {
			if ("nogui".equals(args[0]))
				showGui = false;
		}
		LinearRegression p = new LinearRegression(g, showGui);

		p.generateRefPlane();

		g.show();
		g.drawInputs(plane.getInputs());
		g.drawTargetLine(plane.getTargetLine());
		g.show();

		p.solve();
	}

}
