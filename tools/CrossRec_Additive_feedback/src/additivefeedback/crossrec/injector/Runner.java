package additivefeedback.crossrec.injector;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;


class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}



public class Runner {
	
	private String srcDir;	
	private int numOfProjects;
	
	public Runner(){
		
	}
	
	public void loadConfigurations(){		
		Properties prop = new Properties();				
		try {
			prop.load(new FileInputStream("evaluation.properties"));		
			this.srcDir=prop.getProperty("sourceDirectory");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return;
	}
			
	
	public void run(){		
		System.out.println("CrossRec: Recommender System!");
		loadConfigurations();	
		createFakeProjects("libraries.txt");
	
	}

	/*populate a set of fake projects from the list of libraries*/	
	public void createFakeProjects(String list) {
			
		
		/*the malicious lib, needs to be inserted to all fake projects*/
		String pump_lib = "org.mockito:mockito-core";
		String malLib = "#DEP#" + pump_lib;
		
		String rootName = "forged__project-";
					
		int numOfProjects = 40;
		int numOfLibraries = 9;
		System.out.println("Number of projects: " + numOfProjects);
		/*consider only the first most 50 popular libraries*/
		
		int maxLib = 120;
		
		Map<Integer,String> libraries = new HashMap<Integer,String>();				
				
		DataReader reader = new DataReader();
									
		/*create dictionary and graph file*/
		
		/*add the malicious library*/
		
		//git://github.com/alexholmes/hadoop-utils.git
		
//		testcontainers__testcontainers-java
		
		BufferedWriter writer1 = null, writer2 = null;
		
		String fileName1 = "", fileName2 = "";
					
		String line = "";
		
		String fileName = this.srcDir + list;
		
		libraries = reader.readLibraryList(fileName,maxLib, pump_lib);
		
		for(int i=0;i<numOfProjects;i++) {
			
			/*read the list of libraries for every fake project*/
			
			Map<Integer,Boolean> marks = new HashMap<Integer,Boolean>();
						
			String projectName = rootName + Integer.toString(i);
			
			fileName1 = this.srcDir + "dicth_" + projectName;
			fileName2 = this.srcDir + "graph_" + projectName;
			
			int index = 1;
			
			int size = libraries.size();
															
			Random rand = new Random();
						
			int choosenIndex = -1;
									
			String selectedLibrary = "";
			boolean mark = false;
											
									
			try {							
				writer1 = new BufferedWriter(new FileWriter(fileName1));	
				writer2 = new BufferedWriter(new FileWriter(fileName2));
				System.out.println(fileName1);
				projectName = projectName.replace("__", "/");
				projectName = "git://github.com/" + projectName + ".git";				
				line = Integer.toString(index) + "\t" + projectName;												
				writer1.append(line);							
				writer1.newLine();
				writer1.flush();				
								
				for(int j=0;j<numOfLibraries;j++) {
					
					choosenIndex = rand.nextInt(size);					
					if(marks.containsKey(choosenIndex)) {
						mark = marks.get(choosenIndex);					
						while(mark == true) {					
							choosenIndex = rand.nextInt(size);							
							if(marks.containsKey(choosenIndex))mark = marks.get(choosenIndex);
							else break;
						}							
					} 
					
					index++;
					
					marks.put(choosenIndex,true);
					selectedLibrary = libraries.get(choosenIndex);
										
					line = Integer.toString(index) + "\t" + selectedLibrary;
					
					writer1.append(line);							
					writer1.newLine();
					writer1.flush();
					
					/*the graph file*/
					line = "1#" + Integer.toString(index);
					writer2.append(line);							
					writer2.newLine();
					writer2.flush();
					
				}					
				
				index++;
				
				/*add the malicious library*/
				
				line = Integer.toString(index) + "\t" + malLib;
				
				writer1.append(line);							
				writer1.newLine();
				writer1.flush();
				
				line = "1#" + Integer.toString(index);
				writer2.append(line);							
				writer2.newLine();
				writer2.flush();
								
								
				writer1.close();			
				writer2.close();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
						
			
		}			
		
		
		return;
	}
	
	
	
	public int getNumberOfProjects(String filename) {
		int count = 0;
		
		
		
		return count;
	}
	
	
	
	
	/*count the number of occurrence of a library in the whole dataset*/
	
	public int countLibFrequency(String lib) {
		int count = 0;
		DataReader reader = new DataReader();
		Map<Integer, String> projects = new HashMap<Integer, String>();
		String filename = this.srcDir + "projects.txt";
		projects = reader.readProjectList(filename);
		
		Set<Integer> keySet = projects.keySet();
		count = 0;
	
		for(Integer key:keySet) {
			String project = projects.get(key);
			project = project.replaceAll("/", "__");
			project = "dicth_" + project;
			count += reader.readOneProject(this.srcDir + project, lib);
		}
		
				
		return count;
	}
	
	
	
		
	
	
	
	
	
	
	
	
	/*get the complete list of libraries*/
	
	public void listAllLibraries() {
		
		Set<String> libraries = new HashSet<String>();
						
		DataReader reader = new DataReader();
		Map<Integer, String> projects = new HashMap<Integer, String>();
		String filename = this.srcDir + "projects.txt";
		projects = reader.readProjectList(filename);
		
		Set<Integer> keySet = projects.keySet();
				
		Map<String,Double> map = new HashMap<String,Double>();
				
	
		for(Integer key:keySet) {
			String project = projects.get(key);
			project = project.replaceAll("/", "__");
			project = "dicth_" + project;		
			
			libraries = reader.readLibrariesOfAProject(this.srcDir + project);
						
			for(String lib:libraries) {
				double count = 0;
				if(map.containsKey(lib)) {
					count = map.get(lib) + 1;					
				} else count = 1;				
				map.put(lib,count);				
			}			
		}
		
				
		ValueComparator bvc =  new ValueComparator(map);        
		TreeMap<String,Double> sorted_map = new TreeMap<String,Double>(bvc);
		sorted_map.putAll(map);				
		Set<String> keySet2 = sorted_map.keySet();
				
		
		try {
			String tmp = this.srcDir + "libraries.txt";
			BufferedWriter writer = new BufferedWriter(new FileWriter(tmp));
			
			int count = 0;
			
			for(String key:keySet2){					
				String content = Integer.toString(count) + "\t" + "#DEP#"+ key ;// + "\t" + map.get(key);					
				writer.append(content);							
				writer.newLine();
				writer.flush();		
				count++;
			}			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	
	
	
	
	
	
	
	
	
	public void countLibraries() {
		
		Set<String> libraries = new HashSet<String>();
		DataReader reader = new DataReader();
		Map<Integer, String> projects = new HashMap<Integer, String>();
		String filename = this.srcDir + "projects.txt";
		projects = reader.readProjectList(filename);
		
		Set<Integer> keySet = projects.keySet();
		
	
		for(Integer key:keySet) {
			String project = projects.get(key);
			project = project.replaceAll("/", "__");
			project = "dicth_" + project;
			
			libraries.addAll(reader.readLibrariesOfAProject(this.srcDir + project));
		}
		
		System.out.print("The number of libraries is: " + libraries.size());
		
		return;
	}
	
	
	
	
	
	
	public void listRecommendedLibraries() {
		
		Set<String> libraries = new HashSet<String>();
		
		DataReader reader = new DataReader();		
		String filename = this.srcDir + "projects.txt";	
		numOfProjects = reader.countNumOfProjects(filename);
		
		System.out.println("Number of projects: " + numOfProjects);
		
		String path = "";
		
		int step = (int)numOfProjects/10;								
		
		
		for(int i=0;i<10;i++) {
			
			int k=i+1;
			
//			int trainingStartPos1 = 1;			
//			int trainingEndPos1 = i*step;			
//			int trainingStartPos2 = (i+1)*step+1;
//			int trainingEndPos2 = numOfProjects;
			
			int testingStartPos = 1+i*step;
			int testingEndPos =   (i+1)*step;
				
			Map<Integer,String> testingProjects = new HashMap<Integer,String>();
			testingProjects = reader.readProjectList(this.srcDir + "projects.txt",testingStartPos,testingEndPos);
			
			Set<Integer> keySet = testingProjects.keySet();
			
			for(Integer key:keySet) {				
				String testingPro = testingProjects.get(key);				
				filename        = testingPro.replace("/", "__");				
				path = this.srcDir +"Round" + Integer.toString(k) + "/Recommendations/";
				path = this.srcDir +"Round" + Integer.toString(k) + "/GroundTruth/";
				libraries.addAll(reader.readLibrariesOfAProject(path + filename));
//				libraries.addAll(reader.readRecommendedLibrariesOfAProject(path + filename));
			}
			
			
			
		}
		
		
		System.out.print("The total number of recommended libraries: " + libraries.size());
		
		return;
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {	
		Runner runner = new Runner();			
		runner.run();				    		    
		return;
	}	
	
}
