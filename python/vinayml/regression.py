"""
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
"""
__author__ = 'Vinay Penmatsa'

from numpy import linalg, ones, hstack, dot, array as n_arr

class LinearRegression(object):

    def __init__(self, inputs, targets):
        bias = -ones((inputs.shape[0], 1))
        # bias added input
        eff_inputs = hstack((bias, inputs))
        print eff_inputs
        # parameter vector B: B = ((X^T)X)^(-1)(X^T)y
        print eff_inputs.shape
        print targets.shape

        self._parameters = dot(dot(linalg.inv(dot(eff_inputs.transpose(), eff_inputs)), eff_inputs.transpose()), targets)

    def predict(self, inputs):
        bias = -ones((inputs.shape[0], 1))
        eff_inputs = hstack((bias, inputs))
        return dot(eff_inputs, self._parameters)

    def parameters(self):
        return self._parameters

if __name__ == '__main__':
    ins = n_arr([[0,0],[0,1],[1,0],[1,1]])
    ts = n_arr([[0],[1],[1],[1]])
    lin_reg = LinearRegression(ins, ts)
    print lin_reg.parameters()
