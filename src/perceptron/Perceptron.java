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

public class Perceptron {

	public static final int ATTR_COUNT = 2;
	
	private GUI gui;
	static Point[] inputs;
	static int[] outputs;
	static Line targetLine;
	private boolean noPaint;
	
	Random rand = new Random();
	
	public Perceptron(GUI gui, boolean showGui) {
		this.gui = gui;
		this.noPaint = !showGui;
	}
	
	public void generateInputs(int size) {
		inputs = new Point[size];
		
		for(int i=0; i<size; i++) {
			inputs[i] = new Point(getSign() * rand.nextDouble(), getSign() * rand.nextDouble());
		}
	}
	
	public void generateTargetLine() {
		double d1 = getSign() * rand.nextDouble();
		double d2 = getSign() * rand.nextDouble();
		
		while(d1 > d2) {
			d1 = getSign() * rand.nextDouble();
			d2 = getSign() * rand.nextDouble();
		}
		targetLine = new Line(new Point(getSign() * rand.nextDouble(), d1),
				new Point(getSign() * rand.nextDouble(), d2));
	}
	
	private void determineOutput() {
		outputs = new int[inputs.length];
		int i = 0;
		for(Point p : inputs) {
			if(isLeft(targetLine.getP1(), targetLine.getP2(), p)) {
				outputs[i] = -1;
			} else
				outputs[i] = 1;
			i++;
		}
	}

	private int getSign() {
		return rand.nextBoolean() ? 1 : -1;
	}
	
	public boolean isLeft(Point a, Point b, Point c){
	     return ((b.getX() - a.getX())*(c.getY() - a.getY()) - (b.getY() - a.getY())*(c.getX() - a.getX())) > 0;
	}

	private int solve() {
		double[] weights = new double[ATTR_COUNT + 1];
		Arrays.fill(weights, 0.1);
		
		Random r = new Random();
		int count = 0;
		for(int i=0; i<10000; i++) {
			int next = r.nextInt(inputs.length);
			Point xn = inputs[next];
			
			double sign = weights[0] + weights[1] * xn.getX() + weights[2] * xn.getY();
			
			if((sign < 0 && outputs[next] > 0) || (sign > 0 && outputs[next] < 0)) {
				// misclassified
				weights[0] += outputs[next];
				weights[1] += outputs[next] * xn.getX();
				weights[2] += outputs[next] * xn.getY();
				count++;
				
				double xx = -(weights[0]/weights[1]);
				double yy = -(weights[0]/weights[2]);
				if(!noPaint) {
					gui.drawHypothesisLine(new Line(new Point(xx, 0), new Point(0, yy)));
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
		if(args.length == 1) {
			if("nogui".equals(args[0]))
				showGui = false;
		}
		Perceptron p = new Perceptron(g, showGui);
		
		p.generateInputs(100);
		p.generateTargetLine();
		p.determineOutput();

		
		g.show();
		g.drawInputs(inputs);
		g.drawTargetLine(targetLine);
		g.show();

		p.solve();
	}

}

class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
}

class Line {
	private Point p1;
	private Point p2;
	
	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Point getP1() {
		return p1;
	}
	
	public Point getP2() {
		return p2;
	}
	
	@Override
	public String toString() {
		return p1 + " - " + p2;
	}
}
