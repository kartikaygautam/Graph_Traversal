import java.util.Scanner;
import java.util.Stack;
import java.util.Vector;

public class GraphDFT
{
	protected Vector<Vertex> graph;
	protected Stack<Vertex> s = new Stack<Vertex>();
	protected Vector<Vertex> output = new Vector<Vertex>();
	
	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */
	
	public static void main(String[] args)
	{
		new GraphDFT().run();

		return;
	}
	
	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	public void run()
	{
		
		while(true)
		{
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);
			System.out.println("DO YOU WANT TO INITIATE A TRAVERSAL?(Y/N) ");
			String input = sc.nextLine();
			
			if(input.equalsIgnoreCase("y"))
			{
				initialiseGraph();
				output.clear();
				initialiseTraversal();
				System.out.println("\n");
			}
			else break;
		}	
		System.out.println("PROGRAM TERMINATED..");
	}
	
	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */
	
	private void initialiseGraph() 
	{
		int inf = Integer.MAX_VALUE;
		int max_row, max_col;
		
		// THE ADJACENCY MATRIX :-
		
                  		/*        A    B    C    D    E    F    G    H    I */
		int adj[][] = { 
						/* A */ { inf, 8,   inf, 10,  inf, inf, 12,  inf, inf },
						/* B */ { 8,   inf, inf, inf, 12,  18,  inf, inf, inf },
						/* C */ { inf, inf, inf, inf, inf, 2,   inf, 10,  inf },
						/* D */ { 10,  inf, inf, inf, inf, 8,   inf, inf, inf },
						/* E */ { inf, 12,  inf, inf, inf, inf, 24,  inf, inf },
						/* F */ { inf, 18,  2,   8,   inf, inf, inf, inf, inf },
						/* G */ { 12,  inf, inf, inf, 24,  inf, inf, inf, inf },
						/* H */ { inf, inf, 10,  inf, inf, inf, inf, inf, inf },
						/* I */ { inf, inf, inf, inf, inf, inf, inf, 3,   inf }
		};

		max_row  = max_col = 9;
		
		/*
		 * ADD VERTICES TO THE GRAPH
		 */
		
		graph = new Vector<Vertex>();
		
		graph.add(new Vertex("A"));
		graph.add(new Vertex("B"));
		graph.add(new Vertex("C"));
		graph.add(new Vertex("D"));
		graph.add(new Vertex("E"));
		graph.add(new Vertex("F"));
		graph.add(new Vertex("G"));
		graph.add(new Vertex("H"));
		graph.add(new Vertex("I"));
		
		/*
		 * INITIALIZING THE CORRESPONINDG EDGE LIST
		 * FOR EACH VERTEX IN THE GRAPH SHOWN ABOVE.  
		 */
		
		for(int i=0; i<max_row; i++)
		{
			Vertex v = graph.elementAt(i);
			
			for(int j=0; j<max_col; j++)
			{
				if(adj[i][j]!=inf)
				{
					Edge e = new Edge(v, graph.elementAt(j), adj[i][j]);
					v.addEdge(e);
				}//end if
				
			}//end col
			
			v.initialiseNeighbours();
			
		}// end row
	}
	
	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	public void initialiseTraversal() {
		String source = getSourceLabel();

		Vertex v = getCorrespondingVertex(source);

		if (valid(v))
		{
			DFT(v);
			for (int i = 0; i < output.size(); i++)
				System.out.print(output.elementAt(i).getLabel() + " ");
		}
		else
		{
			System.out.println("\nNO SUCH VERTEX EXIST..PLEASE RE-ENTER \n");
			initialiseTraversal();
		}
	}

	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	@SuppressWarnings("resource")
	private String getSourceLabel() {
		Scanner sc = new Scanner(System.in);
		System.out.println("ENTER THE SOURCE VERTEX: ");
		String input = sc.nextLine();
		return input;
	}

	private Vertex getCorrespondingVertex(String source) {
		for (int i = 0; i < graph.size(); i++) {
			String element = graph.elementAt(i).getLabel();
			if (source.equalsIgnoreCase(element))
				return graph.elementAt(i);
		}
		return null;
	}

	private boolean valid(Vertex v) {
		if (v != null)
			return true;
		else
			return false;
	}

	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	public void DFT(Vertex source) {
		Vertex current = source;
		current.markVisited();
		output.addElement(current);
		while (current.nextUnvisitedNeighbour() != null)
			DFT(current.nextUnvisitedNeighbour());
	}

	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	protected static class Vertex {
		private String label;
		private int id; /* Integral id of vertex: [0, n-1] */
		private int state; /* 0: undiscovered; 1: discovered; 2: visited */
		private int pred; /* Predecessor node. Unused here */
		private Vector<Edge> edgelist;
		private Vector<Vertex> neighbours;

		private static int counter = 0;

		private final static int undiscovered = 0;
		private final static int discovered = 1;
		private final static int visited = 2;

		public Vertex(String label) {
			this.label = label;
			state = undiscovered;
			pred = -1;
			id = counter++;
			edgelist = null;
		}

		public int getState() {
			return this.state;
		}

		public String getLabel() {
			return this.label;
		}

		public int getId() {
			return this.id;
		}

		public Vector<Edge> getEdgeList() {
			return this.edgelist;
		}

		public Vector<Vertex> getNeighbours() {
			return this.neighbours;
		}

		public void markDiscovered() {
			state = discovered;
		}

		public void markVisited() {
			state = visited;
		}

		public void addEdge(Edge e) {
			if (edgelist == null) {
				edgelist = new Vector<Edge>();
			}

			if (edgelist.isEmpty())
				edgelist.addElement(e);
			else {
				int comp;
				for (int i = 0; i < edgelist.size(); i++) {
					comp = e.compareTo(edgelist.elementAt(i));
					if (comp == -1 || comp == 0) {
						edgelist.add(i, e);
						return;
					}
				}
			}
			edgelist.add(e);
		}

		public void initialiseNeighbours() {
			neighbours = new Vector<Vertex>();

			for (int i = 0; i < edgelist.size(); i++) {
				Vertex temp = edgelist.elementAt(i).getDestination();
				neighbours.add(temp);
			}
		}

		public Vertex nextUnvisitedNeighbour() {
			for (int i = 0; i < neighbours.size(); i++) {
				Vertex n = neighbours.elementAt(i);
				if (n.getState() == 0)
					return n;
			}

			return null;
		}
	}

	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

	protected static class Edge {
		private Vertex source;
		private Vertex destination;
		private int cost;

		public Edge(Vertex source, Vertex destination, int cost) {
			this.source = source;
			this.destination = destination;
			this.cost = cost;
		}

		public Vertex getSource() {
			return this.source;
		}

		public Vertex getDestination() {
			return this.destination;
		}

		public int getCost() {
			return this.cost;
		}

		public int compareTo(Edge o) {
			if (this.cost < o.cost)
				return -1;
			else if (this.cost > o.cost)
				return 1;
			else
				return 0;
		}
	}

	/* ----------------------------------------------------------------------- */
	/* ----------------------------------------------------------------------- */

}
