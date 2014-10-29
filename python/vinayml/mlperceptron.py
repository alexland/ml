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

from numpy import *


class MultiLayerPerceptron(object):
    """
    A generic neural net implementation where the number of
    hidden layers and layer size is configurable.
    """
    def __init__(self, inputs, targets, hidden_layers=None):
        """
        :param inputs:
        :param targets:
        :param hidden_layers: array of layer sizes [2,3] mean two hidden layers
                with sizes 2 and 3 respectively
        :return:
        """
        self.input_sizes = [shape(inputs)[1]]
        self.target_count = shape(targets)[1]
        self.input_sizes.extend(hidden_layers)

        # assign initial random weights in range [-1/sqrt(n), 1/sqrt(n)]
        self.weights = []
        for i in range(len(self.input_sizes) - 1):
            self.weights.append((random.rand(self.input_sizes[i] + 1, self.input_sizes[i + 1]) - 0.5) * 2 / sqrt(
                self.input_sizes[i]))
        self.weights.append(
            (random.rand(self.input_sizes[len(self.input_sizes) - 1] + 1, self.target_count) - 0.5) * 2 / sqrt(
                self.input_sizes[len(self.input_sizes) - 1]))
        self.inputs = inputs
        self.targets = targets
        print self.weights
        print self.inputs
        print self.targets

    def get_biased(self, arr):
        bias = -ones((arr.shape[0], 1))
        biased = hstack((bias, arr))
        return biased

    def train(self):
        eff_inputs = self.get_biased(self.inputs)
        eff_targets = self.targets.copy()

        # to randomize inputs in each iteration
        change = range(self.inputs.shape[0])

        # iterations
        for n in range(5000):

            # shuffle
            random.shuffle(change)
            eff_inputs = eff_inputs[change, :]
            eff_targets = eff_targets[change, :]

            # feed forward
            active_input = eff_inputs
            hidden_layer_inputs = []

            for i in range(len(self.weights)):
                next_inputs = dot(active_input, self.weights[i])
                g_next_inputs = 1.0 / (1.0 + exp(-next_inputs))  # sigmoid activation
                if i != len(self.weights) - 1:
                    active_input = self.get_biased(g_next_inputs)
                    hidden_layer_inputs.append(active_input)
                else:
                    outputs = g_next_inputs

            o_error = eff_targets - outputs

            if (mod(n,100)==0):
                o_sq_error = .5 * dot(o_error.transpose(), o_error)  # sum-of-squares error
                print "Iteration: ", n, " Error: ", o_sq_error

            delta_error = o_error * outputs * (1.0 - outputs)
            delta_errors = [delta_error]
            # back propagate error
            first = True
            for i in range(len(hidden_layer_inputs))[::-1]:
                delta_error = hidden_layer_inputs[i] * (1.0 - hidden_layer_inputs[i]) * dot(delta_error, transpose(
                    self.weights[i + 1]))
                delta_error = delta_error[:,1:delta_error.shape[1]]
                delta_errors.insert(0, delta_error)

            # update weights
            hidden_layer_inputs.insert(0, eff_inputs) # just to make it easier for looping
            for i in range(len(self.weights)):
                self.weights[i] += .25 * dot(transpose(hidden_layer_inputs[i]), delta_errors[i])

        print eff_targets
        print outputs



if __name__ == "__main__":
    inputs = array([[0, 0], [0, 1], [1, 0], [1, 1]])
    targets = array([[0], [1], [1], [1]])
    p = MultiLayerPerceptron(inputs, targets, [3,2,2])
    p.train()