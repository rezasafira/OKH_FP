/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Timetabling;
import java.util.Arrays;

public class Schedule {
	
	String nameFile;
	int[][] conflictMatrix;
	int[] timeslot;
	int totalExams, timeslotIndex;
	
	public Schedule(String file, int[][] conflictMatrix, int totalExams) {
		this.nameFile = file;
		this.conflictMatrix = conflictMatrix;
		this.totalExams = totalExams;
	}
	
	public int[][] getSchedule() {
            int [][] timeslotSchedule = new int[totalExams][2];
            // mengisi array timeslot          
            for (int course = 0; course < totalExams; course++) {
    		timeslotSchedule[course][0] = (course+1);
    		timeslotSchedule[course][1] = timeslot[course];
            }
            return timeslotSchedule; 
	}
	
	public int[] scheduling(int[] timeslot) {
            this.timeslot = new int[totalExams];
            timeslotIndex = 1;
            
            for(int i= 0; i < conflictMatrix.length; i++) {
    		this.timeslot[i] = 0;
            }
    	
            for(int i = 0; i < conflictMatrix.length; i++) {
		for (int j = 1; j <= timeslotIndex; j++) {
                    if(isTimeslotAvailable(i, j, conflictMatrix, timeslot)) {
			this.timeslot[i] = j;
                        break;
                    } else {
			timeslotIndex = timeslotIndex+1;
                    }
                }
            }
		return this.timeslot;
	}
	public int[] schedulingByDegree(int [][] sortedCourse) {
            this.timeslot = new int[totalExams];
            timeslotIndex = 1; 
            for(int i= 0; i < sortedCourse.length; i++) {
    		this.timeslot[i] = 0;
            }
            
            //update timeslot
            for(int course = 0; course < sortedCourse.length; course++) {
		for (int time_slotindex = 1; time_slotindex <= timeslotIndex; time_slotindex++) {
                    if(isTimeslotAvailableWithSorted(course, time_slotindex, conflictMatrix, sortedCourse, this.timeslot)) {
			this.timeslot[sortedCourse[course][0]-1] = time_slotindex;
			break;
                    }
                    else {
			timeslotIndex = timeslotIndex+1; 
                    }
		}
            }
		
            return this.timeslot;
    }
	
	public int getTotalTimeslots(int[][] timeslot) { 
		int jumlah_timeslot = 0;
		
		for(int i = 0; i < timeslot.length; i++) {
			if(timeslot[i][1] > jumlah_timeslot)
				jumlah_timeslot = timeslot[i][1];
		}
		return jumlah_timeslot; 
	}
        
	public static boolean isTimeslotAvailable(int course, int timeslot, int[][] conflictmatrix, int[] timeslotarray) {
		for(int i = 0; i < conflictmatrix.length; i++)
			if(conflictmatrix[course][i] != 0 && timeslotarray[i] == timeslot)
				return false;
		
		return true;
	}
        
        public static boolean isTimeslotAvailableWithSorted(int course, int timeslot, int[][] conflictmatrix, int[][] sortedmatrix, int[] timeslotarray) {
		for(int i = 0; i < sortedmatrix.length; i++) 
			if(conflictmatrix[sortedmatrix[course][0]-1][i] != 0 && timeslotarray[i] == timeslot) {
				return false;
			}
		
		return true;
	}
        
        public static boolean checkRandomTimeslot(int randomCourse, int randomTimeslot, int[][] conflict_matrix, int[][] jadwal){
        for(int i=0; i<conflict_matrix.length; i++)
            if(conflict_matrix[randomCourse][i] !=0 && jadwal[i][1]==randomTimeslot)
                return false;
        return true;              
    }
        
           public static boolean checkRandomTimeslotForLLH(int randomCourse, int randomTimeslot, int[][] conflict_matrix, int[][] jadwal){
        for(int i=0; i<conflict_matrix.length; i++)
            if(conflict_matrix[randomCourse][i] !=0 && jadwal[i][1]==randomTimeslot)
                return false;
        return true;              
    }
}
