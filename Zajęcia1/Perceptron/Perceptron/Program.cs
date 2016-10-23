using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Perceptron
{
    class Program
    {
        static void Main(string[] args)
        {
            int[] outputs = new int[] { 0, 1, 0, 0 };
            Console.Write("Podaj operacje:\n  (1) - AND (Domyślnie)\n  (2) - OR\n  (3) - NOT\nWybór: ");
            int choose = Convert.ToInt32(Console.ReadLine());

            switch (choose)
            {
                case 1:
                    outputs = new int[] { 0, 1, 0, 0 };
                    break;
                case 2:
                    outputs = new int[] { 1, 1, 1, 0 };
                    break;
                case 3:
                    outputs = new int[] { 1, 1, 0, 0 };
                    break;
                default:
                    outputs = new int[] { 0, 1, 0, 0 };
                    break;
            }
            

            Random r = new Random();

            //Synapsy
            double[] weights = { r.NextDouble(), r.NextDouble(), r.NextDouble() };

            double totalError = 1;
            double learningRate = 1;

            if (choose == 1 || choose == 2)
            {
                int[,] input = new int[,] { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 0, 0 } };
                //showTab(input);

                while (totalError > 0)
                {
                    totalError = 0;

                    for (int i = 0; i < 4; i++)
                    {
                        int output = calculateOutput(input[i, 0], input[i, 1], weights);
                        int error = outputs[i] - output;

                        weights[0] += learningRate * error * input[i, 0];
                        weights[1] += learningRate * error * input[i, 1];
                        weights[2] += learningRate * error * 1;

                        totalError += Math.Abs(error);
                    }
                }

                Console.WriteLine("Wynik: ");
                for (int i = 0; i < 4; i++)
                {
                    Console.WriteLine(calculateOutput(input[i, 0], input[i, 1], weights));
                }
            }

            else
            {
                int[] input = new int[] { 0, 0, 1, 1 };
                showTab(input);
                while (totalError > 0)
                {
                    totalError = 0;

                    for (int i = 0; i < 4; i++)
                    {
                        int output = calculateOutput(input[i], weights);
                        int error = outputs[i] - output;

                        weights[0] += learningRate * error * input[i];
                        weights[1] += learningRate * error * 1;

                        totalError += Math.Abs(error);
                    }
                }

                Console.WriteLine("Wynik: ");
                for (int i = 0; i < 4; i++)
                {
                    Console.WriteLine(calculateOutput(input[i], weights));
                }
            }

            Console.ReadLine();


        }

        private static int calculateOutput(double input1, double input2, double[] weights)
        {
            double sum = input1 * weights[0] + input2 * weights[1] + 1 * weights[2];
            return (sum >= 0) ? 1 : 0;
        }
        private static int calculateOutput(double input1, double[] weights)
        {
            double sum = input1 * weights[0] + 1 * weights[1];
            return (sum >= 0) ? 1 : 0;
        }
        
        public static void showTab(int[] tab)
        {
            Console.Write("[ ");
            for (int i =0; i< tab.Length; i++)
            {
                Console.Write(i + ", ");
            }
            Console.WriteLine("]");
        }

        public static void showTab(int[,] tab)
        {
            Console.Write("[ ");
            for (int i = 0; i < tab.Length; i++)
            {
                Console.Write("{ ");

                for (int j = 0; j < 2; j++)
                    Console.Write(j + ", ");

                Console.Write("}");
            }
            Console.WriteLine("]");
        }
    }
}
