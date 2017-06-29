import java.io.File;
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
		try{
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
			System.out.println("\n");
			for(String key : HASH.keySet()){
				if(!key.equals("Weight") && !key.equals("Time")){
					Collections.sort(HASH.get(key), new Sort());
				}else{
					if(key.equals("Weight")){
						Collections.sort(HASH.get(key), new SortWeight());
					}else Collections.sort(HASH.get(key), new SortTime());
				}
				int count = 0;
				double size = 0;
				String text = "";
				for(File file : HASH.get(key)){
					Double sizeFile = file.length() / 1048576d;
					BasicFileAttributes attr = null;
		        	try{
		        		attr = Files.readAttributes(Paths.get(file.getPath()), BasicFileAttributes.class);
					}catch (Exception e){
						e.printStackTrace();
					}
					Integer year = Integer.valueOf(new SimpleDateFormat("yyyy").format(attr.lastModifiedTime().toMillis()).toString());
					String newPath = file.getPath().substring(file.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
					if(!key.equals("Weight") && !key.equals("Time")){
						NUMBER++;
						if(NUMBER % (new Random().nextInt(200) + 1) == 0 || NUMBER == COUNT){
							System.out.printf("%10d/%d\n", NUMBER, COUNT);
						}
					}
					count++;
					size += file.length();
					BigDecimal sizeFileBig = new BigDecimal(sizeFile.toString());
					sizeFileBig = sizeFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
		        	String date = new SimpleDateFormat("dd.MM.yyyy").format(attr.lastModifiedTime().toMillis()).toString();
					text += date + "   " + sizeFileBig + "МБ   " + newPath + "\n";
					if(sizeFile >= 70d || year < 2015){
						file.delete();
					}
				}
				try{
					Double sizeFile = size / 1048576d;
					BigDecimal sizeFileBig = new BigDecimal(sizeFile.toString());
					sizeFileBig = sizeFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
					Double sizeAllFile = size / count / 1048576;
					BigDecimal sizeAllFileBig = new BigDecimal(sizeAllFile.toString());
					sizeAllFileBig = sizeAllFileBig.setScale(3, BigDecimal.ROUND_HALF_UP);
					String filePath = preFile + "\\(" + key + ") (" + count + ") (" + sizeFileBig + "МБ) (" + sizeAllFileBig + "МБ)" + ".txt";
					File file = new File(filePath);
					file.createNewFile();
					PrintWriter pw = null;
					pw = new PrintWriter(file.getAbsoluteFile());
					pw.print(text);
				    pw.close();
				}catch(Exception e){
					e.printStackTrace();
				}
		    }
			System.out.println("Finish.");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	static class Sort implements Comparator<File>{
        @SuppressWarnings("null")
		@Override
        public int compare(File arg_0, File arg_1){
        	try{
	        	String newPath_1 = arg_1.getPath().substring(arg_1.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
	        	String newPath_0 = arg_0.getPath().substring(arg_0.getPath().indexOf(NAME_CURRENT_DIR) + NAME_CURRENT_DIR.length() + 1);
	        	return newPath_1.compareTo(newPath_0);
        	}catch(Exception e){
        		return (Integer) null;
        	}
        }
	}
	static class SortWeight implements Comparator<File>{
        @SuppressWarnings("null")
		@Override
        public int compare(File arg_0, File arg_1){
        	try{
	        	Long file_1 = arg_1.length();
	        	Long file_0 = arg_0.length();
	        	return file_1.compareTo(file_0);
        	}catch(Exception e){
        		return (Integer) null;
        	}
        }
	}
	static class SortTime implements Comparator<File>{
        @SuppressWarnings("null")
		@Override
        public int compare(File arg_0, File arg_1){
        	try{
	        	BasicFileAttributes attr_1 = null;
	        	try{
	        		attr_1 = Files.readAttributes(Paths.get(arg_1.getPath()), BasicFileAttributes.class);
				}catch (Exception e){
					e.printStackTrace();
				}
	        	Long file_1 = attr_1.lastModifiedTime().toMillis();
	        	BasicFileAttributes attr_0 = null;
	        	try{
	        		attr_0 = Files.readAttributes(Paths.get(arg_0.getPath()), BasicFileAttributes.class);
				}catch (Exception e){
					e.printStackTrace();
				}
	        	Long file_0 = attr_0.lastModifiedTime().toMillis();
	        	return file_1.compareTo(file_0);
        	}catch(Exception e){
        		return (Integer) null;
        	}
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
				}catch (Exception e){
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
		        if(HASH.get("Weight") == null){
		        	HASH.put("Weight", new ArrayList<>());
		        }
		        HASH.get("Weight").add(file);
		        if(HASH.get("Time") == null){
		        	HASH.put("Time", new ArrayList<>());
		        }
		        HASH.get("Time").add(file);
	        }
	    }
	}
	public static String getFormat(String arg_0){
		try{
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
		}catch(Exception e){
    		return null;
    	}
	}
}
