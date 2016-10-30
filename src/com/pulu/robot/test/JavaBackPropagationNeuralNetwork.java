package com.pulu.robot.test;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

/**
 * JAVA ������������
 * 
 * @author kj021320 �� codeby 2008.12.10
 * 
 */
public class JavaBackPropagationNeuralNetwork {
	/**
	 * ��Ԫ
	 */
	public class Neuron {
		HashMap<Integer, Link> target = new HashMap<Integer, Link>();// ����������Ԫ��
		HashMap<Integer, Link> source = new HashMap<Integer, Link>();// ��������Ԫ���ӵ�
		double data = 0.0;

		public Link sourceGet(int index) {
			return source.get(index);
		}

		public Link targetGet(int index) {
			return target.get(index);
		}

		public boolean targetContains(Link l) {
			return target.containsValue(l);
		}

		public boolean sourceContains(Link l) {
			return source.containsValue(l);
		}

		public Link sourceLink(int index, Link l) {
			if (l.linker != this) {
				l.setLinker(this);
			}
			return source.put(index, l);
		}

		public Link targetLink(int index, Link l) {
			if (l.owner != this) {
				l.setOwner(this);
			}
			return target.put(index, l);
		}
	}

	/**
	 * ����
	 */
	public class Link {
		Neuron owner;

		public void setOwner(Neuron o) {
			owner = o;
			if (!o.targetContains(this)) {
				o.targetLink(o.target.size(), this);
			}
		}

		public Link() {
			weight = rand(-1, 1);
		}

		public void setLinker(Neuron o) {
			linker = o;
			if (!o.sourceContains(this)) {
				o.sourceLink(o.source.size(), this);
			}
		}

		@Override
		public String toString() {
			return super.toString() + " weight:" + weight;
		}

		Neuron linker;
		double weight;
	}

	Random random = new Random();

	{
		random.setSeed(System.nanoTime());
	}

	Neuron[] inputnode; // �������Ԫ
	Neuron[] hiddennode; // ��������Ԫ
	Neuron[] outputnode; // �������Ԫ
	double learnrate;// ѧϰ�ٶ�
	double threshold;// ��ֵ,��������

	private final int inputCount;
	private final int hiddenCount;
	private final int outputCount;

	/**
	 * 
	 * @param input
	 *            ��������Ԫ����
	 * @param hidden
	 *            ���������Ԫ����
	 * @param output
	 *            ��������Ԫ�ĸ���
	 */
	public JavaBackPropagationNeuralNetwork(int input, int hidden, int output) {
		inputCount = input;
		hiddenCount = hidden;
		outputCount = output;
		build();
	}

	public void reBuildNeuralNetwork() {
		build();
	}

	private void build() {
		inputnode = new Neuron[inputCount + 1];
		hiddennode = new Neuron[hiddenCount];
		outputnode = new Neuron[outputCount];
		initNeurons(inputnode);
		initNeurons(hiddennode);
		initNeurons(outputnode);
		makeLink(inputnode, hiddennode);
		makeLink(hiddennode, outputnode);
	}

	/**
	 * ˼������
	 * 
	 * @param inputs
	 *            ǰ�����񾭸�������ĸ����� -1~1֮��
	 * @return ˼����Ľ��,�������˵��񾭸��������ÿ������Ϊ-1~1֮��
	 */
	public double[] thinking(double[] inputs) {
		/** ������ӳ�䵽ǰ�������Ԫ���� */
		makeNeuron(inputnode, inputs);
		/** ͨ��ÿ��������Ȩ�� �����ز���㵽����������ֵ */
		thinking();
		/** ��������ֵӳΪreturn��double���� */
		return makeMatrix();
	}

	public double[][] batchThinking(double[][] inputs) {
		double[][] ret = new double[inputs.length][];
		for (int i = 0; i < inputs.length; i++) {
			makeNeuron(inputnode, inputs[i]);
			thinking();
			ret[i] = makeMatrix();
		}
		return ret;
	}

