/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Timetabling;

import java.util.Random;

public class SimulatedAnnealing {
    int[][] timeslotSimulatedAnnealing, conflict_matrix, course_sorted;
	int[] timeslot;
	String file;
	int jumlahexam, jumlahmurid, iterasi;
	double initialPenalty, bestPenalty, deltaPenalty;
	
	Schedule schedule;
        
    SimulatedAnnealing(String file, int[][] conflict_matrix, int[][] course_sorted, int jumlahexam, int jumlahmurid, int iterasi) { 
		this.file = file; 
		this.conflict_matrix = conflict_matrix;
		this.course_sorted = course_sorted;
		this.jumlahexam = jumlahexam;
		this.jumlahmurid = jumlahmurid;
		this.iterasi = iterasi;
	}
    
    
	public void getTimeslotBySimulatedAnnealing(double temperature) {
		double coolingrate = 0.1;
		schedule = new Schedule(file, conflict_matrix, jumlahexam);
		timeslot = schedule.schedulingByDegree(course_sorted);
		LowLevelHeuristic lowLevelHeuristics = new LowLevelHeuristic(conflict_matrix);
		
		// initial solution
		timeslotSimulatedAnnealing = schedule.getSchedule();
		initialPenalty = Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealing, jumlahmurid);
		
		int[][] timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotSimulatedAnnealing);
		
		for	(int i=0; i < iterasi; i++) {
			int llh = randomNumber(1, 5);
			int[][] timeslotLLH;
			switch (llh) {
				case 1:
					timeslotLLH = lowLevelHeuristics.move1(timeslotSimulatedAnnealingSementara);
					break;
				case 2:
					timeslotLLH = lowLevelHeuristics.swap2(timeslotSimulatedAnnealingSementara);
					break;
				case 3:
					timeslotLLH = lowLevelHeuristics.move2(timeslotSimulatedAnnealingSementara);
					break;
				case 4:
					timeslotLLH = lowLevelHeuristics.swap3(timeslotSimulatedAnnealingSementara);
					break;
				case 5:
					timeslotLLH = lowLevelHeuristics.move3(timeslotSimulatedAnnealingSementara);
					break;
				default:
					timeslotLLH = lowLevelHeuristics.move1(timeslotSimulatedAnnealingSementara);
					break;
			}
			
                        
			
		System.out.println("Suhu : " + temperature);
			if (Evaluator.getPenalty(conflict_matrix, timeslotLLH, jumlahmurid) <= Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealingSementara, jumlahmurid)) {
				timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotLLH);
				if (Evaluator.getPenalty(conflict_matrix, timeslotLLH, jumlahmurid) <= Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealing, jumlahmurid)) {
					timeslotSimulatedAnnealing = Evaluator.getTimeslot(timeslotLLH);
					bestPenalty = Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealing, jumlahmurid);
				}
			}
				else if (acceptanceProbability(Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealingSementara, jumlahmurid), Evaluator.getPenalty(conflict_matrix, timeslotLLH, jumlahmurid), temperature) > Math.random())
					timeslotSimulatedAnnealingSementara = Evaluator.getTimeslot(timeslotLLH);

			System.out.println("Iterasi: " + (i+1) + " memiliki penalty " + Evaluator.getPenalty(conflict_matrix, timeslotSimulatedAnnealingSementara, jumlahmurid));

			temperature = temperature - coolingrate;
		}
                

		deltaPenalty = ((initialPenalty-bestPenalty)/initialPenalty)*100;
		System.out.println("=============================================================");
		System.out.println("		Metode SIMULATED ANNEALING				 			 "); // print best penalty
		System.out.println("\nPenalty Initial : "+ initialPenalty); // print initial penalty
		System.out.println("Penalty Terbaik : " + bestPenalty); // print best penalty
		System.out.println("Terjadi Peningkatan Penalti : " + deltaPenalty + " % dari inisial solusi");
		System.out.println("Timeslot yang dibutuhkan : " + schedule.getTotalTimeslots(timeslotSimulatedAnnealing) + "\n");
		System.out.println("=============================================================");
	}
        
        public int[][] getTimeslotWithSimulatedAnnealing() { return timeslotSimulatedAnnealing; }
        
        private static int randomNumber(int min, int max) {
		Random random = new Random();
		return random.nextInt(max - min) + min;
	}
        private static double acceptanceProbability(double penaltySementara, double penaltyLLH, double temperature) {

		return Math.exp((penaltySementara - penaltyLLH) / temperature);
	}
        

	
}
