package Raster2d;

public class Matrix {

	/**
	 * Columns, Rows
	 */
	public double[][] array;
	public int columns, rows;

	/**
	 * Creates an Identity Matrix 3*3
	 */
	public Matrix () {
		this.columns = 3;
		this.rows = 3;
		this.array = new double[3][3];
		for (int y = 0; y < 3; y++)
			for (int x = 0; x < 3; x++)
				this.array[y][x] = 0; 
		this.createIdentity();
	}
	
	/**
	 * 
	 * @param rows son las filas lool
	 * @param columns son las columnas looos
	 */
	public Matrix (int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		this.array = new double[rows][columns];
		for (int y = 0; y < this.rows; y++)
			for (int x = 0; x < this.columns; x++)
				this.array[y][x] = 0;
	}
	
	public int getRows () {
		return this.rows;
	}
	
	public int getColumns () {
		return this.columns;
	}
	
	public void createIdentity () {
		int to;
		if (this.rows < this.columns)
			to = this.rows;
		else
			to = this.columns;
		
		for (int i = 0; i < to; i++) {
			this.setCell(i, i, 1);
		}
	}
	
	public double getCell (int row, int column) {
		return this.array[row][column];
	}
	
	public void setCell (int row, int column, double value) {
		this.array[row][column] = value;
	}
	
	public void display () {
		for (int y = 0; y < this.rows; y++) {
			for (int x = 0; x< this.columns; x++)
				System.out.print(" " + this.array[y][x]);
			System.out.println();
		}
	}
	
	public static Matrix product (Matrix m1, Matrix m2) {
		Matrix res = new Matrix(m1.rows, m2.columns);
		for (int i = 0; i < m1.rows; i++) {
			for (int j = 0; j < m2.columns; j++) {
				for (int k = 0; k < m1.rows; k++) {
					/*System.out.println("i = " + i);
					System.out.println("j = " + j);
					System.out.println("k = " + k);
					System.out.println("m1 cols = " + m1.columns);
					System.out.println("m1 rows = " + m1.rows);
					System.out.println("m2 cols = " + m2.columns);
					System.out.println("m2 rows = " + m2.rows);
					System.out.println("res cols = " + res.columns);
					System.out.println("res rows = " + res.rows);
					*/res.array[i][j] += m1.array[i][k] * m2.array[k][j];
				}
			}
		}
		return res;
	}
	
}