	/**
	 * ����ѵ��
	 * 
	 * @param inputs
	 * @param outputs
	 * @param learnrate
	 *            ѧϰ��ϸ��
	 * @param error
	 *            �������
	 * @param maxlearn
	 *            ���ѧϰ����
	 * @return �Ƿ����ѵ��
	 */
	public boolean train(double[][] inputs, double[][] outputs, double learnrate, double error, int maxlearn) {
		this.learnrate = learnrate;
		this.threshold = error;
		boolean complete = false;
		int count = 0;
		double e = 0;
		while (!complete) {
			count++;
			e = 0;
			complete = true;
			for (int size = 0; size < inputs.length; size++) {
				e += learn(inputs[size], outputs[size]);
				if (e > threshold) {
					complete = false;
				}
			}
			if (count >= maxlearn) {
				System.err.println("convergence fail  error:" + e);
				return false;
			}
		}
		System.out.println("convergence success    error:" + e);
		return true;
	}

	/**
	 * ����ѧϰ
	 * 
	 * @param input
	 * @param output
	 * @return ���
	 */
	private double learn(double[] input, double[] output) {
		/** ������ӳ�䵽ǰ�������Ԫ���� */
		makeNeuron(inputnode, input);
		/** ͨ��ÿ��������Ȩ�� �����ز���㵽����������ֵ */
		thinking();
		/** ������ */
		return evolutionComputing(output);
	}

	private void thinking() {
		transmitComputing(hiddennode);
		transmitComputing(outputnode);
	}

	/**
	 * ��Ԫ�������
	 * 
	 * @param ns
	 */
	private void transmitComputing(Neuron[] ns) {
		for (Neuron ne : ns) {
			double sum = 0.0;
			Set<Entry<Integer, Link>> linkset = ne.source.entrySet();
			for (Entry<Integer, Link> ent : linkset) {
				Link l = ent.getValue();
				Neuron n = l.owner;
				// �������ص�,������Ԫ*��Ȩ��
				sum += n.data * l.weight;
			}
			// ������Ϻ�ͨ�� S�ͼ���������ݴ洢�����ز���񾭽ڵ���
			ne.data = sigmoid(sum);
		}
	}

	/**
	 * �����ݶ��½��� ������ delta����(��������ֲ����ŵ����޷���������)
	 * 
	 * @param datas
	 * @return
	 */
	private double evolutionComputing(double[] datas) {
		double[] output_deltaDatas = new double[outputnode.length];
		double totalError = 0.0;
		for (int i = 0; i < outputnode.length; i++) {
			/**
			 * Erri = Ti �C Oi O is the predicted output T is the correct output
			 * ��i = Erri * g��(ini) g�� is the derivative of the activation
			 * function g
			 */
			output_deltaDatas[i] = (datas[i] - outputnode[i].data) * sigmoidDerivative(datas[i]);
		}

		double[] hidden_deltaDatas = new double[hiddennode.length];
		for (int i = 0; i < hiddennode.length; i++) {
			/**
			 * ��j = g��(inj) * ��i(Wj,i * ��i)
			 */
			double error = 0.0;
			Set<Entry<Integer, Link>> linkSet = hiddennode[i].target.entrySet();
			for (Entry<Integer, Link> ent : linkSet) {
				error += output_deltaDatas[ent.getKey()] * ent.getValue().weight;
			}
			hidden_deltaDatas[i] = sigmoidDerivative(hiddennode[i].data) * error;
		}
		/**
		 * Wj,i = Wj,i + �� * Hj * ��i Hj is the activation of the hidden unit
		 */
		for (int i = 0; i < hiddennode.length; i++) {
			Set<Entry<Integer, Link>> linkSet = hiddennode[i].target.entrySet();
			for (Entry<Integer, Link> ent : linkSet) {
				Link hidden2output = ent.getValue();
				hidden2output.weight += output_deltaDatas[ent.getKey()] * hiddennode[ent.getKey()].data * learnrate;
				// System.out.println("hidden2output:"+hidden2output);
			}
		}
		// System.out.println();
		/**
		 * Wk,j = Wk,j + �� * Ik * ��j Ik is the activation of the input unit
		 */
		for (int i = 0; i < inputnode.length; i++) {
			Set<Entry<Integer, Link>> linkSet = inputnode[i].target.entrySet();
			for (Entry<Integer, Link> ent : linkSet) {
				Link input2hidden = ent.getValue();
				input2hidden.weight += hidden_deltaDatas[ent.getKey()] * inputnode[i].data * learnrate;
				// System.out.println("inputnode[i].data:"+inputnode[i].data+"input2hidden:"+input2hidden);
			}
		}
		// System.out.println();
		/**
		 * E = 1/2 ��i((Ti �C Oi)^2)
		 */
		for (int i = 0; i < outputnode.length; i++) {
			double temp = outputnode[i].data - datas[i];
			totalError += temp * temp;
		}
		return totalError * 0.5;
	}

