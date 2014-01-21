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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI {

	private static final int X_LEN = 500;
	private static final int Y_LEN = 500;

	JFrame frame;
	DrawPanel panel;

	public GUI(String title) {
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new DrawPanel();
		frame.getContentPane().add(panel);
		frame.setSize(X_LEN, Y_LEN);
		frame.setMinimumSize(new Dimension(X_LEN, Y_LEN));
		frame.setLocation(200, 200);
	}

	public void show() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.pack();
				frame.setVisible(true);

			}
		});
	}

	public void drawInputs(Point[] points) {
		for (Point p : points) {
			panel.addPoint(p);
		}
		frame.repaint();
	}

	public void drawTargetLine(Line l) {

		panel.setTargetLine(l);
		frame.repaint();
	}

	public void drawHypothesisLine(Line l) {

		panel.setHypoLine(l);

		frame.repaint();
	}

	public static void main(String[] args) {
		new GUI("Test").show();
	}

	class DrawPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -3601565066787736514L;
		List<Point> points;
		Line targetLine;
		Line hypoLine;

		public DrawPanel() {
			points = new ArrayList<>();
			setSize(X_LEN, Y_LEN);
			setVisible(true);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.drawRect(0, 0, X_LEN, Y_LEN);
			g2d.setBackground(Color.LIGHT_GRAY);
			g2d.setColor(Color.BLACK);
			drawCoordinateSystem(g2d);

			for (Point p : points) {
				double xPos = (p.getX() * X_LEN / 2);
				double yPos = (p.getY() * Y_LEN / 2);
				g2d.fillOval((int) xPos + X_LEN / 2, (int) yPos + Y_LEN / 2, 5,
						5);
			}
			drawLine(g2d);

		}

		private void drawCoordinateSystem(Graphics2D g) {
			g.drawLine(0, Y_LEN / 2, X_LEN, Y_LEN / 2);
			g.drawLine(X_LEN / 2, 0, X_LEN / 2, Y_LEN);
			g.drawOval(X_LEN / 2, Y_LEN / 2, 3, 3);
		}

		void addPoint(Point p) {
			points.add(p);
		}

		void setTargetLine(Line l) {
			this.targetLine = l;
		}

		void setHypoLine(Line l) {
			this.hypoLine = l;
		}

		void drawLine(Graphics2D g) {
			g.setColor(Color.BLUE);
			double xPos1 = (targetLine.getP1().getX() * X_LEN / 2) + X_LEN / 2;
			double yPos1 = (targetLine.getP1().getY() * Y_LEN / 2) + Y_LEN / 2;

			double xPos2 = (targetLine.getP2().getX() * X_LEN / 2) + X_LEN / 2;
			double yPos2 = (targetLine.getP2().getY() * Y_LEN / 2) + Y_LEN / 2;

			g.drawLine((int) xPos1, (int) yPos1, (int) xPos2, (int) yPos2);

			xPos1 = (hypoLine.getP1().getX() * X_LEN / 2) + X_LEN / 2;
			yPos1 = (hypoLine.getP1().getY() * Y_LEN / 2) + Y_LEN / 2;

			xPos2 = (hypoLine.getP2().getX() * X_LEN / 2) + X_LEN / 2;
			yPos2 = (hypoLine.getP2().getY() * Y_LEN / 2) + Y_LEN / 2;

			g.setColor(Color.RED);
			g.drawLine((int) xPos1, (int) yPos1, (int) xPos2, (int) yPos2);
		}
	}
}
