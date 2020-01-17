package BediSlivkoff.cs146.project3;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

//This program is designed to generate a perfect maze and solve it using DFS and BFS
public class Maze {
	private Cell cellList[]; //an array containing all the cells
	private int adjacencyMatrix[][]; //adjacency matrix representation of all the cells
	private int size; //dimensions of the maze
	private Random rand; //random number generator  


	public Maze(int size) {
		this.size = size;

		cellList = new Cell[size * size];
		for (int i = 0; i < size * size; i++)
		{
			cellList[i] = new Cell(' ', i);
		}

		adjacencyMatrix = new int[size * size][size * size];
		for (int i = 0; i < size * size; i++)
		{
			for (int j = 0; j < size * size; j++)
			{
				adjacencyMatrix[i][j] = 0;
			}
		}

		rand = new Random(0);
	}


	//pseudocode given by Prof. Potika
	public void generatePerfectMaze() {
		Stack<Integer> cellStack = new Stack<Integer>(); 
		int totalCells = cellList.length;
		for (int i = 0; i < totalCells; i++)
		{
			cellStack.push(i);
		}
		int currentCell = adjacencyMatrix[0][0];
		int visitedCells = 1;
		while (visitedCells < totalCells) 
		{
			ArrayList<Integer> wallsIntact = findWallsIntact(currentCell);
			if (!wallsIntact.isEmpty()) 
			{
				int nextCell = rand.nextInt(wallsIntact.size());
				nextCell = wallsIntact.get(nextCell);

				adjacencyMatrix[currentCell][nextCell] = 1;
				adjacencyMatrix[nextCell][currentCell] = 1;

				cellStack.push(currentCell);
				currentCell = nextCell;
				visitedCells++;
			}
			else 
			{
				currentCell = cellStack.pop();
			}
		}
	}

	//Copy needed for solving the maze in DFS and BFS
	private Maze copyOfMaze() {
		Maze maze = new Maze(size);

		for (int i = 0; i < size * size; i++)
		{
			maze.cellList[i] = new Cell(cellList[i].label, cellList[i].index);
		}

		for (int i = 0; i < size * size; i++)
		{
			for (int j = 0; j < size * size; j++)
			{
				maze.adjacencyMatrix[i][j] = this.adjacencyMatrix[i][j];
			}
		}
		return maze;
	}

	//solves maze using the DFS method
	public Maze solveDFS() 
	{
		Maze maze = copyOfMaze();
		Stack<Cell> cellStack = new Stack<Cell>();
		char label = '0';
		cellStack.push(maze.cellList[0]); 
		while (!cellStack.isEmpty()) 
		{
			Cell currentCell = cellStack.pop();			

			if (!currentCell.visited) 
			{
				currentCell.visited = true;	
				currentCell.label = label++;	

				if (currentCell.index == cellList.length - 1)
				{
					break;
				}

				if (currentCell.label == '9')
				{
					label = '0';
				}

				ArrayList<Cell> emptyCell = maze.getEmptyCell(currentCell.index);
				for (Cell neighbors : emptyCell) 
				{
					neighbors.parent = currentCell;				
					cellStack.push(neighbors);
				}
			}
		}
		return maze;
	}


	//solves maze using BFS
	public Maze solveBFS() {

		Maze maze = copyOfMaze();
		Queue<Cell> cellQueue = new LinkedList<Cell>();
		char label = '0';
		cellQueue.add(maze.cellList[0]); 

		while (!cellQueue.isEmpty()) 
		{
			Cell currentCell = cellQueue.poll();	

			if (!currentCell.visited) 
			{
				currentCell.visited = true;		
				currentCell.label = label++;		

				if (currentCell.index == cellList.length - 1)
				{
					break;
				}

				if (currentCell.label == '9')
				{
					label = '0'; 
				}

				ArrayList<Cell> emptyCell = maze.getEmptyCell(currentCell.index);
				for (Cell neighbors : emptyCell) 
				{
					neighbors.parent = currentCell;
					cellQueue.add(neighbors);
				}
			}
		}
		return maze;
	}


	//returns the index of the cell
	private int cellIndex(int row, int column) {
		return row * size + column;
	}

	//determines the unbroken walls of neighbors
	private ArrayList<Integer> findWallsIntact(int cellIndex) {
		ArrayList<Integer> unbrokenNeighbor = new ArrayList<Integer>();
		int row = cellIndex / size;		
		int column = cellIndex % size;	

		if (column - 1 >= 0) {
			int leftNeighbor = cellIndex(row, column - 1);
			if (allWallsStatus(leftNeighbor))
			{
				unbrokenNeighbor.add(leftNeighbor);
			}
		}

		if (column + 1 < size) 
		{
			int rightNeighbor = cellIndex(row, column + 1);
			if (allWallsStatus(rightNeighbor))
			{
				unbrokenNeighbor.add(rightNeighbor);
			}
		}

		if (row - 1 >= 0) 
		{
			int topNeighbor = cellIndex(row - 1, column);
			if (allWallsStatus(topNeighbor))
			{
				unbrokenNeighbor.add(topNeighbor);
			}
		}

		if (row + 1 < size) 
		{
			int bottomNeighbor = cellIndex(row + 1, column);
			if (allWallsStatus(bottomNeighbor))
			{
				unbrokenNeighbor.add(bottomNeighbor);
			}
		}
		return unbrokenNeighbor;
	}

