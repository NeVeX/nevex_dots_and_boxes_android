package com.mark.board;


public class Edges {
    /** 
     * Applies the involution ({@link Edge#NORTH NORTH},  
     * {@link Edge#SOUTH SOUTH}) ({@link Edge#EAST EAST}, 
     * {@link Edge#WEST WEST}) to its parameter.  
     */
    public static int oppositeSide(int side) {
	if (side == Edge.NORTH) 
	    return Edge.SOUTH;
	if (side == Edge.SOUTH) 
	    return Edge.NORTH;
	if (side == Edge.EAST) 
	    return Edge.WEST;
	if (side == Edge.WEST)
	    return Edge.EAST;
	return -1;
    }
    

    //public static int circle) {
    
    //public static {}
    /** 
     * This utility function assumes an infinite board.<br/>
     * <b>Warning:</b> Make sure you validate the returned {@link Edge} for 
     * the {@link boxit.RmtBoard board} on which yo play.
     * @param edge the edge to be represented differently.
     * @return The same edge as <key>edge</key> but represented 
     * from the box on the other side. 
     */
    public static Edge otherBox(Edge edge) {
	int c = edge.getCol();
	int r = edge.getRow();
	int e = edge.getEdge(); 
	if (e == Edge.NORTH) 
	    r--;
	if (e == Edge.SOUTH) 
	    r++;
	if (e == Edge.EAST) 
	    c--;
	if (e == Edge.WEST)
	    c++;
	return new Edge(c, r, oppositeSide(e));
    }
}
