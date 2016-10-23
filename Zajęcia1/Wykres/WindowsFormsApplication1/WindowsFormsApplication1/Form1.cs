using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        Graphics g;
        Pen pen1 = new System.Drawing.Pen(Color.Blue, 3);
        Pen pen2 = new System.Drawing.Pen(Color.Red, 3);
        float sum;

        public Form1()
        {
            InitializeComponent();
            g = pictureBox1.CreateGraphics();


        }

        private void button1_Click(object sender, EventArgs e)
        {
            int[] outputs = new int[] { 0, 1, 0, 0 };
            Random r = new Random();

            double[] weights = { r.NextDouble(), r.NextDouble(), r.NextDouble() };

            float totalError = 1;
            float learningRate = 1;
            int output;
            int error;



            int[,] input = new int[,] { { 1, 0 }, { 1, 1 }, { 0, 1 }, { 0, 0 } };

            while (totalError > 0.4)
            {
                totalError = 0;

                for (int i = 0; i < 4; i++)
                {
                    output = calculateOutput(input[i, 0], input[i, 1], weights);
                    if (output == 0) pen1.Color = Color.Aqua;
                    else pen1.Color = Color.Red;
                    error = outputs[i] - output;

                    weights[0] += learningRate * error * input[i, 0];
                    weights[1] += learningRate * error * input[i, 1];
                    weights[2] += learningRate * error * 1;

                    totalError += Math.Abs(error);

                    int x = input[i, 0] + input[i, 1];
                    drawPoint(x, sum, Color.Black, g);
                    
                }
            }
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {
            

        }

        public double GetRandomNumber(float minimum, float maximum)
        {
            Random random = new Random();
            return random.NextDouble() * (maximum - minimum) + minimum;
        }

        public void drawPoint(int x, float y, Color clr, Graphics g)
        {
            //x += 90;
            //y += 90;
            //y = 180 - y;

            x *= 120;
            y *= 120;

            g.DrawEllipse(pen1, x, y, 5, 5);

        }

        private void pictureBox1_Paint_1(object sender, PaintEventArgs e)
        {
            Graphics g = e.Graphics;
            SolidBrush b = new SolidBrush(Color.Black);
            g.FillRectangle(b, 180, 0, 1, 360);
            g.DrawLine(pen1, 176, 8, 180, 0);
            g.DrawLine(pen1, 184, 8, 180, 0);

            g.FillRectangle(b, 0, 180, 360, 1);
            g.DrawLine(pen1, 352, 176, 360, 180);
            g.DrawLine(pen1, 352, 184, 360, 180);
        }

        private int calculateOutput(float input1, float input2, double[] weights)
        {
            sum = (float)(input1 * weights[0] + input2 * weights[1] + 1 * weights[2]);
            return (sum >= 0) ? 1 : 0;
        }
        private static int calculateOutput(float input1, double[] weights)
        {
            float sum = (float)(input1 * weights[0] + 1 * weights[1]);
            return (sum >= 0) ? 1 : 0;
        }

        public static void showTab(int[] tab)
        {
            Console.Write("[ ");
            for (int i = 0; i < tab.Length; i++)
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