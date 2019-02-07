package be.jtb.vds.documentmover.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtils {

public static void copyFileJava7(File sourceFile, File destFile){
//	Files.move(sourceFile.toPath(), newFileDest.toPath());
}

	public static void copyFile(File sourceFile, File destFile) throws IOException {
		if(!destFile.exists()) {
			if(!destFile.getParentFile().exists()){
				destFile.getParentFile().mkdirs();
			}
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();

	        // previous code: destination.transferFrom(source, 0, source.size());
	        // to avoid infinite loops, should be:
	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}
	
	public static void moveFile(File sourceFile, File destFile) throws IOException{
		try {
			copyFile(sourceFile, destFile);
		} catch (IOException e) {
			destFile.delete();
			throw e;
		}
		
		sourceFile.delete();
	}
	
}
