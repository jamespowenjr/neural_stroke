package edu.jhu.nn.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.jhu.nn.input.normalizer.ConsciousStateNormalizer;
import edu.jhu.nn.input.normalizer.GenderNormalizer;
import edu.jhu.nn.input.normalizer.Normalizer;
import edu.jhu.nn.input.normalizer.NumericalNormalizer;
import edu.jhu.nn.input.normalizer.YesNoNormalizer;

public class SampleDataGenerator {

	private static final String DEFAULT_FILE = "/all.csv";
	
	private static final List<Integer> DEFAULT_COLUMNS = Arrays.asList(1, 2, 3, 4);
	
	private static final int TOTAL_LINES = 19435;
	private static final int DEFAULT_START_LINE = 1;
	private static final int DEFAULT_END_LINE = 500;
	private static final int DEFAULT_LINES = 50;
	private static final int OUTPUT_SETS = 10;
	
	private static final String OUTPUT_FOLDER = "output";
	
	private static final String DEFAULT_OUTPUT_FILENAME = "test";
	
	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd_kk-mm");
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		SampleDataGenerator generator = new SampleDataGenerator();
		
		String inputFile = DEFAULT_FILE;
		String outputFile = OUTPUT_FOLDER + "/" + FORMAT.format(System.currentTimeMillis());
		File outputDir = new File(outputFile);
		outputDir.mkdir();
		
		outputFile += "/" + DEFAULT_OUTPUT_FILENAME + "-";
		Map<Integer,Normalizer> inputColumnMap = new TreeMap<Integer,Normalizer>();
		inputColumnMap.put(3, new ConsciousStateNormalizer());
		inputColumnMap.put(4, new GenderNormalizer());
		inputColumnMap.put(5, new NumericalNormalizer(100.0));
		inputColumnMap.put(12, new NumericalNormalizer(200.0));
		inputColumnMap.put(13, new YesNoNormalizer());
		inputColumnMap.put(14, new YesNoNormalizer());
		inputColumnMap.put(15, new YesNoNormalizer());
		inputColumnMap.put(16, new YesNoNormalizer());
		
		Map<Integer,Normalizer> outputColumnMap = new TreeMap<Integer,Normalizer>();
		inputColumnMap.put(50, new YesNoNormalizer());
		inputColumnMap.put(51, new YesNoNormalizer());
		inputColumnMap.put(52, new YesNoNormalizer());
		inputColumnMap.put(53, new YesNoNormalizer());
		
		boolean randomLines = true;
		
		int start = DEFAULT_START_LINE;
		int end = DEFAULT_END_LINE;
		int span = DEFAULT_END_LINE - DEFAULT_START_LINE;
		
		for(int i = 0; i < OUTPUT_SETS; i++){
		
			if(randomLines){
				generator.loadFileRandom(inputFile, outputFile + i + ".csv", inputColumnMap, outputColumnMap, start, end, DEFAULT_LINES);
			}
			else{
				generator.loadFile(inputFile, outputFile + i + ".csv", inputColumnMap, outputColumnMap, start, DEFAULT_LINES);
			}
			
			start = end + 1;
			end = start + span;
		}
		generator.loadFile(inputFile, outputFile + "all.csv", inputColumnMap, outputColumnMap, 1, TOTAL_LINES);
	}
	
	public void loadFile(String inputFile, String outputFile, Map<Integer,Normalizer> inputColumnMap, Map<Integer,Normalizer> outputColumnMap, int startLine, int lines) throws IOException{
		InputStream stream = this.getClass().getResourceAsStream(inputFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		PrintWriter out = new PrintWriter(new File(outputFile));
		
		String line = reader.readLine();
		
		//parse header line
		//out.write("LINE,");
		parseHeaderLine(line, inputColumnMap, outputColumnMap, out);
		
		for(int i = 1; i < startLine; i++){
			reader.readLine();
		}
		
		int count = 1;
		while((line = reader.readLine()) != null && count <= lines){
			parseLine(count + startLine, line, inputColumnMap, outputColumnMap, out);
			count++;
		}
		
		out.close();
		
	}
	
	public void loadFileRandom(String inputFile, String outputFile, Map<Integer,Normalizer> inputColumnMap, Map<Integer,Normalizer> outputColumnMap, int startLine, int endLine, int lines) throws IOException{
		InputStream stream = this.getClass().getResourceAsStream(inputFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		PrintWriter out = new PrintWriter(new File(outputFile));
		
		String line = reader.readLine();
		
		//parse header line
		//out.write("LINE,");
		parseHeaderLine(line, inputColumnMap, outputColumnMap, out);
		
		for(int i = 1; i < startLine; i++){
			reader.readLine();
		}
		
		Random random = new Random();
		Set<Integer> lineSet = new TreeSet<Integer>();
		
		while(lineSet.size() < lines){
			lineSet.add(random.nextInt(endLine - startLine + 1));
		}
		System.out.println(lineSet);
		
		int lineNumber = 0;
		int count = 1;
		while((line = reader.readLine()) != null && count <= lines){
			if(lineSet.contains(lineNumber)){
				parseLine(lineNumber + startLine + 1, line, inputColumnMap, outputColumnMap, out);
				count++;
			}
			lineNumber++;
		}
		
		out.close();
		
	}
	
	public void parseHeaderLine(String line, Map<Integer,Normalizer> inputColumnMap, Map<Integer,Normalizer> outputColumnMap, PrintWriter out){
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(",");
		
		int column = 1;
		int writes = 0;
		StringBuilder output = new StringBuilder();
		while(scanner.hasNext()){
			Normalizer normalizer = inputColumnMap.get(column);
			if(normalizer != null){
				if(writes != 0){
					out.write(",");
				}
				out.write(scanner.next());
				writes++;
			}
			else{
				normalizer = outputColumnMap.get(column);
				if(normalizer != null){
					output.append(",");
					output.append(scanner.next());
				}
				else{
					scanner.next();
				}
			}
			column++;
		}
		out.write(output.toString());
		out.write("\n");
		out.flush();
	}
	
	public void parseLine(int lineNum, String line, Map<Integer,Normalizer> inputColumnMap, Map<Integer,Normalizer> outputColumnMap, PrintWriter out){
		//out.write("" + lineNum);
		
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(",");
		
		int column = 1;
		int writes = 0;
		StringBuilder output = new StringBuilder();
		while(scanner.hasNext()){
			Normalizer normalizer = inputColumnMap.get(column);
			if(normalizer != null){
				if(writes != 0){
					out.write(",");
				}
				out.write(normalizer.normalize(scanner.next()));
				writes++;
			}
			else{
				normalizer = outputColumnMap.get(column);
				if(normalizer != null){
					output.append(",");
					output.append(normalizer.normalize(scanner.next()));
				}
				else{
					scanner.next();
				}
			}
			column++;
		}
		
		out.write("\n");
		out.flush();
	}

}
