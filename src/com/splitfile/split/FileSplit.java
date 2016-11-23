package com.splitfile.split;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

class FileSplit {
	static final String mp3SourceFile = "C:\\Users\\chand\\Desktop\\parts\\music.mp3";
	static final String splitFilePath = "C:\\Users\\chand\\Desktop\\parts\\split";
	static final String margedFilePath = "C:\\Users\\chand\\Desktop\\musicJoined.mp3";

	public static void mergeFiles(List<File> files, File into)
			throws IOException {
		try (BufferedOutputStream mergingStream = new BufferedOutputStream(
				new FileOutputStream(into))) {
			for (File f : files) {
				Files.copy(f.toPath(), mergingStream);
			}
		}
	}

	public static void splitFile(File f) throws IOException {
		int partCounter = 1;
		int sizeOfFiles = 1024 * 1024;// 1MB
		byte[] buffer = new byte[sizeOfFiles];

		try (BufferedInputStream bis = new BufferedInputStream(
				new FileInputStream(f))) {
			String name = f.getName();

			int tmp = 0;
			while ((tmp = bis.read(buffer)) > 0) {
				File newFile = new File(f.getParent() + "\\split\\", name + "."
						+ String.format("%03d", partCounter++));
				try (FileOutputStream out = new FileOutputStream(newFile)) {
					out.write(buffer, 0, tmp);// tmp is chunk size
				}
			}
		}
	}

	public static List<File> listFilesForFolder(final File folder) {
		List<File> allSplitFiles = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				System.out.println(fileEntry.getName());
				allSplitFiles.add(fileEntry);
			}
		}
		return allSplitFiles;
	}

	public static void main(String[] args) throws IOException {
		splitFile(new File(mp3SourceFile));
		final File folder = new File(splitFilePath);
		mergeFiles(listFilesForFolder(folder), new File(margedFilePath));

	}
}