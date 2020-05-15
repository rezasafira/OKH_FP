/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Timetabling;

import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class MainHeuristic {

    static String folderDataset = "/Users/MacBookPRO/Desktop/Kuliah/S6/OKH/Dataset Toronto/";
    static String namafile[][] = {
                                    {"car-f-92", "CAR92"}, 
                                    {"car-s-91", "CAR91"}, 
                                    {"ear-f-83", "EAR83"}, 
                                    {"hec-s-92", "HEC92"},
                                    {"kfu-s-93", "KFU93"}, 
                                    {"lse-f-91", "LSE91"}, 
                                    {"pur-s-93", "PUR93"}, 
                                    {"rye-s-93", "RYE93"}, 
                                    {"sta-f-83", "STA83"},
                                    {"tre-s-92", "TRE92"},	
                                    {"uta-s-92", "UTA92"}, 
                                    {"ute-s-92", "UTE92"}, 
                                    {"yor-f-83", "YOR83"}};
   
    static int timeslot[]; // fill with course & its timeslot
    static int[][] conflict_matrix, course_sorted, hasil_timeslot, jadwal;
    
	
	private static Scanner scanner;
        
     public static void main(String[] args) throws IOException {
        scanner = new Scanner(System.in);
        for	(int i=0; i< namafile.length; i++)
        	System.out.println(i+1 + ". Penjadwalan " + namafile[i][1]);
        
        System.out.print("\nSilahkan pilih file untuk dijadwalkan : ");
        int pilih = scanner.nextInt();
        
        String filePilihanInput = namafile[pilih-1][0];
        String filePilihanOutput = namafile[pilih-1][1];
        
        String file = folderDataset + filePilihanInput;
        
		
        Course course = new Course(file);
        int jumlahexam = course.getNumberOfCourses();
        
        conflict_matrix = course.getConflictMatrix();
        int jumlahmurid = course.getTotalMurid();
        
        
		// sort exam by degree
	course_sorted = course.sortingByDegree(conflict_matrix, jumlahexam);
		
        
        
		// scheduling
		/*
		 * Scheduling by largest degree
		 */
		
        long starttimeLargestDegree = System.nanoTime();
	Schedule schedule = new Schedule(file, conflict_matrix, jumlahexam);
	timeslot = schedule.schedulingByDegree(course_sorted);
	int[][] timeslotByLargestDegree = schedule.getSchedule();
	long endtimeLargestDegree = System.nanoTime();
          
        
                HillClimbing HC = new HillClimbing(file, conflict_matrix, course_sorted, jumlahexam, jumlahmurid, 1000);   
                long starttimeHC = System.nanoTime();
		HC.getTimeslotByHillClimbing();
		long endtimeHC = System.nanoTime();
                /*
		 * use simmulated annealing for timesloting
		 * params : temperature
		 */
                SimulatedAnnealing SA = new SimulatedAnnealing(file, conflict_matrix, course_sorted, jumlahexam, jumlahmurid, 1000);
		long starttimeSA = System.nanoTime();
		SA.getTimeslotBySimulatedAnnealing(100.0);
		long endtimeSA = System.nanoTime();
		
		
		// end time
		System.out.println("PERBANDINGAN HASIL METODE HEURISTIK DARI PENJADWALAN UNTUK " + filePilihanOutput + "\n");
		
                System.out.println("-------------------------Initial Solution-------------------------");
		System.out.println("Timeslot        : " + schedule.getTotalTimeslots(schedule.getSchedule()));
		System.out.println("Penalty         : " + Evaluator.getPenalty(conflict_matrix, schedule.getSchedule(), jumlahmurid));
		System.out.println("Time Execution  : " + ((double) (endtimeLargestDegree - starttimeLargestDegree)/1000000000) + " detik.\n");
		
                System.out.println("----------------------------Hill Climbing----------------------------");
                System.out.println("Timeslot        : " + HC.getJumlahTimeslotHC());
		System.out.println("Penalty         : " + Evaluator.getPenalty(conflict_matrix, HC.getTimeslotHillClimbing() , jumlahmurid));
		System.out.println("Time Execution  :" + ((double) (endtimeHC - starttimeHC)/1000000000) + " detik.\n");
               
               
                System.out.println("-------------------------Simulated Annealing-------------------------");
		System.out.println("Timeslot        : " + schedule.getTotalTimeslots(schedule.getSchedule()));
		System.out.println("Penalty         : " + Evaluator.getPenalty(conflict_matrix, SA.getTimeslotWithSimulatedAnnealing(), jumlahmurid));
		System.out.println("Time Execution  : " + ((double) (endtimeSA - starttimeSA)/1000000000) + " detik.\n");
		

    }
        
}
	