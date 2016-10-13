package com.mark.players.ai;

import com.mark.board.Board;
import com.mark.board.Edge;


public class Viper extends AIEngineBase implements AIEngineInterface {
   boolean never_search_neutral_again = false;
   int seed = 347, my_limit, edges_left = 0;
   boolean player_first = true, search_initial = true;
   int chainAmount, edgeAmount, boxCount, chainCounter, boxCounter, nextChain, nextBox,
       pchainAmount, pboxCount, pchainCounter, pboxCounter, limit, is_it_potential;
   boolean  validCoord = false, loopChain = false, noParameters = false;;
   public Edge chainData[][], pchainData[][], loopChainData, coordinates;
   int total_original_edges = 0;
   int my_score = 0, his_score = 0;
   boolean should_flip = true, pick_it = false;


   public Viper(Board b)
   {
	  super(b);
   }
   
   public static String NAME = "Viper";
   
	public String getAIName() {
		// TODO Auto-generated method stub
		return NAME;
	}
   
   
/** Calls NeVeX_Computing to ignite and fire up the A.I. Engine. */
public Edge aiMakeMove() {
//       Edge tempEdge = null;
//       while(tempEdge == null) {
//                 tempEdge = AI_Make_Move();
//       }     
       return AI_Make_Move();
}

/** Created by Mark Cunningham.<br>
 *  This Artificial Intelligence Engine is used for winning games.<br>
 *  It is the Strongest A.I. Engine employed by NeVeX.<br>
 *<br>
 *  Returns : The A.I. Engine's selected Edge.<br>
 *<br>
 */
private Edge AI_Make_Move()  {
   
    /*
     *
     *             |
     *             |            \                  /          \  
     *             |             \                /            \          
     *             |      ______  \              /       ______ \          TM 
     *      |      |    |          \            /    |           \      /  
     *      |\     |    |           \          /     |            \    /
     *      | \    |    |            \        /      |             \  /
     *      |  \   |    |----         \      /       |----          \/
     *      |   \  |    |              \    /        |              /\
     *      |    \ |    |               \  /         |             /  \
     *      |     \|    |_______         \/          |_______     /    \
     *      |                                                           \
     *      |            _  _        _      |-        _                  \  
     *      |           |_ |_| |\/| |_| |_| |_ | |\| |_|                  \
     *      |                       |             _____|                   \
     *
     *
     *
     */ 

           huntChains(0,0); 
           Edge tempEdge = null;
           int i;
           int my_col,my_row;
           int box_edges, above_box_edges, below_box_edges,
            left_box_edges, right_box_edges;
           int neutral_edge_row = 0, neutral_edge_col = 0, neutral_edge = 0;
           int start_col, start_row;
           int search_loop;
           int chain_length[] = new int[chainAmount*2];
           int p_chain_length[] = new int[pchainAmount*2];
           int four_box[][] = new int [row*column][8];
           int smallest_potential = 0, potential_chain_use = 0;
           int smallest_chain = 0, temp_smallest_chain = 0, chain_use = 0;
           int p_length_chain = 1;
           int p_chain_number = 0, box = 2;
           int length_chain = 1;
           int chain_number = 0, temp_smallest_potential = 0;
           int temp_biggest_potential = 0;
           int how_many_empty_boxes = 0, count = 1;
           int total_chain_boxes = 0, total_pchain_boxes = 0, how_many_overlaps = 0;
           
 
           boolean neutral_edge_exists = false;
           boolean picked = false;
           boolean is_there_chain = false;
           boolean is_there_potential_chain = false;
           boolean should_double_cross = false;   
           boolean is_there_box = false;
           boolean north_box = false, east_box = false, south_box = false, west_box = false;
           boolean cannot_choose_it = false;
           
   edges_left = board.getEdgesLeft();
    
   if ( search_initial == true) { 
       total_original_edges = board.getEdgesLeft();
       for ( my_col = 0; my_col < row; my_col++) { // row first
           for ( my_row = 0; my_row < column; my_row++) {
               if (    (board.isSelected(my_row, my_col, 0))||
                       (board.isSelected(my_row, my_col, 1)) ||
                       (board.isSelected(my_row, my_col, 2)) ||
                       (board.isSelected(my_row, my_col, 3))) {
                   player_first = false;
                   
               }
           }
       }
       search_initial = false;
   }
   
// THE NEUTRAL EDGE CASE:
          start_col = (seed * (int) (Math.random()*seed)) % row;
          start_row = (seed * (int) (Math.random()*seed)) % column;

   if ( never_search_neutral_again == false ) {
      loop_edge_search: for (search_loop =0; search_loop < 2; search_loop++) {
          for ( my_col = start_col; my_col < row; my_col++) { // row first
               for ( my_row = start_row; my_row < column; my_row++) {

                   box_edges=0; above_box_edges=0; below_box_edges=0;
                    left_box_edges=0; right_box_edges=0;

                   if (!board.isSelected(my_row, my_col, 0)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 1)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 2)) {
                       box_edges++;
                   }
                   if (!board.isSelected(my_row, my_col, 3)) {
                       box_edges++;
                   }


                   if (box_edges >= 3) {  // box is good, but check neighbouring boxes

                       if (my_col > 0) { // top of box
                           if ((board.isSelected(my_row, my_col,0))) { // obviously need to check
                                   above_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row,my_col-1, 0)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 1)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 2)) {
                                   above_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col-1, 3)) {
                                   above_box_edges++;
                               }
                           }
                       }
                       if (my_row < column - 1) { // east side of board
                           if ((board.isSelected(my_row, my_col,1))) { // obviously need to check
                                   right_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row+1,my_col, 0)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 1)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 2)) {
                                   right_box_edges++;
                               }
                               if (!board.isSelected(my_row+1,my_col, 3)) {
                                   right_box_edges++;
                               }
                          }
                       }

                       if (my_col < row - 1) { // very bottom of board
                           if ((board.isSelected(my_row, my_col,2))) { // obviously need to check
                                   below_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row,my_col+1, 0)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 1)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 2)) {
                                   below_box_edges++;
                               }
                               if (!board.isSelected(my_row,my_col+1, 3)) {
                                   below_box_edges++;
                               }
                           }
                       }

                       if (my_row > 0) { // west side
                           if ((board.isSelected(my_row, my_col,3))) { // obviously need to check
                                   left_box_edges = 0;
                           }
                           else {
                               if (!board.isSelected(my_row-1,my_col, 0)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 1)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 2)) {
                                   left_box_edges++;
                               }
                               if (!board.isSelected(my_row-1,my_col, 3)) {
                                   left_box_edges++;
                               }
                           }
                       }
                   }
                       if ( above_box_edges >= 3 || (box_edges >= 3&& my_col == 0 && (!board.isSelected(my_row, my_col, 0)))) {
                            //north edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 0;
                           neutral_edge_exists = true;
                           break loop_edge_search;
                       }
                       else if ( right_box_edges >= 3 || (box_edges>= 3 && my_row == (column-1) && (!board.isSelected(my_row, my_col,1)))) {  //right edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 1;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( below_box_edges >= 3 || (box_edges>= 3 && my_col == (row-1) && (!board.isSelected(my_row, my_col,2)))) {  //bottom edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 2;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
                       else if ( left_box_edges >= 3 || (box_edges >=3 && my_row == 0 && (!board.isSelected(my_row, my_col, 3)))) {//left edge can be selected
                           neutral_edge_row = my_row;
                           neutral_edge_col = my_col;
                           neutral_edge = 3;
                           neutral_edge_exists = true;
                       break loop_edge_search;
                       }
               }
          }

          // if random edge search didnt work restart loop with original values
          start_col = 0;
          start_row = 0;
      }
   }
          
          
  // neutral edge is either found or doesn't exist be this stage
    if ( !neutral_edge_exists ) {
        this.never_search_neutral_again = true;
       }
  // searching for boxes 
   box_search: for ( my_row = 0; my_row < row; my_row++) { // row first
            for ( my_col = 0; my_col < column; my_col++) {
                if ( my_row == row - 1 || my_col == column - 1) {
                 }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    how_many_empty_boxes++;
                }
            }   
   }
          
   box_search: for ( my_row = 0; my_row < row; my_row++) { // row first
            for ( my_col = 0; my_col < column; my_col++) {
                if ( my_row == row - 1 || my_col == column - 1) {
                 }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    north_box = true;
                    is_there_box = true;
                    break box_search;
                    
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    east_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && !board.isSelected(my_col+1, my_row+1, 3)) {
                    west_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }
                else if ( board.isSelected(my_col, my_row, 0) && board.isSelected(my_col, my_row, 3) &&
                        board.isSelected(my_col+1, my_row, 0) && board.isSelected(my_col+1, my_row, 1) &&
                         board.isSelected(my_col+1, my_row+1, 1) && board.isSelected(my_col+1, my_row+1, 2) &&
                          board.isSelected(my_col, my_row+1, 2) && board.isSelected(my_col, my_row+1, 3) &&
                            !board.isSelected(my_col, my_row, 1) && !board.isSelected(my_col, my_row, 2) &&
                             !board.isSelected(my_col+1, my_row+1, 0) && board.isSelected(my_col+1, my_row+1, 3)) {
                    south_box = true;
                    is_there_box = true;
                    four_box[0][0] = my_col;
                    four_box[0][1] = my_row;
                    break box_search;
                
                }

            }
        }
   
   if (chainAmount > 0) {  // get the length of the chains
       is_there_chain = true;
       box = 2;

       
       for (chain_number = 1; chain_number <= chainAmount; chain_number++ ) {
           while( chainData[chain_number][box].getCol() <= limit ) {
               length_chain++;
               box++;
           }
           chain_length[chain_number] = length_chain; 
           box = 2;
           length_chain = 1;
       }
       temp_smallest_chain = chain_length[1];
       chain_use = 1;

       for ( i = 2; i <= chainAmount; i++) {
        smallest_chain = chain_length[i];
          if(smallest_chain < temp_smallest_chain) {
             chain_use = i;
             temp_smallest_chain = smallest_chain;
          }
       }
   
   }
   if (pchainAmount > 0) {  // get the length of the potential chains
       is_there_potential_chain = true;
       box = 2;
       
       for (p_chain_number = 1; p_chain_number <= pchainAmount; p_chain_number++ ) {
           while( pchainData[p_chain_number][box].getCol() <= limit ) {
               p_length_chain++;
               box++;
           }
           p_chain_length[p_chain_number] = p_length_chain; 
           p_length_chain = 1;
           box = 2;
       }
       
       temp_smallest_potential = p_chain_length[1]; // first p.chain
       temp_biggest_potential = p_chain_length[1];
       potential_chain_use = 1;

       for( i = 2; i <= pchainAmount; i++) {
        smallest_potential = p_chain_length[i];
         if (smallest_potential < temp_smallest_potential) {
            potential_chain_use = i;
            temp_smallest_potential = smallest_potential;
            ////////System.out.println("pot chain "+(i)+" has length "+p_chain_length[i]+" and is smaller than pot chain "+(i-1)+" with length "+p_chain_length[i-1]);  
         }
         else if (smallest_potential > temp_biggest_potential ) {
             temp_biggest_potential = smallest_potential;
         }
       }
   }
   
   ////System.out.println("the edges are "+edges_left);
   if ( neutral_edge_exists ) {
       if ( (edges_left % 2 == 1 && player_first == true) || pick_it == true) {
           int q = 1, c = 2;
           while ( q <= chainAmount ) {
               if ( chain_length[q] == 1 ) {
                 if ( pick_it == false ) {
                     while ( c <= chainAmount ) {
                       if ( chain_length[c] == 1) {
                              // then select both the chains as this wont effect the game
                               tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                               picked = true;
                               //System.out.println("debug 4a");
                               pick_it = true;
                       }
                       c++;
                     }
                 }
                 else if ( pick_it == true && chain_length[q] == 1 && picked == false ) {
                   tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                   picked = true;
                   //System.out.println("debug 4b");
                   pick_it = false;
                 }
               }
               q++;
           }
       }
   }
   if ( edges_left <= (total_original_edges * 0.9) && neutral_edge_exists && picked == false && this.should_flip == true) { // when 10 percent of the game is done, then let's play'
      // need to give away a odd chain, size of one or three
                  if ( edges_left % 2 == 0 && player_first == false ) {
                      if (is_there_potential_chain == true && picked == false) {
                thisloop: for ( i = 1; i <= pchainAmount; i++) {
                              if ( p_chain_length[i] == 1 ) {
                                  // need to check if the if the 1 chain is not really a two chain
                                  int y = i + 1;
                                here:  while ( y <= pchainAmount ) {
                                      if ( p_chain_length[y] == 1 ) {
                                          if ( (pchainData[i][1].getCol() == pchainData[y][1].getCol()+1 
                                              || pchainData[i][1].getCol() == pchainData[y][1].getCol()-1)
                                                && (pchainData[i][1].getRow() == pchainData[y][1].getRow()+1 
                                              || pchainData[i][1].getRow() == pchainData[y][1].getRow()-1)) {
                                            // cannot pick this chain  
                                          }
                                          else {
                                              // i can pick the i chain
                                              tempEdge = new Edge(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge());
                                              picked = true;
                                              //System.out.println(" debug 1");
                                              break here;
                                          }
                                      }
                                      y++;
                                  }
                              }
                              if ( p_chain_length[i] == 3 ) {
                                  boolean my_turn = true;
                                 if (my_turn == true && (four_box[0][0] == pchainData[i][1].getCol() && four_box[0][1] == pchainData[i][1].getRow()
                                    || four_box[0][0] == pchainData[i][2].getCol() && four_box[0][1] == pchainData[i][2].getRow()
                                    || four_box[0][0] == pchainData[i][3].getCol() && four_box[0][1] == pchainData[i][3].getRow() )) {
                                      // cant use this chain as it will give away an even chain
                                      // bug what about a chain with 3 with opposite starting points
                                      my_turn = false;
                                 }
                                 if ( pchainAmount >= 2 && my_turn == true) {
                                      int y;
                                      for  ( i = 1; i < pchainAmount; i++) {
                                       for ( y = 2; y <= pchainAmount; y++) {
                                           if ( i == y) {
                                           }
                                           else if (pchainData[i][2].getCol() == pchainData[y][3].getCol()
                                               && pchainData[i][3].getCol() == pchainData[y][2].getCol()
                                               && pchainData[i][2].getRow() == pchainData[y][3].getRow()
                                               && pchainData[i][3].getRow() == pchainData[y][2].getRow()) {
                                               // cant pick this either
                                               my_turn = false;
                                                 }
                                       }
                                      }
                                 }
                                 if ( my_turn == true && !board.isSelected(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge())) {
                                          tempEdge = new Edge(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge());
                                          picked = true;
                                          //System.out.println(" debug 2");
                                          break thisloop;
                                          }
                              }
                          }
                      }
                  }
     }

   for( i = 1; i <= chainAmount; i++ ) {
       while( chainData[i][count].getCol() <= limit) {
           total_chain_boxes++;
           count++; 
       }
       count = 1;
   }
   count = 1;
   for( i = 1; i <= pchainAmount; i++ ) {
       while( pchainData[i][count].getCol() <= limit) {
           total_pchain_boxes++;
           count++;
       }
       count = 1;
   }
   
   count = 1;
   i = 1;
   while ( i <= pchainAmount) {
       i++;
   }

   // DO I DOUBLE CROSS PEOPLE?????
   if ( picked == false ) {
       int counting = 0;
       boolean forward = true;
       if ( chainAmount == 1 && chain_length[1] == 2 && pchainAmount >= 2 ) {
           int t = 1;
          here: while ( t <= pchainAmount ) {
               if ( p_chain_length[t] == 1 ) {
                   int e = t + 1;
                   counting = 1;
                   while ( e <= pchainAmount ) {
                       if ( p_chain_length[e] == 1 ) {
                           counting = 2;
                           break here;
                       }
                       e++;
                   }
                   break here;
               }
               t++;
           }
           if ( counting == 1 ) {
              should_double_cross = false;
              forward = false;
           }
           if ( counting == 2) {
               should_double_cross = true;
               forward = false;
           }
       }
       if ( forward == true ) {
           if( !neutral_edge_exists && is_there_chain && is_there_potential_chain && temp_smallest_potential == 1) {
              should_double_cross = false;
           }
           else if ( edges_left == 2 ) {
               should_double_cross = false;
           }
           else if (!neutral_edge_exists && is_there_chain && is_there_potential_chain) {
               boolean is_there_overlap = false;
               // find some overlaps
                   for  ( i = 1; i < chainAmount; i++) {
                     for ( int y = 2; y <= chainAmount; y++) {
                        if ( i == y) {
                        }
                        else if (chainData[i][2].getCol() == chainData[y][chain_length[y]].getCol()
                                 && chainData[i][3].getCol() == chainData[y][chain_length[y-1]].getCol()
                                 && chainData[i][2].getRow() == chainData[y][chain_length[y]].getRow()
                                 && chainData[i][3].getRow() == chainData[y][chain_length[y-1]].getRow()) {
                            // these two chains have the same traits and are wrongly thought to have 2 extra boxes,
                            // so we simply subtract for every instance we find
                            is_there_overlap = true;
                            how_many_overlaps++;
                        }
                     }
                   }
             if ( chainAmount > 2) {
                   should_double_cross = false;
             }
           // so now i have the correct number of boxes left.
            // only act upon the last chain 
            else if ( chainAmount == 1 && chain_length[1] == 2) {
                 int my_boxes = 0, his_boxes = 0;
                 //decide if it is best to double_cross
                 int t = 1;
                 for ( t = 1; t < pchainAmount; t++) {
                     if ( temp_biggest_potential == p_chain_length[t]){ 
                     }
                     else {
                         my_boxes += ( p_chain_length[t] - 2);
                         his_boxes += 2;
                     }
                 }
                 my_boxes += temp_biggest_potential; // my last chain
                 my_boxes += chain_length[1] - 2;
                 his_boxes += 2;

                 if ( his_boxes > my_boxes ) {
                     should_double_cross = false; // its smaller and don't double cross'
                 }
                 else {
                     should_double_cross = true; // its bigger
                 } 
               if ( my_score == 0 && his_score == 0) {
                     this.my_score = my_boxes;
                     this.his_score = his_boxes; 
                 }  
             }
             else if ( chainAmount == 2 && is_there_box == true) {
                 int my_boxes = 0, his_boxes = 0;
                 //decide if it is best to double_cross
                 int t = 1;
                 for ( t = 1; t < pchainAmount; t++) {
                     if ( temp_biggest_potential == p_chain_length[t]){ 
                     }
                     else {
                         my_boxes += ( p_chain_length[t] - 2);
                         his_boxes += 2;
                     }
                 }
                 my_boxes += temp_biggest_potential; // my last chain
                 // now the boxes
                 // i give away the current and the other ones
                 his_boxes += 4 + ( how_many_empty_boxes *4);
                 if ( his_boxes > my_boxes ) {
                     should_double_cross = false; // its smaller and don't double cross'
                 }
                 else {
                     should_double_cross = true; // its bigger
                 }
                 if ( my_score == 0 && his_score == 0) {
                     this.my_score = my_boxes;
                     this.his_score = his_boxes; 
                 }
             }
             else if ( chainAmount == 2 && is_there_overlap == true && chain_length[1] == 3 && chain_length[2] == 3) {
                  int my_boxes = 0, his_boxes = 0;
                 //decide if it is best to double_cross
                 int t = 1;
                 for ( t = 1; t < pchainAmount; t++) {
                     if ( temp_biggest_potential == p_chain_length[t]){ 
                     }
                     else {
                         my_boxes += ( p_chain_length[t] - 2);
                         his_boxes += 2;
                     }
                 }
                 my_boxes += temp_biggest_potential; // my last chain
                 // how big is the overlap
                 my_boxes += (chain_length[1] + 1) - 4; // leave this
                 his_boxes += 4;
                 if ( his_boxes > my_boxes ) {
                     should_double_cross = false; // its smaller and don't double cross'
                 }
                 else {
                     should_double_cross = true; // its bigger
                 }
                 if ( my_score == 0 && his_score == 0) {
                     this.my_score = my_boxes;
                     this.his_score = his_boxes; 
                 } 
             }
           }
       }
   }
    // chains and potential chains
   if (is_there_chain == true && is_there_potential_chain == true && picked == false) {
       if ( neutral_edge_exists == true) {
           if ( edges_left % 2 == 0 && player_first == false) {
               if ( should_flip == false ) {
                   tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
                   picked = true;
                   //System.out.println(" debug 4 asd67");
               }
               else {
                   if ( chainAmount >= 2 ) {
                       int z = 1;
                       int d = 2;
                       while ( z <= chainAmount ) {
                           if ( chain_length[z] == 1 ) {
                               while ( d <= chainAmount) {
                                   if ( chain_length[d] == 1 ) {
                                       // select it only if it is not in the even chain
                                       if ( chainData[z][1].getEdge() == chainData[d][1].getEdge() 
                                            && ( chainData[z][1].getRow() == chainData[d][1].getRow()
                                            || chainData[z][1].getCol() == chainData[d][1].getCol())) {
                                            // cannot pick this pchain  
                                          }
                                       else {
                                         tempEdge = new Edge( chainData[z][1].getCol(), chainData[z][1].getRow(), chainData[z][1].getEdge());
                                         picked = true;
                                         //System.out.println("DEBUG 102");
                                       }
                                   }
                                   d++;
                               }
                           }
                           z++;
                       }
                   }
                   if ( !picked ) {
                       // pick all the chains bar the last box in the last chain
                       int y = 1, current = chain_length[1], new_one, use = 1;
                       while ( y <= chainAmount) {
                            new_one = chain_length[y];
                            if ( new_one > current) {
                                current = new_one;
                                use = y;
                            }
                       y++;
                       }
                       // now select the biggest one
                       if ( chainAmount == 1 && chain_length[use] > 1 ) {
                           tempEdge = new Edge( chainData[use][1].getCol(), chainData[use][1].getRow(), chainData[use][1].getEdge());
                           picked = true;
                           //System.out.println(" debug 4");
                       }
                   }
               }
           }
           else if ( edges_left % 2 == 1 && player_first == true) { // watch out for odd chains
                // WATCH OUT FOR ODD CHAINS
             int q;
           overlapping: for (q = 1; q<= chainAmount; q++) {
                if ( chain_length[q] % 2 == 1 ) {
                    
                    if ( chain_length[q] >= 5 ) {
                        tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                        picked = true;
                        //System.out.println(" debug 5");
                    }
                    else {
                        // find another like this 1
                        int r;
                        for ( r = q + 1; r <= chainAmount; r++) {
                            if ( chain_length[q] == chain_length[r]) {
                                // two chains with the same length, now check their location
                                if ( chain_length[q] == 1) {
                                    if ( chainData[q][1].getEdge() == chainData[r][1].getEdge() 
                                    && ( chainData[q][1].getRow() == chainData[r][1].getRow()
                                    || chainData[q][1].getCol() == chainData[r][1].getCol())) {
                                        tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                                        picked = true;  
                                        //System.out.println(" debug6");
                                    }
                                    else {
                                        tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);
                                        picked = true;
                                        //System.out.println(" debug7");
                                        cannot_choose_it = true;
                                    }
                                }
                                else if ( chainData[q][2].getCol() == chainData[r][chain_length[r]].getCol()
                                     && chainData[q][3].getRow() == chainData[r][chain_length[r-1]].getRow()) {
                                    // they are in the same chain
                                    tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                                    picked = true;
                                    //System.out.println(" debug8");
                                }
                            }
                        }
                    }  
                }
                else {
                    tempEdge = new Edge(chainData[q][1].getCol(),chainData[q][1].getRow(),chainData[q][1].getEdge());
                    picked = true;
                    //System.out.println(" debug9");
                }
               if ( picked ) {
                    break overlapping;
               }  
             }
           }
           
         if ( !picked ) {
               tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
               picked = true;
               //System.out.println(" debug15");
               cannot_choose_it = true;
        }   
         
      }
       else { // get all smaller chains
         if (chainAmount == 1)  { // we are in last chain
                if (should_double_cross == false)  { //only two boxes to gain
                     tempEdge = new Edge(chainData[1][1].getCol(),chainData[1][1].getRow(),chainData[1][1].getEdge());
                     picked = true;
                     //System.out.println(" debug16");
                }
                else if (should_double_cross == true ) {
                            tempEdge = new Edge(chainData[1][2].getCol(),chainData[1][2].getRow(),chainData[1][2].getEdge());
                            picked = true;
                            //System.out.println(" debug17");
                }
         }     
         if ( chainAmount == 2 && picked == false) {
            if( is_there_box == true) {
                if ( should_double_cross == true) {
                  // these are all double crossing moves inside a box
                    if ( north_box ) {
                        tempEdge = new Edge(four_box[0][0]+1,four_box[0][1]+1,3);
                        //System.out.println(" debug18");
                    }
                    else if ( east_box ) {
                        tempEdge = new Edge(four_box[0][0],four_box[0][1],2);
                        //System.out.println(" debug19");
                    } 
                    else if ( west_box ) {
                        tempEdge = new Edge(four_box[0][0]+1,four_box[0][1]+1,0);
                        //System.out.println(" debug20");
                    }
                    else if ( south_box ) {
                        tempEdge = new Edge(four_box[0][0],four_box[0][1],1);
                        //System.out.println(" debug21");
                    }
                picked = true;
                }
            }
         }
         // checking if two chains are in a the same chain, i.e the same except the last and first locations
         if ( chainAmount >= 2 && picked == false) { // this should be if two chains are the same, not just 2 chains     
              /*if( temp_smallest_chain != 3) { 
                tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
                picked = true;
                ////System.out.println(" debug22");
              } */
              //else {
                // there is one chain now with length 2 at least.
                // we need to double cross here aswell.
                // first we should get all the bigger chains if there exists
                // a 3 chain with an extra 1 attached.
                
                  // just apply for the double cross first.
                int length_3_found;  
         length_3:for(int checking = 1; checking <= chainAmount; checking++) {
                    if ( chain_length[checking] == 3) {
                        length_3_found = checking;
                        for (int checking2 = 1; checking2 <= chainAmount; checking2++) {
                            if (chain_length[checking2] == 3) {
                                // now check to see that this one chain is actually
                                // belonging to the 3 chain found earlier.
                                // this is checked by checking that this one chain 
                                // opens to the three chain.
                                ////System.out.println("I HAVE FOUND TWO CHAINS OF LENGTH 3");
                                if ( chainData[checking2][1].getEdge() == 0 ) {
                                    if ( chainData[checking][3].getCol() == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow()+1 == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 2) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 800");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 1 ) {
                                    if ( chainData[checking][3].getCol()-1  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow() == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 3) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 801");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 2 ) {
                                    if ( chainData[checking][3].getCol()  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow()-1 == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 0) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 802");
                                        break length_3;
                                    }
                                }
                                else if ( chainData[checking2][1].getEdge() == 3 ) {
                                    if ( chainData[checking][3].getCol()+1  == chainData[checking2][1].getCol() 
                                         && chainData[checking][3].getRow() == chainData[checking2][1].getRow()
                                         && chainData[checking][3].getEdge() == 1) {
                                        // this really a four length chain
                                        tempEdge = new Edge(chainData[checking][2].getCol(),chainData[checking][2].getRow(),chainData[checking][2].getEdge());
                                        picked = true;
                                        ////System.out.println("*NEW* - debug 803");
                                        break length_3;
                                        
                                    }
                                }
                            }
                        }
                    }
                }
         }
       
           // leave this
      if ( picked == false) {
               tempEdge = new Edge(chainData[chain_use][1].getCol(),chainData[chain_use][1].getRow(),chainData[chain_use][1].getEdge());
               picked = true;
               //System.out.println(" debug3");
           }
       }
   }
  // only potential chains
   else if (is_there_chain == false && is_there_potential_chain == true && picked == false)  { // no chain but only p.chains
       if (neutral_edge_exists == true)  {   // neutral edgee exists
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);
           picked = true;
           //System.out.println(" debug31");
           cannot_choose_it = true;
       }
       else {  // find smallest potential chain and give it away
           if ( temp_smallest_potential == 1 && pchainAmount >= 2 ) {
               int r = 1, badly = -1;
               boolean another = false, one = false;
               boolean p_another = false, two = false;
               while ( r <= pchainAmount ) {
                   if ( p_chain_length[r] == 1 ) {
                       int w = r + 1;
                       one = true;
                       while ( w <= pchainAmount ) {
                           if ( p_chain_length[w] == 1) {
                               another = true;
                           }
                           w++;
                       }
                   }
                    r++;
               }
               if ( another == false && one == true) {
                // find a two chain if it exists
                 int u = 1;
                 while ( u <= pchainAmount ) {
                   if ( p_chain_length[u] == 2 ) {
                      int p = u + 1;
                      two = true;
                      badly = u;
                         while ( p <= pchainAmount ) {
                             if ( p_chain_length[p] == 2) {
                                   p_another = true;
                             }
                             p++;
                         }
                   }
                   u++;
                 }
               }
               if ( another == false && one == true && p_another == true && two == true ) {
                   // pick the two chain badly 
                   // get the two chain
                   int this_row, this_col, next_row, next_col;
                       this_col = pchainData[badly][1].getCol();
                       this_row = pchainData[badly][1].getRow();
                       next_col = pchainData[badly][2].getCol();
                       next_row = pchainData[badly][2].getRow();
                       
                       if ( this_row == next_row - 1 && this_col == next_col ) { // south edge i must select
                        tempEdge = new Edge(this_col,this_row,2);
                        picked = true;
                        //System.out.println(" debug32s");
                       }
                       else if ( this_row == next_row + 1 && this_col == next_col ) { // north edge i must select
                        tempEdge = new Edge(this_col,this_row,0);
                        picked = true;
                        //System.out.println(" debug33s1");
                       }
                       else if ( this_row == next_row && this_col == next_col - 1 ) { // east edge i must select
                        tempEdge = new Edge(this_col,this_row,1);
                        picked = true;
                        //System.out.println(" debug34s2");
                       }
                       else if ( this_row == next_row && this_col == next_col + 1 ) { // west edge i must select
                        tempEdge = new Edge(this_col,this_row,3);
                        picked = true;
                        //System.out.println(" debug35s3");
                       }
               }
           }  
      if ( !picked ) {          
           if ( temp_smallest_potential == 2) {
               // pick the edge that wont allow double crossing
               int y = 1;
               while ( y <= pchainAmount) {
                   if ( p_chain_length[y] == 2) {
                       int this_row, this_col, next_row, next_col;
                       this_col = pchainData[y][1].getCol();
                       this_row = pchainData[y][1].getRow();
                       next_col = pchainData[y][2].getCol();
                       next_row = pchainData[y][2].getRow();
                       
                       if ( this_row == next_row - 1 && this_col == next_col ) { // south edge i must select
                        tempEdge = new Edge(this_col,this_row,2);
                        picked = true;
                        //System.out.println(" debug32");
                       }
                       else if ( this_row == next_row + 1 && this_col == next_col ) { // north edge i must select
                        tempEdge = new Edge(this_col,this_row,0);
                        picked = true;
                        //System.out.println(" debug33");
                       }
                       else if ( this_row == next_row && this_col == next_col - 1 ) { // east edge i must select
                        tempEdge = new Edge(this_col,this_row,1);
                        picked = true;
                        //System.out.println(" debug34");
                       }
                       else if ( this_row == next_row && this_col == next_col + 1 ) { // west edge i must select
                        tempEdge = new Edge(this_col,this_row,3);
                        picked = true;
                        //System.out.println(" debug35");
                       }
                       
                   }
                   y++;
               }
           }
          // see if there is another size of 3 without any hitches to it
         /* if (temp_smallest_potential == 3) {
              // lets see if we can pick it
            boolean my_turn = true;
             if (my_turn == true && (four_box[0][0] == pchainData[i][1].getCol() && four_box[0][1] == pchainData[i][1].getRow()
                  || four_box[0][0] == pchainData[i][2].getCol() && four_box[0][1] == pchainData[i][2].getRow()
                  || four_box[0][0] == pchainData[i][3].getCol() && four_box[0][1] == pchainData[i][3].getRow() )) {
                   // cant use this chain as it will give away an even chain
                   // bug what about a chain with 3 with opposite starting points
                   my_turn = false;                        
             }
             if ( pchainAmount >= 2 && my_turn == true) {
               int y;
                 for  ( i = 1; i < pchainAmount; i++) {
                   for ( y = 2; y <= pchainAmount; y++) {
                      if ( i == y) {
                      }
                      else if (pchainData[i][2].getCol() == pchainData[y][3].getCol()
                               && pchainData[i][3].getCol() == pchainData[y][2].getCol()
                               && pchainData[i][2].getRow() == pchainData[y][3].getRow()
                               && pchainData[i][3].getRow() == pchainData[y][2].getRow()) {
                               // cant pick this either
                               my_turn = false;                
                      }
                   
                   }
                 }
                 if ( my_turn == true && !board.isSelected(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge())) {
                      tempEdge = new Edge(pchainData[i][1].getCol(),pchainData[i][1].getRow(),pchainData[i][1].getEdge());
                      picked = true;
                      //System.out.println(" debug36");                    
                 }
             }
          }*/
      }
         if ( picked == false) {
             tempEdge = new Edge(pchainData[potential_chain_use][1].getCol(),pchainData[potential_chain_use][1].getRow(),pchainData[potential_chain_use][1].getEdge());
             picked = true;
             //System.out.println(" debug37");
          }
       }
   }
    // only exists a chain
   else if (is_there_chain == true && is_there_potential_chain == false && picked == false) {
      if ( neutral_edge_exists == true) {
          tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
          picked = true;
      }
       else { //select chains starting with smallest
          tempEdge = new Edge( chainData[1][1].getCol(), chainData[1][1].getRow(), chainData[1][1].getEdge());
          picked = true;
          
       }
   }
   // no chains or potential chains
   if (is_there_chain == false && is_there_potential_chain == false && neutral_edge_exists == true && picked == false && cannot_choose_it == false) {
           tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
           picked = true;
           //System.out.println(" debug51");
           //////System.out.println("Picked an Edge with neutral edge 22");
           cannot_choose_it = true;
   } 
   
   if ( cannot_choose_it == true || tempEdge == null) {
       if ( neutral_edge_exists ) {
               //////System.out.println("---------here 1--------");
               if ( (edges_left % 2 == 1 && player_first == true)) {
                  // never have a one pchain size and only have one size two chain
                   // would be best to create one on the edge of the board
                       // so we need to act
                       // the corners
                   if( should_flip == true ) {
                           int randy;
                            randy = (seed * (int) (Math.random()*567)) % 4;
                            if (randy == 0 && !board.isSelected(0,0,0) ) {
                                tempEdge = new Edge ( 0, 0, 0 );
                                picked = true;
                                //System.out.println(" debug52");
                            }
                            if ( randy == 1 && !board.isSelected(column-1,row-1,2) ) {
                               tempEdge = new Edge ( column-1,row-1,2 );
                               //System.out.println(" debug53");
                               picked = true; 
                            }
                            if ( randy == 2 && !board.isSelected(column-1,0,1) ) {
                               tempEdge = new Edge ( column-1,0,1 );
                               //System.out.println(" debug54");
                               picked = true; 
                            }
                            if ( randy == 3 && !board.isSelected(0,row-1,2) ) {
                               tempEdge = new Edge ( 0,row-1,2 );
                               //System.out.println(" debug55");
                               picked = true; 
                            }
                            else {
                            }
                   }
                   if ( pchainAmount >= 1 ) {

                           int y = 1;
                        this_one:   while ( y <= pchainAmount ) {
                            int b = 0, edges = 4;
                                if ( p_chain_length[y] == 1) {
                                    // need to get rid of this chain length
                                    // find the edges that are not selected and select a neutral beside it
                                    int this_col, this_row;
                                    this_col = pchainData[y][1].getCol();
                                    this_row = pchainData[y][1].getRow();
                                    // what edges are not selected
                                    if ( !board.isSelected(this_col, this_row, 0)) {
                                        // north is not selected
                                        // try and pick 1, 3
                                        // can pick 0 iff 1 or 3 exist
                                        if ( this_row > 0 ) {
                                            b = 0; edges = 4;
                                            while ( b < 4 ) {
                                                if ( !board.isSelected(this_col, this_row-1, b) ) {
                                                    edges--;
                                                }
                                                b++;
                                            }
                                            if ( edges < 2 ) {
                                                if ( !board.isSelected(this_col, this_row-1, 1) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_col < column -1 ) {
                                                            if ( !board.isSelected(this_col+1, this_row-1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_col == column - 1) {
                                                        tempEdge = new Edge ( this_col, this_row-1, 1);
                                                        picked = true;
                                                        //System.out.println(" debug56");
                                                        break this_one;
                                                    }

                                                }
                                                else if ( !board.isSelected(this_col, this_row-1, 3) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_col > 0 ) {
                                                            if ( !board.isSelected(this_col-1, this_row-1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_col == 0 ) {
                                                        tempEdge = new Edge ( this_col, this_row-1, 3);
                                                        picked = true;
                                                        //System.out.println(" debug57");
                                                        break this_one;
                                                    }
                                                }
                                                else if ( !board.isSelected(this_col, this_row-1, 0) ) {
                                                        b = 0; edges = 4;
                                                        while ( b < 4 ) {
                                                            if ( this_row > 1) {
                                                                if ( !board.isSelected(this_col, this_row-2, b) ) {
                                                                     edges--;
                                                                }
                                                            }
                                                            b++;
                                                        }
                                                        if ( edges < 2 || this_row == 0) {
                                                            tempEdge = new Edge ( this_col, this_row-1, 0);
                                                            picked = true;
                                                            //System.out.println(" debug58");
                                                            break this_one;
                                                        }

                                                }
                                            }
                                        }
                                    }
                                    if ( !board.isSelected(this_col, this_row, 1)) {
                                        if ( this_col < column - 1 ) {
                                            b = 0; edges = 4;
                                            while ( b < 4 ) {
                                                if ( !board.isSelected(this_col+1, this_row, b) ) {
                                                    edges--;
                                                }
                                                b++;
                                            }
                                            if ( edges < 2 ) {
                                                if ( !board.isSelected(this_col+1, this_row, 0) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_row > 0 ) {
                                                            if ( !board.isSelected(this_col+1, this_row-1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_row == 0 ) {
                                                        tempEdge = new Edge ( this_col+1, this_row, 0);
                                                        picked = true;
                                                        //System.out.println(" debug59");
                                                        break this_one;
                                                    }

                                                }
                                                else if ( !board.isSelected(this_col+1, this_row, 2) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_row < row - 1 ) {
                                                            if ( !board.isSelected(this_col+1, this_row+1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_row == row - 1 ) {
                                                        tempEdge = new Edge ( this_col+1, this_row, 2);
                                                        picked = true;
                                                        //System.out.println(" debug60");
                                                        break this_one;
                                                    }
                                                }
                                                else if ( !board.isSelected(this_col+1, this_row, 1) ) {
                                                        b = 0; edges = 4;
                                                        while ( b < 4 ) {
                                                            if ( this_col < column - 2 ) {
                                                                if ( !board.isSelected(this_col+2, this_row, b) ) {
                                                                     edges--;
                                                                }
                                                            }
                                                            b++;
                                                        }
                                                        if ( edges < 2 || this_col == column - 1) {
                                                            tempEdge = new Edge ( this_col+1, this_row, 1);
                                                            picked = true;
                                                            //System.out.println(" debug61");
                                                            break this_one;
                                                        }

                                                }
                                            }
                                        }
                                    }
                                    if ( !board.isSelected(this_col, this_row, 2)) {
                                        if ( this_row < row - 1 ) {
                                            b = 0; edges = 4;
                                            while ( b < 4 ) {
                                                if ( !board.isSelected(this_col, this_row+1, b) ) {
                                                    edges--;
                                                }
                                                b++;
                                            }
                                            if ( edges < 2 ) {
                                                if ( !board.isSelected(this_col, this_row+1, 1) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_col < column - 1 ) {
                                                            if ( !board.isSelected(this_col+1, this_row+1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_col == column - 1 ) {
                                                        tempEdge = new Edge ( this_col, this_row+1, 1);
                                                        picked = true;
                                                        //System.out.println(" debug62");
                                                        break this_one;
                                                    }

                                                }
                                                else if ( !board.isSelected(this_col, this_row+1, 3) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_col > 0 ) {
                                                            if ( !board.isSelected(this_col-1, this_row+1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_col == 0 ) {
                                                        tempEdge = new Edge ( this_col, this_row+1, 3);
                                                        picked = true;
                                                        //System.out.println(" debug63");
                                                        break this_one;
                                                    }
                                                }
                                                else if ( !board.isSelected(this_col, this_row+1, 2) ) {
                                                        b = 0; edges = 4;
                                                        while ( b < 4 ) {
                                                            if ( this_row < row - 2 ) {
                                                                if ( !board.isSelected(this_col, this_row+2, b) ) {
                                                                     edges--;
                                                                }
                                                            }
                                                            b++;
                                                        }
                                                        if ( edges < 2 || this_row == row - 1 ) {
                                                            tempEdge = new Edge ( this_col, this_row+1, 2);
                                                            picked = true;
                                                            //System.out.println(" debug64");
                                                            break this_one;
                                                        }

                                                }
                                            }
                                        }
                                    }
                                    if ( !board.isSelected(this_col, this_row, 3)) {
                                        if ( this_col > 0 ) {
                                            b = 0; edges = 4;
                                            while ( b < 4 ) {
                                                if ( !board.isSelected(this_col-1, this_row, b) ) {
                                                    edges--;
                                                }
                                                b++;
                                            }
                                            if ( edges < 2 ) {
                                                if ( !board.isSelected(this_col-1, this_row, 0) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_row > 0 ) {
                                                            if ( !board.isSelected(this_col-1, this_row-1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_row == 0 ) {
                                                        tempEdge = new Edge ( this_col-1, this_row, 0);
                                                        picked = true;
                                                        //System.out.println(" debug65");
                                                        break this_one;
                                                    }

                                                }
                                                else if ( !board.isSelected(this_col-1, this_row, 2) ) {
                                                    b = 0; edges = 4;
                                                    while ( b < 4 ) {
                                                        if ( this_row < row - 1 ) {
                                                            if ( !board.isSelected(this_col-1, this_row+1, b) ) {
                                                                 edges--;
                                                            }
                                                        }
                                                        b++;
                                                    }
                                                    if ( edges < 2 || this_row == row - 1 ) {
                                                        tempEdge = new Edge ( this_col-1, this_row, 2);
                                                        picked = true;
                                                        //System.out.println(" debug66");
                                                        break this_one;
                                                    }
                                                }
                                                else if ( !board.isSelected(this_col-1, this_row, 3) ) {
                                                        b = 0; edges = 4;
                                                        while ( b < 4 ) {
                                                            if ( this_col > 1 ) {
                                                                if ( !board.isSelected(this_col-2, this_row, b) ) {
                                                                     edges--;
                                                                }
                                                            }
                                                            b++;
                                                        }
                                                        if ( edges < 2 || this_col == 0 ) {
                                                            tempEdge = new Edge ( this_col-1, this_row, 3);
                                                            picked = true;
                                                            //System.out.println(" debug67");
                                                            break this_one;
                                                        }

                                                }
                                            }
                                        }
                                    }
                                    // if i get here with picked == false then something is seriously wrong, but it shouldn't'
                                    // this happens when we have a potential at any corner
                                }
                                y++;
                           }
                   }
               }
               else {// need to make a p_chain of length 1
                   int how_many = 0;
                       if ( (board.isSelected(0, row-1, 0) && board.isSelected(0, row-1, 1) 
                            && (!board.isSelected(0, row-1, 2) && !board.isSelected(0, row-1, 3)))) {
                           how_many = 1;
                       } 
                       if (board.isSelected(0, 0, 1) && board.isSelected(0, 0, 2)
                            && (!board.isSelected(0, 0, 0) && !board.isSelected(0, 0, 3))) {
                          how_many = 2;
                       }
                       if  (board.isSelected(column-1, row-1, 0) && board.isSelected(column-1, row-1, 3)
                            && (!board.isSelected(column-1, row-1, 1) && !board.isSelected(column-1, row-1, 2))) {
                            how_many = 3;  
                       }
                       if  (board.isSelected(column-1, 0, 3) && board.isSelected(column-1, 0, 2)
                            && (!board.isSelected(column-1, 0, 1) && !board.isSelected(column-1, 0, 0)))  {
                           how_many = 4;
                       }
                       if ( how_many == 1 || how_many == 3) {
                            this.should_flip = false;
                            ////System.out.println("i have stopped flipping");
                       }
                       else if ( how_many == 2 || how_many == 4 ) {
                            this.should_flip = true;
                            ////System.out.println("i have started flipping again");
                       }
                   
                       if ( this.should_flip == true ) {
                           if ( !board.isSelected(0, row-1, 0) || !board.isSelected(0, row-1, 1) 
                            && (!board.isSelected(0, row-1, 2) && !board.isSelected(0, row-1, 3))) {
                               if ( !board.isSelected(0, row-1, 0) ) {
                                   tempEdge = new Edge (0, row-1, 0);
                                   picked = true;
                                   //System.out.println(" debug68");
                               }
                               else if ( !board.isSelected(0, row-1, 1) ) {
                                   tempEdge = new Edge (0, row-1, 1);
                                   picked = true;
                                   //System.out.println(" debug69");
                               }
                           }
                           else if ( !board.isSelected(0, 0, 1) || !board.isSelected(0, 0, 2) 
                            && (!board.isSelected(0, 0, 0) && !board.isSelected(0, 0, 3))) {
                               if ( !board.isSelected(0, 0, 1) ) {
                                   tempEdge = new Edge (0, 0, 1);
                                   picked = true;
                                   //System.out.println(" debug70");
                               }
                               else if ( !board.isSelected(0, 0, 2) ) {
                                   tempEdge = new Edge (0, 0, 2);
                                   picked = true;
                                   //System.out.println(" debug71");
                               }
                           }
                           else if ( !board.isSelected(column-1, row-1, 1) || !board.isSelected(column-1, row-1, 3) 
                            && (!board.isSelected(column-1, row-1, 1) && !board.isSelected(column-1, row-1, 2))) {
                               if ( !board.isSelected(column-1, row-1, 1) ) {
                                   tempEdge = new Edge (column-1, row-1, 1);
                                   picked = true;
                                   //System.out.println(" debug72");
                               }
                               else if ( !board.isSelected(column-1, row-1, 3) ) {
                                   tempEdge = new Edge (column-1, row-1, 3);
                                   picked = true;
                               }
                           }
                           else if ( !board.isSelected(column-1, 0, 3) || !board.isSelected(column-1, 0, 2) 
                            && (!board.isSelected(column-1, 0, 1) && !board.isSelected(column-1, 0, 0))) {
                               if ( !board.isSelected(column-1, 0, 3) ) {
                                   tempEdge = new Edge (column-1, 0, 3);
                                   picked = true;
                                   //System.out.println(" debug73");
                               }
                               else if ( !board.isSelected(column-1, 0, 2) ) {
                                   tempEdge = new Edge (column-1, 0, 2);
                                   picked = true;
                                   //System.out.println(" debug74");
                               }
                           }

                       }
                   }
               }

       }

   if ( tempEdge == null) {
              //System.out.println("--ERROR--     No Edge Got Selected    --ERROR--");
   }  
   
   if ( tempEdge == null && neutral_edge_exists && picked == false ) {
        tempEdge = new Edge(neutral_edge_row, neutral_edge_col,neutral_edge);    // this is correct although names say otherwise
        picked = true;
        //System.out.println(" debug75");
        // fool proof
    }

    return tempEdge;
   
} // NeVeX_Computing End


            /********************************************/
            /*        CHAIN HUNTER ALGORITHM            */
            /********************************************/


    public void huntChains(int c, int r) {
   


        ////////System.out.println("-------IM IN THE chainhunter----------");

         int edgeCount = 0, unselect =0, i,j,k;                       //i = column, j = row, k = edge
         limit = column*row; pchainAmount = 0; chainAmount = 0;                     //Assigning varriables
         chainData = new Edge[limit+1][limit+1]; pchainData = new Edge[limit+1][limit+1];
         
         
         for(int a=0;a<limit;a++) {
             for(int b=0;b<limit;b++) {
                 chainData[a][b] = new Edge(limit+1,limit+1,0);      //Assigns a value that will never be used to each array of the data arrays
                 pchainData[a][b] = new Edge(limit+1,limit+1,0);     //Basically acts like a NULL value
             }
         }
         my_limit = limit;
          for(i=c;i<column;i++) {
                for(j=r;j<row;j++) {                                //Loop for searching board for chains
                  for(k=0;k<4;k++) {
                          if(board.isSelected(i,j,k) == true) {
                              edgeCount++;                              //Determines how many edges in box 
                          }
                          else
                              unselect = k;                         //Gives a name to the unselected edge
                   }
                   if(edgeCount == 3) {
                      chainAmount++;                   //Finds beginning of chain and records its coords & edge
                      ////////System.out.println("Chainfound");
                      boxCount = 1;
                      chainData[chainAmount][boxCount] = new Edge(i,j,unselect);
                      ////////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                      //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());                        //Some of your code in here
                      stalkChains(i, j, unselect);                  //Goes in to find the neighbouring box
                   }
                  edgeCount = 0;
                }
         }
                  
              for(i=c;i<column;i++) {
                for(j=r;j<row;j++) {        //Counts board for pot. chain data
                  for(k=0;k<4;k++) {
                          if(board.isSelected(i,j,k) == true) {  //Determines amount of edges in box & chooses the unselected edge
                              edgeCount++;                           //of a possible two
                          }
                          else
                              unselect = k;
                   }
                  
                   if(edgeCount == 2) {
                      coordinates = new Edge(i,j,unselect);
                      validCoord = true;                        //Finds a box for a pot. chain & checks that it isn't already recorded in chain data
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {
                              if(coordinates.getCol() == chainData[a][b].getCol() && coordinates.getRow() == chainData[a][b].getRow()) {
                                    validCoord = false;
                              }
                          }
                      }
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {            //Checks that the box isn't already recorded in pot. chain data
                              if(coordinates.getCol() == pchainData[a][b].getCol() && coordinates.getRow() == pchainData[a][b].getRow()) {
                                    validCoord = false;
                              }
                          }
                      }
                      if(validCoord == true) {
                                        pchainAmount++;
                                        is_it_potential = 1;
                                        loopChainData = new Edge(i,j,unselect);
                                       // //////System.out.println("pot. Chainfound");
                                        loopChainData = new Edge(i,j,unselect);     //If box isn't already recorded goes on to check it's neighbouring box
                                        stalkChains(i, j, unselect);
                                        validCoord = false;
                      }
                   }
                   validCoord = false;
                   edgeCount = 0;
             }
         }
         for(int a=0;a<limit;a++) {
            for(int b=0;b<limit;b++) {
                if(chainData[a][b].getCol() != limit+1) {
                ////////System.out.println("chain data for chain "+a+" box "+b+" is "+chainData[a][b].getCol()
                //+chainData[a][b].getRow()+chainData[a][b].getEdge());
                }
                
            } 
         }
         for(int a=0;a<limit;a++) {
            for(int b=0;b<limit;b++) {
                if(pchainData[a][b].getCol() != limit+1) {
                ////////System.out.println("pot. chain data for pot. chain "+a+" box "+b+" is "+pchainData[a][b].getCol()
                //+pchainData[a][b].getRow()+pchainData[a][b].getEdge());
                }
                
            } 
         }
         //regurgatateChains();
    }

    public void eatChains(int cols, int rows, int prevEdge) {
          int nextEdge = 4, edgeAmount = 0, loopParameter = 1;
          coordinates = new Edge(cols, rows, prevEdge);
          ////////System.out.println("determining info. about box");

          for(int k=0;k<4;k++) {
              // IT DOES NOT LIKE THIS LINE MICK I think - it might be a box type chain think (i.e. a circle)
              if(board.isSelected(cols,rows,k) == true) {  //Determines amount of edges in the current box
                  edgeAmount++;
              }
          }

          for(int k=0;k<4;k++) {
              if(edgeAmount == 2 && k != prevEdge && board.isSelected(cols,rows,k) == false) {
                   nextEdge = k;                                //Gets the next unselected edge of that box
              }
          }
          ////////System.out.println("noParameters = "+noParameters);
          
          if ( loopChainData == null )
          {
        	  int i=0;
        	  i = 9 + 3;
          }
          
          if((coordinates.getCol() == loopChainData.getCol() && coordinates.getRow() == loopChainData.getRow() 
          && is_it_potential == loopParameter) || (noParameters == true)) {
              is_it_potential = 2;
              loopParameter = 2;
              noParameters = true;
              ////////System.out.println("in a looped pot. chain");
              ////////System.out.println("noParameters = "+noParameters);
                      for(int a=0;a<limit;a++) {
                          for(int b=0;b<limit;b++) {                         //This function sorts out when the pot. chain goes in to loop
                              if(coordinates.getCol() == pchainData[a][b].getCol() && coordinates.getRow() == pchainData[a][b].getRow()) {
                                  ////////System.out.println("looped chain turned on");
                                  ////////System.out.println("I AM THE BEST PROGRAMMER IN THE WORLD!!!!!!!!!!!");
                                    loopChain = true;
                              }
                          }
                      }
          }


          if(edgeAmount == 2 && loopChain == false)  {
              

                if(boxCount >= 1) {
                    boxCount++;                                 //Records coordinates of the current box if in a chain
                    chainData[chainAmount][boxCount] = new Edge(cols,rows,nextEdge);
                    ////////System.out.println("chainData for chain "+chainAmount+ " is "+chainData[chainAmount][boxCount].getCol()
                    //+chainData[chainAmount][boxCount].getRow()+chainData[chainAmount][boxCount].getEdge());
                }
                else if(is_it_potential == 2) {
                    pboxCount++;                    //Records coordinates of the current box if in pot. chain
                    pchainData[pchainAmount][pboxCount] = new Edge(cols,rows,nextEdge);
                    ////////System.out.println("pot. chainData for pot. chain "+pchainAmount+ " box "+pboxCount+" is "+pchainData[pchainAmount][pboxCount].getCol()
                    //+pchainData[pchainAmount][pboxCount].getRow()+pchainData[pchainAmount][pboxCount].getEdge());
                }
                stalkChains(cols, rows, nextEdge);              //Goes on to check next box
          }

          else if(is_it_potential == 1)  {
                is_it_potential++;
                ////////System.out.println("beginning of pot. chain found");//Determines if the box is the start of a pot. chain
                loopChain = false;
                stalkChains(cols, rows, prevEdge);
          }
          else if(is_it_potential == 2) {
                is_it_potential = 0;
                ////////System.out.println("end of pot. chain");
                loopChain = false;
                noParameters = false;
                loopParameter = 1;
                pboxCount = 0;                              //Determines if the box is the end of a pot. chain
                return;
          }
          else 
                 ////////System.out.println("end of chain");
                 boxCount=0;                                //Box is end of a chain
                 return;
}
          
    public void stalkChains(int cols, int rows, int nextEdge) {
        loopChain = false;
        ////////System.out.println("going on to check neighbouring box");
        
              /* NORTH SEARCH */
              
              if( nextEdge == Edge.NORTH) {
                if(rows > 0) {
                  rows--;                       //If box has north edge unselected, as long a it's not in row zero, neighbouring north box is selected 
                  eatChains(cols, rows, 2);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;          //Determines if the box is start of a pot. chain & sends it back to get it's neighbour
                  eatChains(cols, rows, 0);
                }
                else {
                  boxCount=0;                      //box is end of a chain so terminates and resets all necessary variables
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }

              /*EAST SEARCH*/

              if( nextEdge == Edge.EAST) {
                if(cols < column-1) {
                  cols++;
                  eatChains(cols, rows, 3);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows, 1);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }
              
              /*SOUTH SEARCH*/

              if( nextEdge == Edge.SOUTH) {
                if(rows < row-1) {
                  rows++;                               //This search algorithm does the same as above search except with different edge & end column or row
                  eatChains(cols, rows,0);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows,2);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                  return;
                }
              }
              
              /*WEST SEARCH*/
              
              if( nextEdge == Edge.WEST) {
                if(cols > 0) {
                  cols--;
                  eatChains(cols, rows, 1);
                }
                else if(is_it_potential == 1) {
                  is_it_potential = 2;
                  eatChains(cols, rows, 3);
                }
                else {
                  boxCount=0;
                  pboxCount = 0;
                  is_it_potential = 0;
                return;
                }
              }
}
    //Following method isn't used just ignore it
    public void regurgatateChains() {

       // //////System.out.println("Im in");
        
        int next = 0, limit = (column*row), cols, rows, edges;
        //tempChainData = new Edge[limit+1][limit+1];
        
        for(int x=0;x<(column*row);x++) {
            for(int y=0;y<(column*row);y++) {
                if(chainData[x][y].getCol() == limit+1 && chainData[x][y].getRow() == limit+1) {
                    ////////System.out.println("in here aswell");
                    //tempChainData[x][y] = chainData[x][y];
               
                for(int chain=next;chain<(column*row);chain++) {
                    for(int box=next;box<(column*row);box++) {
                        if(chainData[chain][box].getCol() != limit+1 && chainData[chain][box].getRow() != limit+1) {
                        ////////System.out.println("and here");
                        cols = chainData[x][y].getCol();
                        rows = chainData[x][y].getRow();
                        edges = chainData[x][y].getEdge();
                        //tempChainData[x][y] = new Edge(cols, rows, edges);
                       // chainData[x][y] = tempChainData[x][y];

                        next++;
                        }
                        }
                    }
                }
            }
        }
        
       for(int i=0;i<(column*row);i++) {
            for(int j=0;j<(column*row);j++) {
                ////////System.out.println("sorted chain data is"+chainData[i][j].getCol()+chainData[i][j].getRow()+chainData[i][j].getEdge());
            } 
       }
    }


}