	//returns the neighbors that have not been visited by looking at all the adjacent cells
	private ArrayList<Cell> getEmptyCell(int cellIndex) 
	{
		ArrayList<Cell> emptyCell = new ArrayList<Cell>();
		int row = cellIndex / size;		
		int column = cellIndex % size;	

		if (column - 1 >= 0) 
		{
			int leftNeighbor = cellIndex(row, column - 1);

			if (adjacencyMatrix[cellIndex][leftNeighbor] == 1 && !cellList[leftNeighbor].visited)
			{
				emptyCell.add(cellList[leftNeighbor]);
			}
		}

		if (column + 1 < size) 
		{
			int rightNeighbor = cellIndex(row, column + 1);

			if (adjacencyMatrix[cellIndex][rightNeighbor] == 1 && !cellList[rightNeighbor].visited)
			{
				emptyCell.add(cellList[rightNeighbor]);
			}
		}

		if (row - 1 >= 0) {
			int topNeighbor = cellIndex(row - 1, column);

			if (adjacencyMatrix[cellIndex][topNeighbor] == 1 && !cellList[topNeighbor].visited)
			{
				emptyCell.add(cellList[topNeighbor]);
			}
		}

		if (row + 1 < size)
		{
			int bottomNeighbor = cellIndex(row + 1, column);

			if (adjacencyMatrix[cellIndex][bottomNeighbor] == 1 && !cellList[bottomNeighbor].visited)
			{
				emptyCell.add(cellList[bottomNeighbor]);
			}
		}
		return emptyCell;
	}

	//determines if the top wall is broken or not
	private boolean topWallStatus(int cellIndex) 
	{
		int row = cellIndex / size;		
		int column = cellIndex % size;	

		if (row == 0)
		{
			return false;
		}

		int topNeighbor = cellIndex(row - 1, column);
		return (adjacencyMatrix[cellIndex][topNeighbor] == 1);	
	}

	//determines if the bottom wall is broken or not
	private boolean bottomWallStatus(int cellIndex) 
	{
		int row = cellIndex / size;		
		int column = cellIndex % size;	

		if (row == size - 1)
		{
			return false;
		}

		int bottomNeighbor = cellIndex(row + 1, column);
		return (adjacencyMatrix[cellIndex][bottomNeighbor] == 1);	
	}

	//determines if the right wall is broken or not
	private boolean rightWallStatus(int cellIndex) 
	{
		int row = cellIndex / size;			
		int column = cellIndex % size;		

		if (column == size - 1)
		{
			return false;
		}

		int rightNeighbor = cellIndex(row, column + 1);
		return (adjacencyMatrix[cellIndex][rightNeighbor] == 1);	
	}

	//determines if the left wall is broken or not
	private boolean leftWallStatus(int cellIndex) 
	{
		int row = cellIndex / size;
		int column = cellIndex % size;

		if (column == 0)
		{
			return false;
		}

		int leftNeighbor = cellIndex(row, column - 1);
		return (adjacencyMatrix[cellIndex][leftNeighbor] == 1);	
	}

	//returns status of each wall
	private boolean allWallsStatus(int cellIndex) 
	{
		return !leftWallStatus(cellIndex) && !rightWallStatus(cellIndex) && 
				!topWallStatus(cellIndex) && !bottomWallStatus(cellIndex);
	}


	//Shows the path of the solved maze using hashtags
	public void labelPath() {		
		for (int i = 0; i < cellList.length; i++)
		{
			cellList[i].label = ' ';
		}

		Cell currentCell = cellList[cellList.length - 1];
		while (currentCell.parent != null) 
		{
			currentCell.label = '#';
			currentCell = currentCell.parent;
		}
		currentCell.label = '#';	
	}

	//prints actual maze using string characters
	@Override
	public String toString() 
	{
		String produceMaze = "";

		for (int row = 0; row < size; row++) 
		{

			for (int column = 0; column < size; column++) 
			{
				produceMaze += "+"; 
				if (cellIndex(row, column) == 0)
				{
					produceMaze += " "; 
				}
				else 
				{

					if(topWallStatus(cellIndex(row, column))) 
					{
						produceMaze += " ";
					}
					else 
					{
						produceMaze += "-";
					}
				}
			}

			produceMaze += "+\n";	

			for (int column = 0; column < size; column++) 
			{

				if(leftWallStatus(cellIndex(row, column))) 
				{
					produceMaze += " ";
				}
				else
				{
					produceMaze += "|";
				}

				produceMaze += cellList[cellIndex(row, column)].label;
			}

			produceMaze += "|\n";	
		}

		for (int column = 0; column < size; column++) 
		{
			produceMaze += "+"; 
			if (cellIndex(size - 1, column) == cellList.length - 1)
			{
				produceMaze += " "; 
			}
			else 
			{
				if(bottomWallStatus(cellIndex(size - 1, column))) 
				{
					produceMaze += " ";
				}
				else
				{
					produceMaze += "-";
				}
			}
		}
		produceMaze += "+\n";
		return produceMaze;
	}

}