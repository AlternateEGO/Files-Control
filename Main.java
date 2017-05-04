import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class Main{
	private static HashMap<String, ArrayList<File>> HASH = new HashMap<String, ArrayList<File>>();
	private static String DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private static int COUNT = 0;
	private static int NUMBER = 0;
	private static String NAME_CURRENT_DIR;
	
	public static void main(String[] args){
		System.out.println("Start.\nClear logs.");
		String def = Paths.get(System.getProperty("user.dir")).toString();
		String path = def + "//FilesControl//" + DATE + "//";
		File preFile = new File(path);
		preFile.mkdirs();
		for(File delete : preFile.listFiles()){
			if(delete.isFile()){
				delete.delete();
			}
		}
		System.out.println("Get files.");
		String pathFiles = def;
		File currentDir = new File(pathFiles);
		NAME_CURRENT_DIR = currentDir.getName();
		System.out.println("Read files.");
		getFiles(new File(pathFiles));
		for(String key : HASH.keySet()){
			Collections.sort(HASH.get(key), new Sort());
			int count = 0;
			double size = 0;
			String text = "";
			for(File file : HASH.get(key)){
				count++;
				NUMBER++;
				if(NUMBER % (new Random().nextInt(200) + 1) == 0 || NUMBER == COUNT){
					System.out.printf("%10d/%d\n", NUMBER, COUNT);
				}
				size += file.length();
				String newPath = file.getPath().substring(file.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
				Double sizeFile = file.length() / 1048576d;
				BigDecimal sizeFileBig = new BigDecimal(sizeFile.toString());
				sizeFileBig = sizeFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
				text += sizeFileBig + "МБ   " + newPath + "\n";
			}
			Double sizeFile = size / 1048576d;
			BigDecimal sizeFileBig = new BigDecimal(sizeFile.toString());
			sizeFileBig = sizeFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
			Double sizeAllFile = size / count / 1048576d;
			BigDecimal sizeAllFileBig = new BigDecimal(sizeAllFile.toString());
			sizeAllFileBig = sizeAllFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
			String filePath = preFile + "\\(" + key + ") (" + count + ") (" + sizeFileBig + "МБ) (" + sizeAllFileBig + "МБ)" + ".txt";
			File file = new File(filePath);
			try{
				file.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
			PrintWriter pw = null;
			try{
				pw = new PrintWriter(file.getAbsoluteFile());
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			pw.print(text);
		    pw.close();
	    }
		System.out.println("Finish.");
		
	}
	
	static class Sort implements Comparator<File>{
        @Override
        public int compare(File arg_0, File arg_1){
        	String newPath_1 = arg_1.getPath().substring(arg_1.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
        	String newPath_0 = arg_0.getPath().substring(arg_0.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
        	return newPath_1.compareTo(newPath_0);
        }
	}
	
	public static int COUNT_FILES = 0;
	
	public static void getFiles(File arg_0){
	    File[] files = arg_0.listFiles();
	    for (File file : files){
	        if(file.isDirectory()){
	        	if(file.getPath().indexOf("FilesControl") == -1){
	        		getFiles(file);
	        		continue;
	        	}
	        	continue;
	        }
	        if(file.getPath().toString().lastIndexOf("FilesControl") == -1){
	        	COUNT_FILES ++;
	        	if(COUNT_FILES % 100 == 0) System.out.print(".");
	        	BasicFileAttributes attr = null;
	        	try{
					attr = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
				}catch (IOException e){
					e.printStackTrace();
				}
		        String par_1 = getFormat(file.getPath());
		        if(HASH.get(par_1) == null){
		        	HASH.put(par_1, new ArrayList<>());
		        }
		        HASH.get(par_1).add(file);
		        COUNT++;
		        if(attr.lastModifiedTime().toString().indexOf(DATE) != -1){
					if(HASH.get("NewFiles") == null){
			        	HASH.put("NewFiles", new ArrayList<>());
			        }
			        HASH.get("NewFiles").add(file);
				}
	        }
	    }
	}
	
	public static String getFormat(String arg_0){
		arg_0 = arg_0.toLowerCase();
		if(arg_0.lastIndexOf('.') != -1){
			String par_0 = arg_0.substring(arg_0.lastIndexOf('.') + 1);
			if(par_0.lastIndexOf(Paths.get("\\").toString()) == -1){
				return par_0;
			}
		}else{
			return " ";
		}
		return " ";
	}
}