	/**
	 * ������ӳ�䵽ÿ����Ԫ����
	 * 
	 * @param neurons
	 * @param datas
	 */
	private void makeNeuron(Neuron[] neurons, double[] datas) {
		for (int len = 0; len < neurons.length; len++) {
			if (len >= datas.length) {
				neurons[len].data = 1.0;
			} else {
				neurons[len].data = datas[len];
			}
		}
	}

	/**
	 * ��output����Ԫ����ӳ��Ϊ����
	 * 
	 * @return
	 */
	private double[] makeMatrix() {
		double[] temp = new double[outputnode.length];
		for (int i = 0; i < outputnode.length; i++) {
			temp[i] = outputnode[i].data;
		}
		return temp;
	}

	private void initNeurons(Neuron[] startns) {
		for (int lenN = 0; lenN < startns.length; lenN++) {
			if (startns[lenN] == null) {
				startns[lenN] = new Neuron();
			}
		}
	}

	/**
	 * �����ǻ��ཻ������
	 * 
	 * @param startns
	 * @param endns
	 */
	private void makeLink(Neuron[] startns, Neuron[] endns) {
		for (int lenN = 0; lenN < startns.length; lenN++) {
			for (int len = 0; len < endns.length; len++) {
				Link target = startns[lenN].targetGet(len);
				if (target == null) {
					target = new Link();
					startns[lenN].targetLink(len, target);
				}
				target.setLinker(endns[len]);
			}
		}
	}

	/**
	 * ������S�ͼ����.����Ŀ���ǰ��������ݶ�2ֵ��
	 * 
	 * @param x
	 * @return
	 */
	private double sigmoid(double x) {
		return Math.tanh(x);
	}

	/*
	 * calculate a random number where: a <= rand < b def rand(a, b): return
	 * (b-a)*random.random() + a
	 */
	private double rand(double min, double max) {
		return (max - min) * random.nextDouble() + min;
	}

	// derivative of our sigmoid function
	private double sigmoidDerivative(double y) {
		// return (1.0 - sigmoid(y)) * sigmoid(y);
		// return 1.0-y*y;
		return 1.0 - sigmoid(y) * y;
	}

	/**
	 * @param args
	 * @throws Throwable
	 */
	public static void main(String[] args) throws Throwable {
		// ����һ�� ������������
		JavaBackPropagationNeuralNetwork jbpn = new JavaBackPropagationNeuralNetwork(2, 4, 1);

		// ѵ��XOR
		while (!jbpn.train(
				new double[][] { new double[] { -1, -1 }, new double[] { 1, 1 }, new double[] { -1, 1 },
						new double[] { 1, -1 } }, // ���Ϊ����ֵ
				new double[][] { new double[] { -1 }, new double[] { -1 }, new double[] { 1 }, new double[] { 1 } }, // ����Ǽලָ�����
				0.3, 0.05, 1000)) {
			jbpn.reBuildNeuralNetwork();
		}
		// ˼��
		double[] res = jbpn.thinking(new double[] { -1, -1 });
		for (double s : res) {
			System.out.println("thinking:" + s);
		}
		// ����˼��
		double[][] ress = jbpn.batchThinking(new double[][] { new double[] { -0.8, -0.9 }, new double[] { 0.7, 0.3 },
				new double[] { -.6, .85 }, new double[] { 1, -1 } });
		for (double[] s : ress) {
			for (double d : s) {
				System.out.print("batchThinking:" + d + "    ");
			}
			System.out.println();
		}

	}

}