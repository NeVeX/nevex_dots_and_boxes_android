package com.mark.players.ai;

import com.mark.board.Board;
import com.mark.board.Edge;

public class Random extends AIEngineBase implements AIEngineInterface {
    /** Creates a new instance of Nevex */
    public Random(Board b) {
    	super(b);
    }

    public Edge aiMakeMove()
    {
//        Edge tempEdge = null;
//        while(tempEdge == null) {   
//                  tempEdge = AI_Make_Move();
//        }
//        return tempEdge;
    	return AI_Make_Move();
    }
    
    public static String NAME = "Eejit";
    
 	public String getAIName() {
 		// TODO Auto-generated method stub
 		return NAME;
 	}

	private Edge AI_Make_Move() {
	    Edge tempEdge = null;
        boolean proceed = true;
        boolean wrong_edge = true;
        int rand_col = 0, rand_row = 0, rand_edge = 0;
        int seed = 976;
        
        while(proceed) {
        // toPlay = new Random().nextInt(2);   
        rand_col = (seed * (int) (Math.random()*seed)) % column;

        rand_row =  (seed * (int) (Math.random()*seed)) % row;

        rand_edge =  (seed * (int) (Math.random()*seed)) % 4;
        seed++;
             
//         System.out.println("col = "+rand_col);
//         System.out.println("row = "+rand_row);
//         System.out.println("edge = "+rand_edge);
         
         proceed = board.isSelected(rand_col, rand_row, rand_edge );


        }
        tempEdge = new Edge(rand_col, rand_row, rand_edge);

        return tempEdge;

   

	}

}
    
