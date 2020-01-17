package BediSlivkoff.cs146.project3;

//stores each cell of the maze as an object
public class Cell {
	
	public final int index; //keeps track of index of cell
	public boolean visited; //determines if the cell is visited or not
	public char label; //produces labels for the maze
	public Cell parent; //keeps track of the source cell

	public Cell(char label, int index)
	{
		this.label = label;
		this.index = index;
		visited = false;
		parent = null;
	}
}