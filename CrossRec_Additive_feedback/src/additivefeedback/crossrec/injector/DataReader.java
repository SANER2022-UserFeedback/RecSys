package additivefeedback.crossrec.injector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class DataReader {

	
	
	public DataReader() {		
		
	}
	
	
	
	
	
	/*read the list of libraries from a file*/
	
	public Map<Integer, String> readLibraryList(String filename, int maxNum, String pumpLib) {		
		Map<Integer, String> map = new HashMap<Integer, String>();		
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				if(!vals[1].trim().equals("#DEP#" + pumpLib))
				{
					int ID = Integer.parseInt(vals[0].trim());				
					String lib = vals[1].trim();		
					map.put(ID, lib);
					if(ID==maxNum)break;
				}
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}						
		return map;		
	}
	
	
	
	
	
	
	
	
	
	public Map<Integer, String> readRepositoryList(String filename){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",uri="";
		int id=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split("\t");
				id=Integer.parseInt(vals[0].trim());
				uri=vals[1].trim();//.replace("http://dbpedia.org/resource/", "");
				ret.put(id,uri);							
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;			
	}
	
	
	public Map<Integer, String> readRepositoryList2(String filename){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",uri="";
		int id=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split(",");					
				uri="git://github.com/"+vals[1].trim() + ".git";				
				ret.put(id,uri);	
				id+=1;
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return ret;			
	}

	
	public Map<Integer, String> readRepositoryList3(String filename){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",uri="";
		int id=0;
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split("\t");					
				uri=vals[1].trim();				
				ret.put(id,uri);	
				id+=1;
			}
				
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
		
	
	public Map<Integer, String> readProjectList2(String filename, int startPos, int endPos){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",repo="";
		int count=1;
		int id=startPos;
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			while (count< startPos) {
				line = reader.readLine();
				count++;
			}
			
			while (((line = reader.readLine()) != null)) {
				line = line.trim();							
				vals = line.split(",");					
				repo=vals[0].trim();				
				ret.put(id,repo);
//				System.out.println(id + " : " +repo);
				id++;
				count++;
				if(count>endPos)break;
			}						
				
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
	
	public Map<Integer, String> readProjectList(String filename, int startPos, int endPos){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",repo="";
		int count=1;
		int id=startPos;
		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			
			while (count< startPos) {
				line = reader.readLine();
				count++;
			}
			
			while (((line = reader.readLine()) != null)) {
				line = line.trim();		
//				repo=line;
				
				vals = line.split(",");					
				repo=vals[0].trim();			
				
				ret.put(id,repo);				
				id++;
				count++;
				if(count==endPos)break;
			}						
				
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return ret;				
	}
	
	
	
	/*read all the projects*/
	
	public Map<Integer, String> readProjectList(String filename){		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String line="",repo="";
		int id=1;		
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (((line = reader.readLine()) != null)) {
				line = line.trim();		
				repo=line;			
				ret.put(id,repo);				
				id++;
			}						
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return ret;				
	}
	
	
	public int countNumOfProjects(String filename){		
		int count = 0;		
		String line="";
			
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (((line = reader.readLine()) != null)) {
				count++;
			}						
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return count;				
	}
	
	
	
	public int readOneProject(String filename, String library){		
		int count = 0;
		String line="",dependency="";	
		String[] parts = null;
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (((line = reader.readLine()) != null)) {
				 
				if(line.contains("#DEP#")) {
					parts = line.split("\t");
					dependency = parts[1].trim();
					dependency = dependency.replace("#DEP#", "");
//					System.out.println(dependency + "\t" + library);
					if(dependency.equals(library))count++;
				}
			}						
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return count;				
	}
	
	
	
	
	
	
	public Set<String> readLibrariesOfAProject(String filename){		
		
		Set<String> libraries = new HashSet<String>(); 
		
		
		String line="",dependency="";	
		String[] parts = null;
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (((line = reader.readLine()) != null)) {
				 
				if(line.contains("#DEP#")) {
					parts = line.split("\t");
					dependency = parts[1].trim();
					dependency = dependency.replace("#DEP#", "");
					libraries.add(dependency);
				}
			}						
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();			
		}		
		
		return libraries;				
	}
	
	
	
	public Set<String> readRecommendedLibrariesOfAProject(String filename){		
		
		Set<String> libraries = new HashSet<String>(); 
		
		
		String line="",dependency="";	
		String[] parts = null;
		try {					
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (((line = reader.readLine()) != null)) {
				 
				if(line.contains("#DEP#")) {
					parts = line.split("\t");
					dependency = parts[0].trim();
					dependency = dependency.replace("#DEP#", "");
					libraries.add(dependency);
				}
			}						
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();			
		}		
		
		return libraries;				
	}
	

	
	
	
	
	public Map<Integer, String> readMovieLenList(String filename){
		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",uri="";
		int id=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split("\t");
				id=Integer.parseInt(vals[0].trim());
				uri=vals[2].trim().replace("http://dbpedia.org/resource/", "");
				ret.put(id,uri);							
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;			
	}
	
	public Map<Integer,String> readLastFMMostPopularArtistList(String filename) {	
		
		Map<Integer,String> ret = new HashMap<Integer,String>();
		String[] vals = null;		
		String line="",uri="";
		int id=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split("\t");
				id=Integer.parseInt(vals[0].trim());
				uri=vals[1].trim();
				ret.put(id,uri);							
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;		
	}
	
	
	
	public List<String> readSimilarityArtist(String filename){
		
		List<String> ret = new ArrayList<String>();		
		String line="",URI="";			
		String[] vals = null;
		float val=0;
		BufferedReader reader = null;
	
		try {
			reader = new BufferedReader(new FileReader(filename));		
			try {
				while ((line = reader.readLine()) != null) {				
					vals = line.split("\t");
					URI = vals[0].trim();
					//if(!URI.contains("<"))
					ret.add(URI);									
				}
			} finally {
				reader.close();
			}			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	public ArrayList<List<String>> readResultMatrix(String filename) {
		
		ArrayList<List<String>> ArtistMatrix = new ArrayList<List<String>>();
		
		String line="";
		
		String[] vals = null;
		
		List<String> list;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {
				
				vals = line.split(",");			
				
				list = new ArrayList<String>();				
				
				for (String sim : vals) {			
					
					list.add(sim.trim());				
				}
				
				ArtistMatrix.add(list);							
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return ArtistMatrix;
	}
	
	
	
	
	
	
	
	
	
	
	/* Load the list of node weights */
		
	public Map<Integer, Double> readNodeWeight(String filename) {	
		
		Map<Integer, Double> list = new HashMap<Integer, Double>();
				
		String line="",node1="";
		List<String> nodes = new ArrayList<String>();
		String[] vals = null;
		double val = 0;
		int nodeID = 0;
		
		Map<String, Integer> dictionary = readDictionary("C:\\Temp\\CROSSMINER\\New582\\Dictionary.txt");
			
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));		
			while ((line = reader.readLine()) != null) {				
				line = line.trim();
				vals = line.split(";");								
				node1= vals[0].trim();							
				val= Double.parseDouble(vals[4].trim().replace(",", "."));
//				System.out.println(val);
				nodeID = dictionary.get(node1);				
				list.put(nodeID, val);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	
		return list;		
	}
	

	
		
//	public List<String> readArtistList2(String filename) {		
//		List<String> ret = new ArrayList<String>();				
//		String line="";				
//		try {
//			BufferedReader reader = new BufferedReader(new FileReader(filename));						
//			while ((line = reader.readLine()) != null) {				
//				line = line.trim();				
//				ret.add(line);							
//			}
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return ret;		
//	}
//	
	
	public List<String> readMovieList(String filename) {		
		List<String> ret = new ArrayList<String>();				
		String line="";				
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				line = line.trim();				
				ret.add(line);							
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;		
	}
	
	
	
	public List<String> readResultPyramid(String filename) {
		
		List<String> ret = new ArrayList<String>();
				
		String line="";
		
		String[] vals = null;
				
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {
				
				line = line.trim();
				
				vals = line.split(",");
				
				for (String sim : vals) {			
					ret.add(sim.trim());						
				}
															
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;		
	}
	
	
	
	public Map<String, Integer> readAset400List() {
		
		Map<String, Integer> ret = new HashMap<String, Integer>();
		
		int index = 0;
				
		String line="";
				
		try {
			BufferedReader reader = new BufferedReader(new FileReader("/home/nguyen/Public/Evaluation/aset400.txt"));
						
			while ((line = reader.readLine()) != null) {
				
				line = line.trim();
				
				ret.put(line, index);							
				
				index += 1;				
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		
		return ret;
		
	}
	
	
	
	
	
		
		
	
	public Map<String, Integer> readDictionary(String filename) {							
		Map<String, Integer> vector = new HashMap<String, Integer>();		
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {
										
				vals = line.split("\t");
			
				int ID = Integer.parseInt(vals[0].trim());
				
				String URI = vals[1].trim();
		
				vector.put(URI, ID);							
			}
			
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return vector;		
	}
	
	
	public Map<Integer, String> readDictionary2(String filename) {							
		Map<Integer, String> vector = new HashMap<Integer, String>();		
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				int ID = Integer.parseInt(vals[0].trim());				
				String URI = vals[1].trim();		
				vector.put(ID, URI);							
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return vector;		
	}
	
	
	public Map<String, String> readDictionary3(String filename) {							
		Map<String, String> vector = new HashMap<String, String>();		
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {
										
				vals = line.split("\t");			
				String ID = vals[0].trim();				
				String URI = vals[1].trim();		
				vector.put(ID, URI);							
			}
			
			reader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return vector;		
	}
		
		
	
	
	
	/*Read dictionary, get only liraries and the first line*/
	
	public Map<Integer, String> readDictionary4(String filename) {							
		Map<Integer, String> vector = new HashMap<Integer, String>();		
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
						
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				int ID = Integer.parseInt(vals[0].trim());				
				String artifact = vals[1].trim();				
				if(ID==1 || artifact.contains("#DEP#"))vector.put(ID, artifact);				
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return vector;		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Map<Integer, String> extractHalfDictionary(String filename, String groundTruthPath, boolean getAlsoUsers, double ratio) {							
		Map<Integer, String> dict = new HashMap<Integer, String>();
		Map<Integer, String> ret = new HashMap<Integer, String>();
		String line = null;		
		String[] vals = null;
		int part=0;
		int libCount = 0;
		int size = 0;
		
//		System.out.println(filename);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				vals = line.split("\t");			
//				System.out.println(vals[0]);
				int ID = Integer.parseInt(vals[0].trim());				
				String artifact = vals[1].trim();				
				dict.put(ID, artifact);
				if(artifact.contains("#DEP#"))size++;
			}	
//			System.out.println("libCount " + libCount);
			reader.close();
			
						
			Set<Integer> keySet = dict.keySet();			
			part = (int) Math.round(size*ratio);			
								
			boolean enoughLib = false;
			libCount = 0;			
				
			int pos = filename.lastIndexOf("/");				
			String fname = filename.substring(pos+1,filename.length());		
			fname = fname.replace("dicth_", "");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(groundTruthPath + fname));
					
			/*Read a half of the dictionary and all users, the other half is put into the ground-truth data*/
			
			for(Integer key:keySet) {					
				String artifact = dict.get(key);
				if(libCount==part)enoughLib=true;							
				if(artifact.contains("#DEP#")) {					
					if(!enoughLib) {						
//						if(filename.contains("ostrich_2_9_29_1_"))System.out.println(artifact);						
						ret.put(key, artifact);
					}
					else {						
						String content = key + "\t" + artifact;					
						writer.append(content);							
						writer.newLine();
						writer.flush();						
					}
					libCount++;				
				} else {
					/*put users into the dictionary*/					
//					if(getAlsoUsers || artifact.contains("git://github.com/"))ret.put(key, artifact);
					if(getAlsoUsers || !artifact.contains("#DEP#"))ret.put(key, artifact);
				}									
			}
			
			writer.close();			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}						
		return ret;		
	}
	
	
	
	
	
	
	
	
	/*read only the first half of the libraries*/
		
	public Set<String> getOnePartOfLibraries(String filename, double ratio) {							
		Map<Integer, String> dict = new HashMap<Integer, String>();
		Set<String> ret = new HashSet<String>();
		String line = null;		
		String[] vals = null;
		int libCount = 0, part=0;
		int size = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				vals = line.split("\t");			
				int ID = Integer.parseInt(vals[0].trim());				
				String artifact = vals[1].trim();				
				dict.put(ID, artifact);			
				if(artifact.contains("#DEP#"))size++;
			}		
			reader.close();
			
			
			Set<Integer> keySet = dict.keySet();			
			
			part = (int) Math.round(size*ratio);		
			
			libCount=0;			
			for(Integer key:keySet) {					
				String artifact = dict.get(key);							
				if(artifact.contains("#DEP#")) {					
					ret.add(artifact);
					libCount++;				
				}
				if(libCount==part)break;
			}			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}						
		return ret;		
	}
	
		
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void getProjectDetails(String filename, Map<String,Set<String>> methodInvocations) {		
		Set<String> vector = null;				
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				vals = line.split("\t");								
				String artifact = vals[1].trim();											
				if(artifact.contains("MI_")) {				
					String[] parts = artifact.split("#");					
					String md = parts[0].trim().replace("MI_", "");
					String mi = parts[1].trim();					
					if(methodInvocations.containsKey(md))vector=methodInvocations.get(md);
					else vector = new HashSet<String>();
					vector.add(mi);
					methodInvocations.put(md, vector);					
				}
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*get the list of libraries for one project*/
		
	public Set<String> getLibraries(String filename) {							
		Set<String> vector = new HashSet<String>();		
		String line = null;		
		String[] vals = null;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {				
				vals = line.split("\t");								
				String library = vals[1].trim();
				if(library.contains("#DEP#"))vector.add(library);		
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}						
		return vector;		
	}
	
	
	
	/*read the whole file*/
	
	
	
	
	public Map<Integer,String> readRecommendationFile(String filename) {							
		Map<Integer,String> ret = new HashMap<Integer,String>();	
		String line = null;		
		String[] vals = null;
		int id=1;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String library = vals[0].trim();			
				ret.put(id, library);
				id++;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	
	
	
	
	
	/*read a specific number of lines from the file*/
	
	public Set<String> readRecommendationFile(String filename, int size) {							
		Set<String> ret = new HashSet<String>();	
		String line = null;		
		String[] vals = null;
		int count=0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String library = vals[0].trim();					
				ret.add(library);
				count++;
				if(count==size)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return ret;		
	}
	
	
	
	
	public Set<String> readGroundTruthFile(String filename) {							
		Set<String> ret = new HashSet<String>();	
		String line = null;		
		String[] vals = null;		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));					
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				String library = vals[1].trim();					
				ret.add(library);								
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}						
		return ret;		
	}
	
		
	
	public Map<Integer, String> getMostSimilarProjects(String filename, int size) {							
		Map<Integer, String> projects = new HashMap<Integer, String>();		
		String line = null;		
		String[] vals = null;		
		int count=0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");							
				String URI = vals[1].trim();		
				projects.put(count, URI);
				count++;
				if(count==size)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return projects;		
	}
		
	
	public Map<Integer, Double> getSimilarityMatrix(String filename, int size) {							
		Map<Integer, Double> sim = new HashMap<Integer, Double>();		
		String line = null;		
		String[] vals = null;		
		int count=0;
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));						
			while ((line = reader.readLine()) != null) {										
				vals = line.split("\t");			
				Double val = Double.parseDouble(vals[2].trim());
				sim.put(count, val);
				count++;
				if(count==size)break;
			}			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
						
		return sim;		
	}
	
	
	
	
}