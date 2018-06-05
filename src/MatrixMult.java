
import java.util.*;

public class MatrixMult
{

    public static void main(String[] args)
    {

        for(int i=0; i<15; i++) { // n = # number of times it runs
            int n = (int) Math.pow(2, i);
            System.out.println("Matrix Size: " + n);

            int[][] m1 = MatrixMult.makeMatrix(n);
            int[][] m2 = MatrixMult.makeMatrix(n);
            //MatMult.print("m1", m1);
            //MatMult.print("m2", m2);

            testMults(m1, m2);
            System.out.println();
        }
    }

    public static int[][] makeMatrix(int n)
    {
        int[][] m = new int[n][n];
        Random r1 = new Random();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                m[i][j] = r1.nextInt(9);
            }
        }
        return m;
    }

    private static void testMults(int[][] m1, int[][] m2)
    {
        System.out.print("Classic Time (ms): ");
        long startTime = System.currentTimeMillis();
        int[][] res = MatrixMult.classicalMatrix(m1, m2);
        System.out.println((System.currentTimeMillis() - startTime));
//    MatrixMult.print("Classic", res);


        System.out.print("Divide & Conquer Time: ");
        startTime = System.currentTimeMillis();
        res = MatrixMult.dcMatrix(m1, m2);
        System.out.println((System.currentTimeMillis() - startTime));
//    MatrixMult.print("D&C", res);


        System.out.print("Strassen Time: ");
        startTime = System.currentTimeMillis();
        res = MatrixMult.strassenMatrix(m1, m2);
        System.out.println((System.currentTimeMillis() - startTime));
//    MatrixMult.print("Strassen", res);

    }


    public static int[][] classicalMatrix(int[][] a, int[][] b)
    {
        int result[][] = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++)
            for (int j = 0; j < a.length; j++) {
                result[i][j] = 0;
                for (int k = 0; k < a.length; k++)
                    result[i][j] = result[i][j] + a[i][k] * b[k][j];
            }
        return result;
    }


    public static int[][] dcMatrix(int[][] a, int[][] b)
    {
        int n = a.length;
        int[][] result = new int[n][n];

        if (n == 1)
            result[0][0] = a[0][0] * b[0][0];
        else {
            int[][] a11 = new int[n/2][n/2];
            int[][] a12 = new int[n/2][n/2];
            int[][] a21 = new int[n/2][n/2];
            int[][] a22 = new int[n/2][n/2];
            int[][] b11 = new int[n/2][n/2];
            int[][] b12 = new int[n/2][n/2];
            int[][] b21 = new int[n/2][n/2];
            int[][] b22 = new int[n/2][n/2];

            copyToPart(a, a11, 0, 0);
            copyToPart(a, a12, 0, n/2);
            copyToPart(a, a21, n/2, 0);
            copyToPart(a, a22, n/2, n/2);

            copyToPart(b, b11, 0, 0);
            copyToPart(b, b12, 0, n/2);
            copyToPart(b, b21, n/2, 0);
            copyToPart(b, b22, n/2, n/2);

            int[][] c11 = add( dcMatrix(a11, b11), dcMatrix(a12, b21) );
            int[][] c12 = add( dcMatrix(a11, b12), dcMatrix(a12, b22) );
            int[][] c21 = add( dcMatrix(a21, b11), dcMatrix(a22, b21) );
            int[][] c22 = add( dcMatrix(a21, b12), dcMatrix(a22, b22) );

            copyToWhole(c11, result, 0, 0);
            copyToWhole(c12, result, 0, n/2);
            copyToWhole(c21, result, n/2, 0);
            copyToWhole(c22, result, n/2, n/2);
        }
        return result;
    }

    public static int[][] strassenMatrix(int[][] a, int[][] b)
    {
        int n = a.length;
        int[][] result = new int[n][n];

        if (n == 1)
            result[0][0] = a[0][0] * b[0][0];
        else {
            int[][] a11 = new int[n/2][n/2];
            int[][] a12 = new int[n/2][n/2];
            int[][] a21 = new int[n/2][n/2];
            int[][] a22 = new int[n/2][n/2];
            int[][] b11 = new int[n/2][n/2];
            int[][] b12 = new int[n/2][n/2];
            int[][] b21 = new int[n/2][n/2];
            int[][] b22 = new int[n/2][n/2];

            copyToPart(a, a11, 0, 0);
            copyToPart(a, a12, 0, n/2);
            copyToPart(a, a21, n/2, 0);
            copyToPart(a, a22, n/2, n/2);

            copyToPart(b, b11, 0, 0);
            copyToPart(b, b12, 0, n/2);
            copyToPart(b, b21, n/2, 0);
            copyToPart(b, b22, n/2, n/2);

            int[][] m1 = strassenMatrix(add(a11, a22), add(b11, b22));
            int[][] m2 = strassenMatrix(add(a21, a22), b11);
            int[][] m3 = strassenMatrix(a11, minus(b12, b22));
            int[][] m4 = strassenMatrix(a22, minus(b21, b11));
            int[][] m5 = strassenMatrix(add(a11, a12), b22);
            int[][] m6 = strassenMatrix(minus(a21, a11), add(b11, b12));
            int[][] m7 = strassenMatrix(minus(a12, a22), add(b21, b22));

            int[][] c11 = add(minus(add(m1, m4), m5), m7);
            int[][] c12 = add(m3, m5);
            int[][] c21 = add(m2, m4);
            int[][] c22 = add(minus(add(m1, m3), m2), m6);

            copyToWhole(c11, result, 0, 0);
            copyToWhole(c12, result, 0, n/2);
            copyToWhole(c21, result, n/2, 0);
            copyToWhole(c22, result, n/2, n/2);
        }
        return result;
    }


    public static void copyToPart(int[][] from, int[][] to, int startRow, int startCol)
    {
        for (int i = 0; i < to.length; i++) // row
            for (int j = 0; j < to.length; j++)  // col
                to[i][j] = from[i+startRow][j+startCol];
    }


    public static void copyToWhole(int[][] from, int[][] to, int startRow, int startCol)
    {
        for (int i = 0; i < from.length; i++)
            for (int j = 0; j < from.length; j++)
                to[i+startRow][j+startCol] = from[i][j];
    }


    public static int[][] add(int[][] a, int[][] b)
    {
        int result[][] = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++)
                result[i][j] = a[i][j] + b[i][j];
        }
        return result;
    }

    private static void addRange(int[][] c, int[][] a, int[][] b,
                                 int rowC, int colC)
    {
        for(int i = 0; i < a.length; i++)
            for(int j = 0; j < a.length; j++)
                c[i + rowC][j + colC] = a[i][j] + b[i][j];
    }


    public static int[][] minus(int[][] a, int[][] b)
    {
        int result[][] = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                result[i][j] = a[i][j] - b[i][j];
            }
        }
        return result;
    }


    public static void print(String msg, int m[][])
    {
        System.out.println("------ " + msg + " ------");
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++)
                System.out.print("  " + m[i][j]);
            System.out.println();
        }
        System.out.println("--------------------");
    }

    private static int parseInt(String s)
    {
        if (s == null)
            return 0;
        try {
            return Integer.parseInt(s);
        }
        catch (NumberFormatException ex){
            System.out.println(s + " could not be parsed as an int; using 4");
            return 4;
        }
    }

}